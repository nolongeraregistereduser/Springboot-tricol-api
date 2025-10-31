package com.tricol.springboottricolapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "stock_batches")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "FK_BATCH_PRODUCT"))
    private Product product;

    @NotBlank(message = "Batch number is mandatory")
    @Column(name = "batch_number", unique = true, nullable = false, length = 100)
    private String batchNumber;

    @NotNull(message = "Entry date is mandatory")
    @Column(name = "entry_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime entryDate = LocalDateTime.now();

    @NotNull(message = "Initial quantity is mandatory")
    @Positive(message = "Initial quantity must be positive")
    @Column(name = "initial_quantity", nullable = false, precision = 12, scale = 3)
    private BigDecimal initialQuantity;

    @NotNull(message = "Remaining quantity is mandatory")
    @PositiveOrZero(message = "Remaining quantity cannot be negative")
    @Column(name = "remaining_quantity", nullable = false, precision = 12, scale = 3)
    private BigDecimal remainingQuantity;

    @NotNull(message = "Unit purchase price is mandatory")
    @Positive(message = "Unit purchase price must be positive")
    @Column(name = "unit_purchase_price", nullable = false, precision = 12, scale = 3)
    private BigDecimal unitPurchasePrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_order_id", foreignKey = @ForeignKey(name = "FK_BATCH_SUPPLIER_ORDER"))
    private SupplierOrder supplierOrder;

    @OneToMany(mappedBy = "batch", cascade = CascadeType.ALL)
    private List<StockMovement> stockMovements = new ArrayList<>();
}

