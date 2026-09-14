package com.disasterconnect.controller;

import com.disasterconnect.dto.AssignmentRequestDTO;
import com.disasterconnect.dto.AssignmentResponseDTO;
import com.disasterconnect.entity.User;
import com.disasterconnect.enums.AssignmentStatus;
import com.disasterconnect.repository.UserRepository;
import com.disasterconnect.service.AssignmentService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;
    private final UserRepository userRepository;

    public AssignmentController(
            AssignmentService assignmentService,
            UserRepository userRepository) {

        this.assignmentService = assignmentService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public AssignmentResponseDTO createAssignment(
            @Valid @RequestBody AssignmentRequestDTO requestDTO) {

        return assignmentService.createAssignment(
                requestDTO
        );
    }

    @GetMapping("/my")
    public java.util.List<AssignmentResponseDTO> getMyAssignments(
            Authentication authentication) {

        User volunteer = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        return assignmentService.getAssignmentsForVolunteer(volunteer);
    }

    @GetMapping("/{id}")
    public AssignmentResponseDTO getMyAssignmentById(
            @PathVariable Long id,
            Authentication authentication) {

        User volunteer = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        return assignmentService.getAssignmentForVolunteer(id, volunteer);
    }

    @PutMapping("/{id}/status")
    public AssignmentResponseDTO updateAssignmentStatus(
            @PathVariable Long id,
            @RequestParam AssignmentStatus status,
            Authentication authentication) {

        User volunteer = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Authenticated user not found"
                        )
                );

        return assignmentService.updateAssignmentStatus(
                id,
                status,
                volunteer
        );
    }
}