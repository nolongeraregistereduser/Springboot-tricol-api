package com.tricol.springboottricolapi.service;

import com.tricol.springboottricolapi.dto.SupplierRequestDTO;
import com.tricol.springboottricolapi.dto.SupplierResponseDTO;
import com.tricol.springboottricolapi.entity.Supplier;
import com.tricol.springboottricolapi.exception.ResourceNotFoundException;
import com.tricol.springboottricolapi.mapper.SupplierMapper;
import com.tricol.springboottricolapi.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;


    @Transactional(readOnly = true)
    public List<SupplierResponseDTO> getAllSuppliers() {
        return supplierRepository.findAll()
                .stream()
                .map(supplierMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    

    @Transactional(readOnly = true)
    public SupplierResponseDTO getSupplierById(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", id));
        return supplierMapper.toResponseDTO(supplier);
    }

    
    public SupplierResponseDTO createSupplier(SupplierRequestDTO requestDTO) {
        Supplier supplier = supplierMapper.toEntity(requestDTO);
        Supplier savedSupplier = supplierRepository.save(supplier);
        return supplierMapper.toResponseDTO(savedSupplier);
    }


    public SupplierResponseDTO updateSupplier(Long id, SupplierRequestDTO requestDTO) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", id));
        
        supplierMapper.updateEntity(requestDTO, supplier);
        Supplier updatedSupplier = supplierRepository.save(supplier);
        return supplierMapper.toResponseDTO(updatedSupplier);
    }

    
    public void deleteSupplier(Long id) {
        if (!supplierRepository.existsById(id)) {
            throw new ResourceNotFoundException("Supplier", id);
        }
        supplierRepository.deleteById(id);
    }
}