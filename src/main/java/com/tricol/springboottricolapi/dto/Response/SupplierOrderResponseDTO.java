package com.tricol.springboottricolapi.dto.Response;

import com.tricol.springboottricolapi.entity.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierOrderResponseDTO {
    private Long id;
    private String orderNumber;
    private LocalDate orderDate;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private LocalDate receptionDate;
    private String comments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Supplier info
    private Long supplierId;
    private String supplierName;

    // Order lines
    private List<SupplierOrderLineResponseDTO> orderLines;
}

