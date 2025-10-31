package com.tricol.springboottricolapi.entity;

import com.tricol.springboottricolapi.entity.enums.OrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Column(name = "order_number", unique = true, nullable = false, length = 50)
    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", foreignKey = @ForeignKey(name = "FK_ORDER_SUPPLIER"))
    private Supplier supplier;

    @NotNull(message = "Order date is mandatory")
    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private OrderStatus status = OrderStatus.EN_ATTENTE;

    @PositiveOrZero(message = "Total amount cannot be negative")
    @Column(name = "total_amount", precision = 14, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(name = "received_date")
    private LocalDate receivedDate;

    @Column(name = "comments", columnDefinition = "TEXT")
    private String comments;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "supplierOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SupplierOrderLine> orderLines = new ArrayList<>();

    @OneToMany(mappedBy = "supplierOrder", cascade = CascadeType.ALL)
    private List<StockBatch> stockBatches = new ArrayList<>();

    // Business methods
    public void calculateTotalAmount() {
        totalAmount = orderLines.stream()
                .map(SupplierOrderLine::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void addOrderLine(SupplierOrderLine orderLine) {
        orderLines.add(orderLine);
        orderLine.setSupplierOrder(this);
        calculateTotalAmount();
    }

    public void removeOrderLine(SupplierOrderLine orderLine) {
        orderLines.remove(orderLine);
        orderLine.setSupplierOrder(null);
        calculateTotalAmount();
    }
}
