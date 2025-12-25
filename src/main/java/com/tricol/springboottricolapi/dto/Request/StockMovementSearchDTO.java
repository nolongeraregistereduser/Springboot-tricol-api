package com.tricol.springboottricolapi.dto.Request;

import com.tricol.springboottricolapi.entity.enums.MovementType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * DTO for searching stock movements with multiple criteria
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockMovementSearchDTO {

    /**
     * Filter by product ID
     */
    private Long produitId;

    /**
     * Filter by product reference
     */
    private String reference;

    /**
     * Filter by movement type (ENTREE or SORTIE)
     */
    private MovementType type;

    /**
     * Filter by start date (inclusive)
     */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateDebut;

    /**
     * Filter by end date (inclusive)
     */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateFin;

    /**
     * Filter by batch number
     */
    private String numeroLot;
}

