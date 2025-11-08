package com.tricol.springboottricolapi.dto.Response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockBatchResponseDTO {

    private Long id;
    private String batchNumber;
    private Long productId;
    private String productName;
    private Double quantityRemaining;
    private LocalDate entryDate;
    private Long supplierOrderId;
    private String supplierOrderNumber;

}
