package com.tricol.springboottricolapi.repository;

import com.tricol.springboottricolapi.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByReference(String reference);

    @Query("SELECT p FROM Product p WHERE p.currentStock <= p.reorderPoint")
    List<Product> findProductsBelowReorderPoint();

    boolean existsByReference(String reference);
}
