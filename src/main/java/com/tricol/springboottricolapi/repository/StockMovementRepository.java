package com.tricol.springboottricolapi.repository;

import com.tricol.springboottricolapi.entity.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {

    // Find movements by product
    List<StockMovement> findByProductIdOrderByMovementDateDesc(Long productId);

    // Find all movements ordered by date
    List<StockMovement> findAllByOrderByMovementDateDesc();
}

