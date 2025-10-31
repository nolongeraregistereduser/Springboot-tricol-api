package com.tricol.springboottricolapi.repository;

import com.tricol.springboottricolapi.entity.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    Page<Supplier> findByRaisonSocialeContainingIgnoreCase(String raisonSociale, Pageable pageable);
    
    Page<Supplier> findByCityContainingIgnoreCase(String city, Pageable pageable);
    
    Optional<Supplier> findByIce(String ice);
    
    @Query("SELECT s FROM Supplier s WHERE " +
           "LOWER(s.raisonSociale) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.city) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.ice) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.email) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Supplier> searchSuppliers(@Param("keyword") String keyword, Pageable pageable);


}
