package com.tricol.springboottricolapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "supplier_orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", foreignKey = @ForeignKey(name = "FK_ORDER_SUPPLIER"))
    private Supplier supplier;

    @NotNull(message = "Order date is mandatory")
    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, columnDefinition = "VARCHAR(20) DEFAULT 'PENDING'")
    private OrderStatus status = OrderStatus.PENDING;

    @PositiveOrZero(message = "Total amount cannot be negative")
    @Column(name = "total_amount", precision = 14, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(name = "reception_date")
    private LocalDate receptionDate;

    @Column(name = "comments", columnDefinition = "TEXT")
    private String comments;

    @OneToMany(mappedBy = "supplierOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SupplierOrderLine> orderLines = new ArrayList<>();

    @OneToMany(mappedBy = "supplierOrder", cascade = CascadeType.ALL)
    private List<StockBatch> stockBatches = new ArrayList<>();

    // Helper methods
    public void addOrderLine(SupplierOrderLine orderLine) {
        orderLines.add(orderLine);
        orderLine.setSupplierOrder(this);
    }

    public void removeOrderLine(SupplierOrderLine orderLine) {
        orderLines.remove(orderLine);
        orderLine.setSupplierOrder(null);
    }

    // Enum for order status
    public enum OrderStatus {
        PENDING, APPROVED, DELIVERED, CANCELLED
    }
}

