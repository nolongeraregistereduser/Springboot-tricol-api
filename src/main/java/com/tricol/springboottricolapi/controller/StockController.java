package com.tricol.springboottricolapi.controller;

import com.tricol.springboottricolapi.dto.ProductStockDetailDTO;
import com.tricol.springboottricolapi.dto.Response.ProductStockDTO;
import com.tricol.springboottricolapi.dto.Response.StockMovementResponseDTO;
import com.tricol.springboottricolapi.dto.StockAlertDTO;
import com.tricol.springboottricolapi.dto.StockValuationDTO;
import com.tricol.springboottricolapi.entity.enums.MovementType;
import com.tricol.springboottricolapi.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @GetMapping
    public ResponseEntity<List<ProductStockDTO>> getGlobalStockOverview() {
        List<ProductStockDTO> globalStock = stockService.getGlobalStockOverview();
        return ResponseEntity.ok(globalStock);
    }

    @GetMapping("/produit/{productId}")
    public ResponseEntity<ProductStockDetailDTO> getProductStockDetail(@PathVariable Long productId) {
        ProductStockDetailDTO stockDetail = stockService.getProductStockDetail(productId);
        return ResponseEntity.ok(stockDetail);
    }

    @GetMapping("/mouvements")
    public ResponseEntity<Page<StockMovementResponseDTO>> searchMovements(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,
            @RequestParam(required = false) Long produitId,
            @RequestParam(required = false) String reference,
            @RequestParam(required = false) MovementType type,
            @RequestParam(required = false) String numeroLot,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("movementDate").descending());
        
        Page<StockMovementResponseDTO> movements = stockService.searchMovements(
                dateDebut, dateFin, produitId, reference, type, numeroLot, pageable);
        
        return ResponseEntity.ok(movements);
    }

    @GetMapping("/mouvements/produit/{productId}")
    public ResponseEntity<List<StockMovementResponseDTO>> getMovementsByProduct(@PathVariable Long productId) {
        List<StockMovementResponseDTO> movements = stockService.getMovementsByProduct(productId);
        return ResponseEntity.ok(movements);
    }

    @GetMapping("/alertes")
    public ResponseEntity<List<StockAlertDTO>> getStockAlerts() {
        List<StockAlertDTO> alerts = stockService.getStockAlerts();
        return ResponseEntity.ok(alerts);
    }

    @GetMapping("/valorisation")
    public ResponseEntity<StockValuationDTO> getStockValuation() {
        StockValuationDTO valuation = stockService.getStockValuation();
        return ResponseEntity.ok(valuation);
    }
}

