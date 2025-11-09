package com.tricol.springboottricolapi.repository;

import com.tricol.springboottricolapi.entity.StockBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockBatchRepository extends JpaRepository<StockBatch, Long> {

    // Find batches with remaining quantity for a product, ordered by entry date (FIFO)
    @Query("SELECT sb FROM StockBatch sb WHERE sb.product.id = :productId " +
           "AND sb.remainingQuantity > 0 ORDER BY sb.entryDate ASC, sb.id ASC")
    List<StockBatch> findAvailableBatchesByProductIdOrderedByFifo(@Param("productId") Long productId);

    // Find all batches for a product
    List<StockBatch> findByProductIdOrderByEntryDateDesc(Long productId);

    // Check if batch number exists
    boolean existsByBatchNumber(String batchNumber);
}

