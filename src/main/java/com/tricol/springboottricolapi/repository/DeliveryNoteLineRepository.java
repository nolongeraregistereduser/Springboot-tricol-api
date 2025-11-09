package com.tricol.springboottricolapi.repository;

import com.tricol.springboottricolapi.entity.DeliveryNoteLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryNoteLineRepository extends JpaRepository<DeliveryNoteLine, Long> {
}