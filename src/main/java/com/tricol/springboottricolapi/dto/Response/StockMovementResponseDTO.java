package com.tricol.springboottricolapi.dto.Response;


import com.tricol.springboottricolapi.entity.enums.MovementType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockMovementResponseDTO {

    private Long id;
    private Long productId;
    private String productName;
    private MovementType movementType;
    private Double quantity;
    private BigDecimal unitPrice;
    private String batchNumber;
    private String reference;
    private LocalDateTime movementDate;
    private String notes;

}
