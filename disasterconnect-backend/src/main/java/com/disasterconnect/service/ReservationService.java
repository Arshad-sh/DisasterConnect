package com.disasterconnect.service;

import com.disasterconnect.dto.ReservationRequestDTO;
import com.disasterconnect.dto.ReservationResponseDTO;
import com.disasterconnect.entity.Request;
import com.disasterconnect.entity.Reservation;
import com.disasterconnect.entity.Resource;
import com.disasterconnect.entity.User;
import com.disasterconnect.enums.ReservationStatus;
import com.disasterconnect.exception.BusinessRuleException;
import com.disasterconnect.exception.ResourceNotFoundException;
import com.disasterconnect.repository.RequestRepository;
import com.disasterconnect.repository.ReservationRepository;
import com.disasterconnect.repository.ResourceRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RequestRepository requestRepository;
    private final ResourceRepository resourceRepository;
    private final AuditLogService auditLogService;

    public ReservationService(
            ReservationRepository reservationRepository,
            RequestRepository requestRepository,
            ResourceRepository resourceRepository,
            AuditLogService auditLogService) {

        this.reservationRepository = reservationRepository;
        this.requestRepository = requestRepository;
        this.resourceRepository = resourceRepository;
        this.auditLogService = auditLogService;
    }

    // =========================================================
    // CREATE RESERVATION
    // =========================================================

    @Transactional
    public ReservationResponseDTO createReservation(
            ReservationRequestDTO requestDTO,
            User authenticatedNgo) {

        Request request = requestRepository
                .findById(requestDTO.getRequestId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Request not found with id: "
                                        + requestDTO.getRequestId()
                        )
                );

        // Fetch resource with database write lock
        Resource resource = resourceRepository
                .findByIdForUpdate(requestDTO.getResourceId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Resource not found with id: "
                                        + requestDTO.getResourceId()
                        )
                );

        // =====================================================
        // NGO OWNERSHIP VALIDATION
        // =====================================================

        if (!resource.getNgo().getId()
                .equals(authenticatedNgo.getId())) {

            throw new BusinessRuleException(
                    "You are not allowed to reserve this resource"
            );
        }

        // =====================================================
        // QUANTITY VALIDATION
        // =====================================================

        if (requestDTO.getQuantity() == null
                || requestDTO.getQuantity() <= 0) {

            throw new BusinessRuleException(
                    "Reservation quantity must be greater than 0"
            );
        }

        // =====================================================
        // AVAILABILITY VALIDATION
        // =====================================================

        if (!resource.isAvailable()) {

            throw new BusinessRuleException(
                    "Resource is not available"
            );
        }

        // =====================================================
        // SUFFICIENT QUANTITY VALIDATION
        // =====================================================

        if (requestDTO.getQuantity()
                > resource.getQuantity()) {

            throw new BusinessRuleException(
                    "Insufficient resource quantity"
            );
        }

        // =====================================================
        // CREATE RESERVATION
        // =====================================================

        Reservation reservation = new Reservation();

        reservation.setRequest(request);
        reservation.setResource(resource);
        reservation.setQuantity(requestDTO.getQuantity());

        // Initial reservation status
        reservation.setStatus(
                ReservationStatus.PENDING
        );

        reservation.setCreatedAt(
                LocalDateTime.now()
        );

        // =====================================================
        // REDUCE RESOURCE QUANTITY
        // =====================================================

        int remainingQuantity =
                resource.getQuantity()
                        - requestDTO.getQuantity();

        resource.setQuantity(remainingQuantity);

        // Availability is controlled by the server
        resource.setAvailable(
                remainingQuantity > 0
        );

        resourceRepository.save(resource);

        // =====================================================
        // SAVE RESERVATION
        // =====================================================

        Reservation savedReservation =
                reservationRepository.save(reservation);

        // =====================================================
        // AUDIT LOG
        // =====================================================

        auditLogService.logAction(
                "RESERVATION_CREATED",
                authenticatedNgo,
                "RESERVATION",
                savedReservation.getId(),
                "Reserved "
                        + savedReservation.getQuantity()
                        + " units of resource "
                        + savedReservation.getResource().getId()
        );

        return convertToResponseDTO(
                savedReservation
        );
    }

    public java.util.List<ReservationResponseDTO> getReservationsForNgo(User ngo) {
        return reservationRepository.findByResource_Ngo_IdOrderByCreatedAtDesc(ngo.getId())
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    // =========================================================
    // VALIDATE RESERVATION STATUS TRANSITION
    // =========================================================

    public boolean isValidStatusTransition(
            ReservationStatus currentStatus,
            ReservationStatus newStatus) {

        if (currentStatus == null
                || newStatus == null) {

            return false;
        }

        return switch (currentStatus) {

            case PENDING ->
                    newStatus == ReservationStatus.CONFIRMED
                            || newStatus == ReservationStatus.CANCELLED;

            case CONFIRMED ->
                    newStatus == ReservationStatus.COMPLETED;

            case CANCELLED ->
                    false;

            case COMPLETED ->
                    false;
        };
    }

    // =========================================================
    // UPDATE RESERVATION STATUS
    // =========================================================

    @Transactional
    public ReservationResponseDTO updateReservationStatus(
            Long id,
            ReservationStatus newStatus,
            User authenticatedNgo) {

        Reservation reservation =
                reservationRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Reservation not found with id: "
                                                + id
                                )
                        );

        // =====================================================
        // NGO OWNERSHIP VALIDATION
        // =====================================================

        if (!reservation.getResource()
                .getNgo()
                .getId()
                .equals(authenticatedNgo.getId())) {

            throw new BusinessRuleException(
                    "You are not allowed to modify this reservation"
            );
        }

        ReservationStatus currentStatus =
                reservation.getStatus();

        // =====================================================
        // VALIDATE STATUS TRANSITION
        // =====================================================

        if (!isValidStatusTransition(
                currentStatus,
                newStatus)) {

            throw new BusinessRuleException(
                    "Invalid reservation status transition from "
                            + currentStatus
                            + " to "
                            + newStatus
            );
        }

        // =====================================================
        // RESTORE RESOURCE WHEN RESERVATION IS CANCELLED
        // =====================================================

        if (newStatus == ReservationStatus.CANCELLED
                && currentStatus == ReservationStatus.PENDING) {

            Resource resource =
                    resourceRepository
                            .findByIdForUpdate(
                                    reservation
                                            .getResource()
                                            .getId()
                            )
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Resource not found with id: "
                                                    + reservation
                                                    .getResource()
                                                    .getId()
                                    )
                            );

            int restoredQuantity =
                    resource.getQuantity()
                            + reservation.getQuantity();

            resource.setQuantity(
                    restoredQuantity
            );

            // Resource becomes available again
            if (restoredQuantity > 0) {
                resource.setAvailable(true);
            }

            resourceRepository.save(resource);
        }

        // =====================================================
        // UPDATE STATUS
        // =====================================================

        reservation.setStatus(newStatus);

        Reservation updatedReservation =
                reservationRepository.save(
                        reservation
                );

        // =====================================================
        // AUDIT LOG
        // =====================================================

        auditLogService.logAction(
                "RESERVATION_STATUS_UPDATED",
                authenticatedNgo,
                "RESERVATION",
                updatedReservation.getId(),
                "Reservation status changed from "
                        + currentStatus
                        + " to "
                        + newStatus
        );

        return convertToResponseDTO(
                updatedReservation
        );
    }

    // =========================================================
    // CONVERT ENTITY → RESPONSE DTO
    // =========================================================

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