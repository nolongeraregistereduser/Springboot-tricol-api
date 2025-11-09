package com.tricol.springboottricolapi.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryNoteLineResponseDTO {

    private Long id;

    private Long productId;
    private String productReference;
    private String productName;

    private BigDecimal quantity;
}