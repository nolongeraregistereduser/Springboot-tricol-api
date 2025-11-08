package com.tricol.springboottricolapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockValuationDTO {

    private BigDecimal totalStockValue;
    private Integer totalProducts;
    private Integer totalBatches;
}
