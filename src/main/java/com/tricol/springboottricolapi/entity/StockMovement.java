package com.tricol.springboottricolapi.entity;

import com.tricol.springboottricolapi.entity.enums.MovementType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_movements")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "FK_MOVEMENT_PRODUCT"))
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id", foreignKey = @ForeignKey(name = "FK_MOVEMENT_BATCH"))
    private StockBatch batch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_note_id", foreignKey = @ForeignKey(name = "FK_MOVEMENT_DELIVERY_NOTE"))
    private DeliveryNote deliveryNote;

    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", length = 10, nullable = false)
    private MovementType movementType;

    @NotNull(message = "Quantity is mandatory")
    @Positive(message = "Quantity must be positive")
    @Column(name = "quantity", nullable = false, precision = 12, scale = 3)
    private BigDecimal quantity;

    @Column(name = "unit_price", precision = 12, scale = 3)
    private BigDecimal unitPrice;

    @NotNull(message = "Movement date is mandatory")
    @Column(name = "movement_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime movementDate = LocalDateTime.now();

    @Column(name = "reference", length = 100)
    private String reference;

    @Column(name = "comments", columnDefinition = "TEXT")
    private String comments;
}
