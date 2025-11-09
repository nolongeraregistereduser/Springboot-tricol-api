package com.tricol.springboottricolapi.dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDTO {

    @NotBlank(message = "Reference is mandatory")
    private String reference;

    @NotBlank(message = "Name is mandatory")
    private String name;

    private String description;

    @NotNull(message = "Unit price is mandatory")
    @Positive(message = "Unit price must be positive")
    private BigDecimal unitPrice;

    @NotBlank(message = "Category is mandatory")
    private String category;

    @PositiveOrZero(message = "Reorder point cannot be negative")
    private BigDecimal reorderPoint;

    @NotBlank(message = "Unit of measure is mandatory")
    private String unitOfMeasure;
}
