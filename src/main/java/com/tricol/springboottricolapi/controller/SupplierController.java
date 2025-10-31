package com.tricol.springboottricolapi.controller;

import com.tricol.springboottricolapi.dto.SupplierRequestDTO;
import com.tricol.springboottricolapi.dto.SupplierResponseDTO;
import com.tricol.springboottricolapi.service.SupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/fournisseurs")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    @GetMapping
    public ResponseEntity<Page<SupplierResponseDTO>> getAllSuppliers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") 
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<SupplierResponseDTO> suppliers = supplierService.getAllSuppliers(pageable);
        return ResponseEntity.ok(suppliers);
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<SupplierResponseDTO> getSupplierById(@PathVariable Long id) {
        SupplierResponseDTO supplier = supplierService.getSupplierById(id);
        return ResponseEntity.ok(supplier);
    }

     @PostMapping
    public ResponseEntity<SupplierResponseDTO> createSupplier(
            @Valid @RequestBody SupplierRequestDTO requestDTO) {
        SupplierResponseDTO createdSupplier = supplierService.createSupplier(requestDTO);
        return new ResponseEntity<>(createdSupplier, HttpStatus.CREATED);
    }

    
    @PutMapping("/{id}")
    public ResponseEntity<SupplierResponseDTO> updateSupplier(
            @PathVariable Long id,
            @Valid @RequestBody SupplierRequestDTO requestDTO) {
        SupplierResponseDTO updatedSupplier = supplierService.updateSupplier(id, requestDTO);
        return ResponseEntity.ok(updatedSupplier);
    }

    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.noContent().build();
    }


     @GetMapping("/search")
    public ResponseEntity<Page<SupplierResponseDTO>> searchSuppliers(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<SupplierResponseDTO> suppliers = supplierService.searchSuppliers(keyword, pageable);
        return ResponseEntity.ok(suppliers);
    }

    
    @GetMapping("/by-city")
    public ResponseEntity<Page<SupplierResponseDTO>> getSuppliersByCity(
            @RequestParam String city,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<SupplierResponseDTO> suppliers = supplierService.getSuppliersByCity(city, pageable);
        return ResponseEntity.ok(suppliers);
    }
}
