package com.tricol.springboottricolapi.repository;

import com.tricol.springboottricolapi.entity.SupplierOrder;
import com.tricol.springboottricolapi.entity.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierOrderRepository extends JpaRepository<SupplierOrder, Long> {

    Optional<SupplierOrder> findByOrderNumber(String orderNumber);

    boolean existsByOrderNumber(String orderNumber);

    @Query("SELECT o FROM SupplierOrder o LEFT JOIN FETCH o.supplier WHERE o.supplier.id = :supplierId")
    List<SupplierOrder> findBySupplierId(@Param("supplierId") Long supplierId);

    List<SupplierOrder> findByStatus(OrderStatus status);

    @Query("SELECT o FROM SupplierOrder o WHERE o.orderDate BETWEEN :startDate AND :endDate")
    List<SupplierOrder> findByOrderDateBetween(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT o FROM SupplierOrder o WHERE o.supplier.id = :supplierId AND o.status = :status")
    List<SupplierOrder> findBySupplierIdAndStatus(
            @Param("supplierId") Long supplierId,
            @Param("status") OrderStatus status
    );
}

