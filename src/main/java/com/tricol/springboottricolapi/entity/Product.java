package com.tricol.springboottricolapi.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Reference is mandatory")
    @Column(unique = true, nullable = false, length = 50)
    private String reference;

    @NotBlank(message = "Name is mandatory")
    @Column(nullable = false, length = 150)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "Unit price is mandatory")
    @Positive(message = "Unit price must be positive")
    @Column(name = "unit_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;

    @NotBlank(message = "Category is mandatory")
    @Column(name = "category", nullable = false, length = 100)
    private String category;

    @PositiveOrZero(message = "Current stock cannot be negative")
    @Column(name = "current_stock", precision = 12, scale = 3)
    @Builder.Default
    private BigDecimal currentStock = BigDecimal.ZERO;

    @PositiveOrZero(message = "Reorder point cannot be negative")
    @Column(name = "reorder_point", precision = 12, scale = 3)
    @Builder.Default
    private BigDecimal reorderPoint = BigDecimal.ZERO;

    @Column(name = "unit_of_measure", length = 50)
    private String unitOfMeasure;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relationships with other entities
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @Builder.Default
    private List<SupplierOrderLine> supplierOrderLines = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @Builder.Default
    private List<StockBatch> stockBatches = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @Builder.Default
    private List<StockMovement> stockMovements = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @Builder.Default
    private List<DeliveryNoteLine> deliveryNoteLines = new ArrayList<>();

    // Business logic method
    public boolean isBelowReorderPoint() {
        return currentStock.compareTo(reorderPoint) <= 0;
    }
}
