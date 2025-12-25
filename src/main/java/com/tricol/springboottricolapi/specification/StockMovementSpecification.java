package com.tricol.springboottricolapi.specification;

import com.tricol.springboottricolapi.entity.StockMovement;
import com.tricol.springboottricolapi.entity.enums.MovementType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class StockMovementSpecification {

    public static Specification<StockMovement> withFilters(
            LocalDate dateDebut,
            LocalDate dateFin,
            Long produitId,
            String reference,
            MovementType type,
            String numeroLot) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filter by date range
            if (dateDebut != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                    root.get("movementDate"), dateDebut.atStartOfDay()));
            }
            if (dateFin != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                    root.get("movementDate"), dateFin.atTime(23, 59, 59)));
            }

            // Filter by product ID
            if (produitId != null) {
                predicates.add(criteriaBuilder.equal(
                    root.get("product").get("id"), produitId));
            }

            // Filter by product reference
            if (reference != null) {
                predicates.add(criteriaBuilder.equal(
                    root.get("product").get("reference"), reference));
            }

            // Filter by movement type
            if (type != null) {
                predicates.add(criteriaBuilder.equal(
                    root.get("movementType"), type));
            }

            // Filter by batch number
            if (numeroLot != null) {
                predicates.add(criteriaBuilder.equal(
                    root.get("batch").get("batchNumber"), numeroLot));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}