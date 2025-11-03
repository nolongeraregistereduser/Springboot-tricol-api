package com.tricol.springboottricolapi.dto.Request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierOrderRequestDTO {

    @NotNull(message = "Supplier ID is required")
    private Long supplierId;

    @NotNull(message = "Order date is required")
    private LocalDate orderDate;

    private String comments;

    @NotEmpty(message = "Order must have at least one product line")
    @Valid
    private List<SupplierOrderLineRequestDTO> orderLines;
}

