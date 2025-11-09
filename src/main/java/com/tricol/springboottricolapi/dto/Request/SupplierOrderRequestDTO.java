package com.tricol.springboottricolapi.dto.Request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierOrderRequestDTO {

    @NotNull(message = "Supplier ID is required")
    private Long supplierId;

    @Valid
    @NotNull(message = "Order lines are required")
    private List<SupplierOrderLineRequestDTO> orderLines;

    private String notes;
}
