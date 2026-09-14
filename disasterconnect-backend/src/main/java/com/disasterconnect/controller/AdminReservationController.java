package com.disasterconnect.controller;

import com.disasterconnect.dto.ReservationResponseDTO;
import com.disasterconnect.entity.Reservation;
import com.disasterconnect.repository.ReservationRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/reservations")
public class AdminReservationController {

    private final ReservationRepository reservationRepository;

    public AdminReservationController(
            ReservationRepository reservationRepository) {

        this.reservationRepository = reservationRepository;
    }

    @GetMapping
    public List<ReservationResponseDTO> getAllReservations() {

        return reservationRepository
                .findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ReservationResponseDTO getReservationById(
            @PathVariable Long id) {

        Reservation reservation =
                reservationRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Reservation not found with id: " + id
                                )
                        );

        return convertToResponseDTO(reservation);
    }

    private ReservationResponseDTO convertToResponseDTO(
            Reservation reservation) {

        return new ReservationResponseDTO(
                reservation.getId(),
                reservation.getRequest().getId(),
                reservation.getResource().getId(),
                reservation.getQuantity(),
                reservation.getStatus(),
                reservation.getCreatedAt()
        );
    }
}