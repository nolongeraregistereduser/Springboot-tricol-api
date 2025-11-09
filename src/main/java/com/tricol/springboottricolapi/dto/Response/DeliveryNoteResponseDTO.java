package com.tricol.springboottricolapi.dto.Response;

import com.tricol.springboottricolapi.entity.enums.ExitOrderStatus;
import com.tricol.springboottricolapi.entity.enums.ExitReason;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryNoteResponseDTO {

    private Long id;
    private String noteNumber;
    private LocalDate exitDate;
    private String workshop;
    private ExitReason exitReason;
    private ExitOrderStatus status;
    private String comments;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<DeliveryNoteLineResponseDTO> deliveryNoteLines;
}