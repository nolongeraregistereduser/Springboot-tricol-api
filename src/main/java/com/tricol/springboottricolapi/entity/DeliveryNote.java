package com.tricol.springboottricolapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tricol.springboottricolapi.entity.enums.ExitOrderStatus;
import com.tricol.springboottricolapi.entity.enums.ExitReason;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @NotNull(message = "Exit date is mandatory")
    @Column(name = "exit_date", nullable = false)
    private LocalDate exitDate;

    @NotBlank(message = "Workshop is mandatory")
    @Column(name = "workshop", nullable = false, length = 100)
    private String workshop;

    @Enumerated(EnumType.STRING)
    @Column(name = "exit_reason", length = 20, nullable = false)
    private ExitReason exitReason = ExitReason.PRODUCTION;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private ExitOrderStatus status = ExitOrderStatus.BROUILLON;

    @Column(name = "comments", columnDefinition = "TEXT")
    private String comments;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "deliveryNote", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
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
}
