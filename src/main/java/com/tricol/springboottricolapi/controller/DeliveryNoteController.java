package com.tricol.springboottricolapi.controller;

import com.tricol.springboottricolapi.dto.Request.DeliveryNoteRequestDTO;
import com.tricol.springboottricolapi.dto.Response.DeliveryNoteResponseDTO;
import com.tricol.springboottricolapi.service.DeliveryNoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/bons-sortie")
@RequiredArgsConstructor
public class DeliveryNoteController {

    private final DeliveryNoteService deliveryNoteService;


    @PostMapping
    public ResponseEntity<DeliveryNoteResponseDTO> createDeliveryNote(
            @Valid @RequestBody DeliveryNoteRequestDTO requestDTO) {
        DeliveryNoteResponseDTO created = deliveryNoteService.createDeliveryNote(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }


    @PutMapping("/{id}")
    public ResponseEntity<DeliveryNoteResponseDTO> updateDeliveryNote(
            @PathVariable Long id,
            @Valid @RequestBody DeliveryNoteRequestDTO requestDTO) {
        DeliveryNoteResponseDTO updated = deliveryNoteService.updateDeliveryNote(id, requestDTO);
        return ResponseEntity.ok(updated);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteDeliveryNote(@PathVariable Long id) {
        deliveryNoteService.deleteDeliveryNote(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Delivery note deleted successfully");
        response.put("id", id.toString());
        return ResponseEntity.ok(response);
    }


    @GetMapping
    public ResponseEntity<List<DeliveryNoteResponseDTO>> getAllDeliveryNotes() {
        List<DeliveryNoteResponseDTO> notes = deliveryNoteService.getAllDeliveryNotes();
        return ResponseEntity.ok(notes);
    }


    @GetMapping("/{id}")
    public ResponseEntity<DeliveryNoteResponseDTO> getDeliveryNoteById(@PathVariable Long id) {
        DeliveryNoteResponseDTO note = deliveryNoteService.getDeliveryNoteById(id);
        return ResponseEntity.ok(note);
    }


    @GetMapping("/atelier/{atelier}")
    public ResponseEntity<List<DeliveryNoteResponseDTO>> getDeliveryNotesByWorkshop(
            @PathVariable String atelier) {
        List<DeliveryNoteResponseDTO> notes = deliveryNoteService.getDeliveryNotesByWorkshop(atelier);
        return ResponseEntity.ok(notes);
    }


    @PutMapping("/{id}/valider")
    public ResponseEntity<DeliveryNoteResponseDTO> validateDeliveryNote(@PathVariable Long id) {
        DeliveryNoteResponseDTO validated = deliveryNoteService.validateDeliveryNote(id);
        return ResponseEntity.ok(validated);
    }


    @PutMapping("/{id}/annuler")
    public ResponseEntity<DeliveryNoteResponseDTO> cancelDeliveryNote(@PathVariable Long id) {
        DeliveryNoteResponseDTO cancelled = deliveryNoteService.cancelDeliveryNote(id);
        return ResponseEntity.ok(cancelled);
    }
}