package com.disasterconnect.controller;

import com.disasterconnect.dto.RequestResponseDTO;
import com.disasterconnect.entity.User;
import com.disasterconnect.enums.RequestStatus;
import com.disasterconnect.enums.Urgency;
import com.disasterconnect.repository.UserRepository;
import com.disasterconnect.service.RequestService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ngo/requests")
public class NGORequestController {

    private final RequestService requestService;
    private final UserRepository userRepository;

    public NGORequestController(
            RequestService requestService,
            UserRepository userRepository) {

        this.requestService = requestService;
        this.userRepository = userRepository;
    }

    // 25.4 - Get all Citizen Help Requests
    // Day 37.7 - Search, filtering, pagination and sorting
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

    // 25.5 - Get one Citizen Help Request
    @GetMapping("/{id}")
    public RequestResponseDTO getRequestById(
            @PathVariable Long id) {

        return requestService.getRequestById(id);
    }

    // 25.6 - NGO updates Request status
    @PutMapping("/{id}/status")
    public RequestResponseDTO updateRequestStatus(
            @PathVariable Long id,
            @RequestParam RequestStatus status,
            Authentication authentication) {

        User ngo = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Authenticated user not found"
                        )
                );

        return requestService.updateRequestStatusByNgo(
                id,
                status,
                ngo
        );
    }
}