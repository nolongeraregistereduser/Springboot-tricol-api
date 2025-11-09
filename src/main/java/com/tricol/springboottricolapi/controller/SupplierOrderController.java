package com.tricol.springboottricolapi.controller;

import com.tricol.springboottricolapi.dto.Request.SupplierOrderRequestDTO;
import com.tricol.springboottricolapi.dto.Response.SupplierOrderResponseDTO;
import com.tricol.springboottricolapi.entity.enums.OrderStatus;
import com.tricol.springboottricolapi.service.SupplierOrderService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/commandes")
public class SupplierOrderController {

    private final SupplierOrderService orderService;

    public SupplierOrderController(SupplierOrderService orderService) {
        this.orderService = orderService;
    }


    @GetMapping
    public ResponseEntity<List<SupplierOrderResponseDTO>> getAllOrders(
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<SupplierOrderResponseDTO> orders;

        if (status != null) {
            orders = orderService.getOrdersByStatus(status);
        } else if (startDate != null && endDate != null) {
            orders = orderService.getOrdersByDateRange(startDate, endDate);
        } else {
            orders = orderService.getAllOrders();
        }

        return ResponseEntity.ok(orders);
    }


    @GetMapping("/{id}")
    public ResponseEntity<SupplierOrderResponseDTO> getOrderById(@PathVariable Long id) {
        SupplierOrderResponseDTO order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }


    @GetMapping("/fournisseur/{id}")
    public ResponseEntity<List<SupplierOrderResponseDTO>> getOrdersBySupplierId(@PathVariable Long id) {
        List<SupplierOrderResponseDTO> orders = orderService.getOrdersBySupplierId(id);
        return ResponseEntity.ok(orders);
    }


    @PostMapping
    public ResponseEntity<SupplierOrderResponseDTO> createOrder(@Valid @RequestBody SupplierOrderRequestDTO requestDTO) {
        SupplierOrderResponseDTO createdOrder = orderService.createOrder(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }


    @PutMapping("/{id}")
    public ResponseEntity<SupplierOrderResponseDTO> updateOrder(
            @PathVariable Long id,
            @Valid @RequestBody SupplierOrderRequestDTO requestDTO) {
        SupplierOrderResponseDTO updatedOrder = orderService.updateOrder(id, requestDTO);
        return ResponseEntity.ok(updatedOrder);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Order deleted successfully");
        response.put("orderId", id.toString());
        return ResponseEntity.ok(response);
    }


    @PutMapping("/{id}/valider")
    public ResponseEntity<SupplierOrderResponseDTO> validateOrder(@PathVariable Long id) {
        SupplierOrderResponseDTO validatedOrder = orderService.validateOrder(id);
        return ResponseEntity.ok(validatedOrder);
    }


    @PutMapping("/{id}/annuler")
    public ResponseEntity<SupplierOrderResponseDTO> cancelOrder(@PathVariable Long id) {
        SupplierOrderResponseDTO cancelledOrder = orderService.cancelOrder(id);
        return ResponseEntity.ok(cancelledOrder);
    }


    @PutMapping("/{id}/reception")
    public ResponseEntity<SupplierOrderResponseDTO> receiveOrder(@PathVariable Long id) {
        SupplierOrderResponseDTO receivedOrder = orderService.receiveOrder(id);
        return ResponseEntity.ok(receivedOrder);
    }
}

