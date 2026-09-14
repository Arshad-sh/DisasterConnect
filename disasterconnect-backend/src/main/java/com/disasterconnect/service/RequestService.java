package com.disasterconnect.service;

import com.disasterconnect.dto.RequestRequestDTO;
import com.disasterconnect.dto.RequestResponseDTO;
import com.disasterconnect.entity.Request;
import com.disasterconnect.entity.User;
import com.disasterconnect.enums.RequestStatus;
import com.disasterconnect.enums.Urgency;
import com.disasterconnect.exception.BusinessRuleException;
import com.disasterconnect.exception.ResourceNotFoundException;
import com.disasterconnect.repository.RequestRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RequestService {

    private final RequestRepository requestRepository;
    private final RequestHistoryService requestHistoryService;

    public RequestService(
            RequestRepository requestRepository,
            RequestHistoryService requestHistoryService) {

        this.requestRepository = requestRepository;
        this.requestHistoryService = requestHistoryService;
    }

    // =========================================================
    // CREATE REQUEST
    // =========================================================

    public RequestResponseDTO createRequest(
            RequestRequestDTO requestDTO,
            User user) {

        Request request = new Request();

        request.setTitle(requestDTO.getTitle());
        request.setDescription(requestDTO.getDescription());
        request.setLocation(requestDTO.getLocation());
        request.setUrgency(requestDTO.getUrgency());

        request.setStatus(RequestStatus.CREATED);
        request.setUser(user);

        Request savedRequest = requestRepository.save(request);

        return convertToResponseDTO(savedRequest);
    }

    // =========================================================
    // GET ALL REQUESTS
    // Search + Filter + Pagination
    // =========================================================

    public Page<RequestResponseDTO> getAllRequests(
            String search,
            RequestStatus status,
            Urgency urgency,
            Pageable pageable) {

        String trimmedSearch =
                search == null ? null : search.trim();

        boolean hasSearch =
                trimmedSearch != null
                        && !trimmedSearch.isEmpty();

        boolean hasStatus =
                status != null;

        boolean hasUrgency =
                urgency != null;

        // Search + filter
        if (hasSearch && (hasStatus || hasUrgency)) {

            return requestRepository.searchAndFilterRequests(
                            trimmedSearch,
                            status,
                            urgency,
                            pageable
                    )
                    .map(this::convertToResponseDTO);
        }

        // Search only
        if (hasSearch) {

            return requestRepository.searchRequests(
                            trimmedSearch,
                            pageable
                    )
                    .map(this::convertToResponseDTO);
        }

        // Filter only
        if (hasStatus || hasUrgency) {

            return requestRepository.filterRequests(
                            status,
                            urgency,
                            pageable
                    )
                    .map(this::convertToResponseDTO);
        }

        // No search or filters
        return requestRepository.findAll(pageable)
                .map(this::convertToResponseDTO);
    }

    // =========================================================
    // GET MY REQUESTS
    // Day 40.3
    // =========================================================

    public Page<RequestResponseDTO> getMyRequests(
            User authenticatedUser,
            Pageable pageable) {

        return requestRepository
                .findByUserId(
                        authenticatedUser.getId(),
                        pageable
                )
                .map(this::convertToResponseDTO);
    }

    // =========================================================
    // GET REQUEST BY ID
    // =========================================================

    public RequestResponseDTO getRequestById(Long id) {

        Request request = requestRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Request not found with id: " + id
                        )
                );

        return convertToResponseDTO(request);
    }

    // =========================================================
    // UPDATE REQUEST
    // =========================================================

    public RequestResponseDTO updateRequest(
            Long id,
            RequestRequestDTO requestDTO,
            User authenticatedUser) {

        Request request = requestRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Request not found with id: " + id
                        )
                );

        // Check whether the authenticated user owns this request
        if (!request.getUser().getId()
                .equals(authenticatedUser.getId())) {

            throw new BusinessRuleException(
                    "You are not allowed to modify this request"
            );
        }

        // Completed requests cannot be modified
        if (request.getStatus() == RequestStatus.COMPLETED) {

            throw new BusinessRuleException(
                    "Completed request cannot be modified"
            );
        }

        request.setTitle(requestDTO.getTitle());
        request.setDescription(requestDTO.getDescription());
        request.setLocation(requestDTO.getLocation());
        request.setUrgency(requestDTO.getUrgency());

        Request updatedRequest =
                requestRepository.save(request);

        return convertToResponseDTO(updatedRequest);
    }

    // =========================================================
    // DELETE REQUEST
    // =========================================================

    public void deleteRequest(
            Long id,
            User authenticatedUser) {

        Request request = requestRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Request not found with id: " + id
                        )
                );

        // Check whether the authenticated user owns this request
        if (!request.getUser().getId()
                .equals(authenticatedUser.getId())) {

            throw new BusinessRuleException(
                    "You are not allowed to delete this request"
            );
        }

        requestRepository.delete(request);
    }

    // =========================================================
    // STATUS TRANSITION VALIDATION
    // =========================================================

    public boolean isValidStatusTransition(
            RequestStatus currentStatus,
            RequestStatus newStatus) {

        if (currentStatus == null || newStatus == null) {
            return false;
        }

        return switch (currentStatus) {

            case CREATED ->
                    newStatus == RequestStatus.VERIFIED;

            case VERIFIED ->
                    newStatus == RequestStatus.MATCHED;

            case MATCHED ->
                    newStatus == RequestStatus.ASSIGNED;

            case ASSIGNED ->
                    newStatus == RequestStatus.IN_PROGRESS;

            case IN_PROGRESS ->
                    newStatus == RequestStatus.COMPLETED;

            case COMPLETED ->
                    false;
        };
    }

    // =========================================================
    // UPDATE REQUEST STATUS
    // Citizen
    // =========================================================

    @Transactional
    public RequestResponseDTO updateRequestStatus(
            Long id,
            RequestStatus newStatus,
            User authenticatedUser) {

        Request request = requestRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Request not found with id: " + id
                        )
                );

        // Check request ownership
        if (!request.getUser().getId()
                .equals(authenticatedUser.getId())) {

            throw new BusinessRuleException(
                    "You are not allowed to modify this request"
            );
        }

        // Store the current status before changing it
        RequestStatus oldStatus = request.getStatus();

        // Validate status transition
        if (!isValidStatusTransition(
                oldStatus,
                newStatus)) {

            throw new BusinessRuleException(
                    "Invalid status transition from "
                            + oldStatus
                            + " to "
                            + newStatus
            );
        }

        // Update request status
        request.setStatus(newStatus);

        Request updatedRequest =
                requestRepository.save(request);

        // Create request history entry
        requestHistoryService.createHistory(
                id,
                oldStatus,
                newStatus,
                authenticatedUser
        );

        return convertToResponseDTO(updatedRequest);
    }

    // =========================================================
    // UPDATE REQUEST STATUS BY NGO
    // =========================================================

    @Transactional
    public RequestResponseDTO updateRequestStatusByNgo(
            Long id,
            RequestStatus newStatus,
            User authenticatedNgo) {

        Request request = requestRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Request not found with id: " + id
                        )
                );

        // Store the current status
        RequestStatus oldStatus = request.getStatus();

        // Validate status transition
        if (!isValidStatusTransition(
                oldStatus,
                newStatus)) {

            throw new BusinessRuleException(
                    "Invalid status transition from "
                            + oldStatus
                            + " to "
                            + newStatus
            );
        }

        // Update request status
        request.setStatus(newStatus);

        Request updatedRequest =
                requestRepository.save(request);

        // Record status change in request history
        requestHistoryService.createHistory(
                id,
                oldStatus,
                newStatus,
                authenticatedNgo
        );

        return convertToResponseDTO(updatedRequest);
    }

    // =========================================================
    // ENTITY → RESPONSE DTO
    // =========================================================

    private RequestResponseDTO convertToResponseDTO(
            Request request) {

        return new RequestResponseDTO(
                request.getId(),
                request.getTitle(),
                request.getDescription(),
                request.getLocation(),
                request.getUrgency(),
                request.getStatus(),
                request.getUser().getId()
        );
    }
}