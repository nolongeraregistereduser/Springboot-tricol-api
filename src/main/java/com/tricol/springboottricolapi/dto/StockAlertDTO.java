package com.tricol.springboottricolapi.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StockAlertDTO {

    private Long productId;
    private String productName;
    private Double currentStock;
    private Double reorderPoint;
    private Double deficit;
}
