package com.tricol.springboottricolapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "delivery_notes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Note number is mandatory")
    @Column(name = "note_number", unique = true, nullable = false, length = 50)
    private String noteNumber;

    @NotNull(message = "Delivery date is mandatory")
    @Column(name = "delivery_date", nullable = false)
    private LocalDate deliveryDate;

    @NotBlank(message = "Receiving department is mandatory")
    @Column(name = "receiving_department", nullable = false, length = 100)
    private String receivingDepartment;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_reason", length = 20, columnDefinition = "VARCHAR(20) DEFAULT 'PRODUCTION'")
    private DeliveryReason deliveryReason = DeliveryReason.PRODUCTION;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, columnDefinition = "VARCHAR(20) DEFAULT 'DRAFT'")
    private DeliveryStatus status = DeliveryStatus.DRAFT;

    @Column(name = "comments", columnDefinition = "TEXT")
    private String comments;

    @OneToMany(mappedBy = "deliveryNote", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeliveryNoteLine> deliveryNoteLines = new ArrayList<>();

    // Helper methods
    public void addDeliveryNoteLine(DeliveryNoteLine line) {
        deliveryNoteLines.add(line);
        line.setDeliveryNote(this);
    }

    public void removeDeliveryNoteLine(DeliveryNoteLine line) {
        deliveryNoteLines.remove(line);
        line.setDeliveryNote(null);
    }

    // Enums
    public enum DeliveryReason {
        PRODUCTION, MAINTENANCE, OTHER
    }

    public enum DeliveryStatus {
        DRAFT, APPROVED, CANCELLED
    }
}

