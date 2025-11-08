package com.tricol.springboottricolapi.dto;


import com.tricol.springboottricolapi.dto.Response.StockBatchResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductStockDetailDTO {

    private Long productId;
    private String productName;
    private Double totalQuantity;
    private BigDecimal totalValue;
    private List<StockBatchResponseDTO> batches;
}
