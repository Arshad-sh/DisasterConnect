package com.disasterconnect.repository;

import com.disasterconnect.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByResource_Ngo_IdOrderByCreatedAtDesc(Long ngoId);
}
