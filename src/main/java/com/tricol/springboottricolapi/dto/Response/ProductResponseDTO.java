package com.tricol.springboottricolapi.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ProductResponseDTO {

    private String reference;
    private String name;
    private String description;
    private String category;
    private  Double  unitPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
