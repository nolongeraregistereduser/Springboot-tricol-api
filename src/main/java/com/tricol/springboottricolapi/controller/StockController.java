package com.tricol.springboottricolapi.controller;

import com.tricol.springboottricolapi.dto.ProductStockDetailDTO;
import com.tricol.springboottricolapi.dto.Response.ProductStockDTO;
import com.tricol.springboottricolapi.dto.Response.StockMovementResponseDTO;
import com.tricol.springboottricolapi.dto.StockAlertDTO;
import com.tricol.springboottricolapi.dto.StockValuationDTO;
import com.tricol.springboottricolapi.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<StockMovementResponseDTO>> getAllMovements() {
        List<StockMovementResponseDTO> movements = stockService.getAllMovements();
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

