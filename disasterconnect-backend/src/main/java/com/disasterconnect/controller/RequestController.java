package com.disasterconnect.controller;

import com.disasterconnect.dto.RequestRequestDTO;
import com.disasterconnect.dto.RequestResponseDTO;
import com.disasterconnect.dto.StatusUpdateRequestDTO;
import com.disasterconnect.entity.User;
import com.disasterconnect.enums.RequestStatus;
import com.disasterconnect.enums.Urgency;
import com.disasterconnect.repository.UserRepository;
import com.disasterconnect.service.RequestService;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/requests")
public class RequestController {

    private final RequestService requestService;
    private final UserRepository userRepository;

    public RequestController(
            RequestService requestService,
            UserRepository userRepository) {

        this.requestService = requestService;
        this.userRepository = userRepository;
    }

    // =========================================================
    // CREATE REQUEST
    // =========================================================

    @PostMapping
    public RequestResponseDTO createRequest(
            @Valid @RequestBody RequestRequestDTO requestDTO,
            Authentication authentication) {

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            throw new RuntimeException(
                    "Authentication is required"
            );
        }

        User user = userRepository.findByEmail(
                        authentication.getName()
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Authenticated user not found"
                        )
                );

        return requestService.createRequest(
                requestDTO,
                user
        );
    }

    // =========================================================
    // GET ALL REQUESTS
    // Search + Filter + Pagination
    // =========================================================

    @GetMapping
    public Page<RequestResponseDTO> getAllRequests(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) RequestStatus status,
            @RequestParam(required = false) Urgency urgency,

            @PageableDefault(
                    size = 10,
                    sort = "id",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable) {

        return requestService.getAllRequests(
                search,
                status,
                urgency,
                pageable
        );
    }

    // =========================================================
    // GET MY REQUESTS
    // Day 40.3
    // =========================================================

    @GetMapping("/my")
    public Page<RequestResponseDTO> getMyRequests(
            Authentication authentication,

            @PageableDefault(
                    size = 10,
                    sort = "id",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable) {

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            throw new RuntimeException(
                    "Authentication is required"
            );
        }

        User user = userRepository.findByEmail(
                        authentication.getName()
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Authenticated user not found"
                        )
                );

        return requestService.getMyRequests(
                user,
                pageable
        );
    }

    // =========================================================
    // GET REQUEST BY ID
    // =========================================================

    @GetMapping("/{id}")
    public RequestResponseDTO getRequestById(
            @PathVariable Long id) {

        return requestService.getRequestById(id);
    }

    // =========================================================
    // UPDATE REQUEST
    // =========================================================

    @PutMapping("/{id}")
    public RequestResponseDTO updateRequest(
            @PathVariable Long id,
            @Valid @RequestBody RequestRequestDTO requestDTO,
            Authentication authentication) {

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            throw new RuntimeException(
                    "Authentication is required"
            );
        }

        User user = userRepository.findByEmail(
                        authentication.getName()
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Authenticated user not found"
                        )
                );

        return requestService.updateRequest(
                id,
                requestDTO,
                user
        );
    }

    // =========================================================
    // DELETE REQUEST
    // =========================================================

    @DeleteMapping("/{id}")
    public void deleteRequest(
            @PathVariable Long id,
            Authentication authentication) {

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            throw new RuntimeException(
                    "Authentication is required"
            );
        }

        User user = userRepository.findByEmail(
                        authentication.getName()
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Authenticated user not found"
                        )
                );

        requestService.deleteRequest(
                id,
                user
        );
    }

    // =========================================================
    // UPDATE REQUEST STATUS
    // =========================================================

    @PutMapping("/{id}/status")
    public RequestResponseDTO updateRequestStatus(
            @PathVariable Long id,
            @Valid @RequestBody StatusUpdateRequestDTO statusUpdateRequestDTO,
            Authentication authentication) {

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            throw new RuntimeException(
                    "Authentication is required"
            );
        }

        User user = userRepository.findByEmail(
                        authentication.getName()
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Authenticated user not found"
                        )
                );

        return requestService.updateRequestStatus(
                id,
                statusUpdateRequestDTO.getStatus(),
                user
        );
    }
}