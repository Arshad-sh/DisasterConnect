package com.disasterconnect.controller;

import com.disasterconnect.dto.RequestHistoryResponseDTO;
import com.disasterconnect.entity.User;
import com.disasterconnect.enums.RequestStatus;
import com.disasterconnect.repository.UserRepository;
import com.disasterconnect.service.RequestHistoryService;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
public class RequestHistoryController {

    private final RequestHistoryService requestHistoryService;
    private final UserRepository userRepository;

    public RequestHistoryController(
            RequestHistoryService requestHistoryService,
            UserRepository userRepository) {

        this.requestHistoryService = requestHistoryService;
        this.userRepository = userRepository;
    }

    @PostMapping("/{requestId}/history")
    public RequestHistoryResponseDTO createHistory(
            @PathVariable Long requestId,
            @RequestParam(required = false) RequestStatus oldStatus,
            @RequestParam RequestStatus newStatus,
            Authentication authentication) {

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Authenticated user not found"
                        )
                );

        return requestHistoryService.createHistory(
                requestId,
                oldStatus,
                newStatus,
                user
        );
    }

    @GetMapping("/{requestId}/history")
    public List<RequestHistoryResponseDTO> getHistory(
            @PathVariable Long requestId) {

        return requestHistoryService.getHistory(requestId);
    }
}