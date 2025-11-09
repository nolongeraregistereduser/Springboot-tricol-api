package com.tricol.springboottricolapi.dto.Request;

import com.tricol.springboottricolapi.entity.enums.ExitReason;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryNoteRequestDTO {

    @NotBlank(message = "Note number is required")
    private String noteNumber;

    @NotNull(message = "Exit date is required")
    private LocalDate exitDate;

    @NotBlank(message = "Workshop is required")
    private String workshop;

    @NotNull(message = "Exit reason is required")
    private ExitReason exitReason;

    private String comments;

    @NotEmpty(message = "At least one delivery line is required")
    @Valid
    private List<DeliveryNoteLineRequestDTO> deliveryNoteLines;
}