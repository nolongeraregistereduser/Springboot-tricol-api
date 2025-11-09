package com.tricol.springboottricolapi.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductStockDTO {
    private Long productId;
    private String reference;
    private String name;
    private BigDecimal currentStock;
    private BigDecimal reorderPoint;
    private String unitOfMeasure;
    private Boolean isBelowReorderPoint;
}
