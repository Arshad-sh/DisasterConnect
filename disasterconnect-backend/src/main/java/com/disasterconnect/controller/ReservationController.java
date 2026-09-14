package com.disasterconnect.controller;

import com.disasterconnect.dto.ReservationRequestDTO;
import com.disasterconnect.dto.ReservationResponseDTO;
import com.disasterconnect.entity.User;
import com.disasterconnect.enums.ReservationStatus;
import com.disasterconnect.repository.UserRepository;
import com.disasterconnect.service.ReservationService;

import jakarta.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ngo/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final UserRepository userRepository;

    public ReservationController(
            ReservationService reservationService,
            UserRepository userRepository) {

        this.reservationService = reservationService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public java.util.List<ReservationResponseDTO> getMyReservations(
            Authentication authentication) {

        User ngo = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        return reservationService.getReservationsForNgo(ngo);
    }

    @PostMapping
    public ReservationResponseDTO createReservation(
            @Valid @RequestBody ReservationRequestDTO requestDTO,
            Authentication authentication) {

        User ngo = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Authenticated user not found"
                        )
                );

        return reservationService.createReservation(
                requestDTO,
                ngo
        );
    }

    @PutMapping("/{id}/status")
    public ReservationResponseDTO updateReservationStatus(
            @PathVariable Long id,
            @RequestParam ReservationStatus status,
            Authentication authentication) {

        User ngo = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Authenticated user not found"
                        )
                );

        return reservationService.updateReservationStatus(
                id,
                status,
                ngo
        );
    }
}