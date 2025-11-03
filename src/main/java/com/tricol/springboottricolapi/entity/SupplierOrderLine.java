package com.tricol.springboottricolapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "supplier_order_lines")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierOrderLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private SupplierOrder supplierOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;

    @NotNull(message = "Quantity is mandatory")
    @Positive(message = "Quantity must be positive")
    @Column(name = "quantity", nullable = false, precision = 12, scale = 3)
    private BigDecimal quantity;

    @NotNull(message = "Unit purchase price is mandatory")
    @Positive(message = "Unit purchase price must be positive")
    @Column(name = "unit_purchase_price", nullable = false, precision = 12, scale = 3)
    private BigDecimal unitPurchasePrice;

    // This is a generated column in the database (line_total = quantity * unit_purchase_price)
    @Column(name = "line_total", precision = 14, scale = 2, insertable = false, updatable = false)
    private BigDecimal lineTotal;

    // Helper method for business logic
    public BigDecimal getLineTotal() {
        if (lineTotal != null) {
            return lineTotal;
        }
        if (quantity != null && unitPurchasePrice != null) {
            return quantity.multiply(unitPurchasePrice);
        }
        return BigDecimal.ZERO;
    }
}
