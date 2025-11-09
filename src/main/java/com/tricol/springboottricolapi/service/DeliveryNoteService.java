package com.tricol.springboottricolapi.service;

import com.tricol.springboottricolapi.dto.Request.DeliveryNoteLineRequestDTO;
import com.tricol.springboottricolapi.dto.Request.DeliveryNoteRequestDTO;
import com.tricol.springboottricolapi.dto.Response.DeliveryNoteResponseDTO;
import com.tricol.springboottricolapi.entity.DeliveryNote;
import com.tricol.springboottricolapi.entity.DeliveryNoteLine;
import com.tricol.springboottricolapi.entity.Product;
import com.tricol.springboottricolapi.entity.enums.ExitOrderStatus;
import com.tricol.springboottricolapi.exception.BusinessException;
import com.tricol.springboottricolapi.exception.DuplicateRessourceException;
import com.tricol.springboottricolapi.exception.ResourceNotFoundException;
import com.tricol.springboottricolapi.mapper.DeliveryNoteMapper;
import com.tricol.springboottricolapi.repository.DeliveryNoteRepository;
import com.tricol.springboottricolapi.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DeliveryNoteService {

    private final DeliveryNoteRepository deliveryNoteRepository;
    private final ProductRepository productRepository;
    private final DeliveryNoteMapper mapper;
    private final StockService stockService;


    public DeliveryNoteResponseDTO createDeliveryNote(DeliveryNoteRequestDTO requestDTO) {
        log.info("Creating delivery note: {}", requestDTO.getNoteNumber());

        if (deliveryNoteRepository.existsByNoteNumber(requestDTO.getNoteNumber())) {
            throw new DuplicateRessourceException("DeliveryNote", "noteNumber", requestDTO.getNoteNumber());
        }

        DeliveryNote deliveryNote = new DeliveryNote();
        deliveryNote.setNoteNumber(requestDTO.getNoteNumber());
        deliveryNote.setExitDate(requestDTO.getExitDate());
        deliveryNote.setWorkshop(requestDTO.getWorkshop());
        deliveryNote.setExitReason(requestDTO.getExitReason());
        deliveryNote.setComments(requestDTO.getComments());
        deliveryNote.setStatus(ExitOrderStatus.BROUILLON);

        for (DeliveryNoteLineRequestDTO lineDTO : requestDTO.getDeliveryNoteLines()) {
            Product product = productRepository.findById(lineDTO.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + lineDTO.getProductId()));

            DeliveryNoteLine line = new DeliveryNoteLine();
            line.setProduct(product);
            line.setQuantity(lineDTO.getQuantity());
            deliveryNote.addDeliveryNoteLine(line);
        }

        DeliveryNote saved = deliveryNoteRepository.save(deliveryNote);
        log.info("Delivery note created successfully: {}", saved.getNoteNumber());

        return mapper.toResponseDTO(saved);
    }


    public DeliveryNoteResponseDTO updateDeliveryNote(Long id, DeliveryNoteRequestDTO requestDTO) {
        log.info("Updating delivery note ID: {}", id);

        DeliveryNote deliveryNote = deliveryNoteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery note not found: " + id));

        if (deliveryNote.getStatus() != ExitOrderStatus.BROUILLON) {
            throw new BusinessException("Cannot update delivery note with status: " + deliveryNote.getStatus());
        }

        deliveryNote.setExitDate(requestDTO.getExitDate());
        deliveryNote.setWorkshop(requestDTO.getWorkshop());
        deliveryNote.setExitReason(requestDTO.getExitReason());
        deliveryNote.setComments(requestDTO.getComments());

        deliveryNote.getDeliveryNoteLines().clear();
        for (DeliveryNoteLineRequestDTO lineDTO : requestDTO.getDeliveryNoteLines()) {
            Product product = productRepository.findById(lineDTO.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + lineDTO.getProductId()));

            DeliveryNoteLine line = new DeliveryNoteLine();
            line.setProduct(product);
            line.setQuantity(lineDTO.getQuantity());
            deliveryNote.addDeliveryNoteLine(line);
        }

        DeliveryNote updated = deliveryNoteRepository.save(deliveryNote);
        log.info("Delivery note updated successfully: {}", updated.getNoteNumber());

        return mapper.toResponseDTO(updated);
    }



    public void deleteDeliveryNote(Long id) {
        log.info("Deleting delivery note ID: {}", id);

        DeliveryNote deliveryNote = deliveryNoteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery note not found: " + id));

        if (deliveryNote.getStatus() != ExitOrderStatus.BROUILLON) {
            throw new BusinessException("Cannot delete delivery note with status: " + deliveryNote.getStatus());
        }

        deliveryNoteRepository.delete(deliveryNote);
        log.info("Delivery note deleted successfully: {}", deliveryNote.getNoteNumber());
    }



    @Transactional(readOnly = true)
    public List<DeliveryNoteResponseDTO> getAllDeliveryNotes() {
        log.info("Getting all delivery notes");
        return deliveryNoteRepository.findAll()
                .stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }



    @Transactional(readOnly = true)
    public DeliveryNoteResponseDTO getDeliveryNoteById(Long id) {
        log.info("Getting delivery note by ID: {}", id);
        DeliveryNote deliveryNote = deliveryNoteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery note not found: " + id));
        return mapper.toResponseDTO(deliveryNote);
    }



    @Transactional(readOnly = true)
    public List<DeliveryNoteResponseDTO> getDeliveryNotesByWorkshop(String workshop) {
        log.info("Getting delivery notes for workshop: {}", workshop);
        return deliveryNoteRepository.findByWorkshop(workshop)
                .stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }



    public DeliveryNoteResponseDTO validateDeliveryNote(Long id) {
        log.info("Validating delivery note ID: {}", id);

        DeliveryNote deliveryNote = deliveryNoteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery note not found: " + id));

        if (deliveryNote.getStatus() == ExitOrderStatus.VALIDE) {
            throw new BusinessException("Delivery note already validated");
        }


        if (deliveryNote.getStatus() == ExitOrderStatus.ANNULE) {
            throw new BusinessException("Cannot validate a cancelled delivery note");
        }


        for (DeliveryNoteLine line : deliveryNote.getDeliveryNoteLines()) {
            stockService.processStockExit(
                    line.getProduct().getId(),
                    line.getQuantity(),
                    deliveryNote.getNoteNumber(),
                    "Bon de sortie: " + deliveryNote.getNoteNumber() + " - " + deliveryNote.getWorkshop()
            );
        }


        deliveryNote.setStatus(ExitOrderStatus.VALIDE);
        DeliveryNote validated = deliveryNoteRepository.save(deliveryNote);

        log.info("Delivery note validated successfully: {}", validated.getNoteNumber());
        return mapper.toResponseDTO(validated);
    }



    public DeliveryNoteResponseDTO cancelDeliveryNote(Long id) {
        log.info("Cancelling delivery note ID: {}", id);

        DeliveryNote deliveryNote = deliveryNoteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery note not found: " + id));


        if (deliveryNote.getStatus() == ExitOrderStatus.ANNULE) {
            throw new BusinessException("Delivery note already cancelled");
        }


        if (deliveryNote.getStatus() == ExitOrderStatus.VALIDE) {
            throw new BusinessException("Cannot cancel a validated delivery note. Stock has already been consumed.");
        }


        deliveryNote.setStatus(ExitOrderStatus.ANNULE);
        DeliveryNote cancelled = deliveryNoteRepository.save(deliveryNote);

        log.info("Delivery note cancelled successfully: {}", cancelled.getNoteNumber());
        return mapper.toResponseDTO(cancelled);
    }
}