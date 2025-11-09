package com.tricol.springboottricolapi.repository;

import com.tricol.springboottricolapi.entity.DeliveryNote;
import com.tricol.springboottricolapi.entity.enums.ExitOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryNoteRepository extends JpaRepository<DeliveryNote, Long> {

    List<DeliveryNote> findByWorkshop(String workshop);

    List<DeliveryNote> findByStatus(ExitOrderStatus status);

    boolean existsByNoteNumber(String noteNumber);
}