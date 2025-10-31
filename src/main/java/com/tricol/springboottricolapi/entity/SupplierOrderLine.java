package com.tricol.springboottricolapi.entity;

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
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "FK_ORDERLINE_ORDER"))
    private SupplierOrder supplierOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "FK_ORDERLINE_PRODUCT"))
    private Product product;

    @NotNull(message = "Quantity is mandatory")
    @Positive(message = "Quantity must be positive")
    @Column(name = "quantity", nullable = false, precision = 12, scale = 3)
    private BigDecimal quantity;

    @NotNull(message = "Unit purchase price is mandatory")
    @Positive(message = "Unit purchase price must be positive")
    @Column(name = "unit_purchase_price", nullable = false, precision = 12, scale = 3)
    private BigDecimal unitPurchasePrice;

    @Column(name = "subtotal", precision = 14, scale = 2)
    private BigDecimal subtotal;

    @PrePersist
    @PreUpdate
    protected void calculateSubtotal() {
        if (quantity != null && unitPurchasePrice != null) {
            subtotal = quantity.multiply(unitPurchasePrice);
        }
    }

    // Helper method for business logic
    public BigDecimal getLineTotal() {
        if (subtotal != null) {
            return subtotal;
        }
        if (quantity != null && unitPurchasePrice != null) {
            return quantity.multiply(unitPurchasePrice);
        }
        return BigDecimal.ZERO;
    }
}
