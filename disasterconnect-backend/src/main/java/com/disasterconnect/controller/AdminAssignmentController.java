package com.disasterconnect.controller;

import com.disasterconnect.dto.AssignmentResponseDTO;
import com.disasterconnect.entity.Assignment;
import com.disasterconnect.repository.AssignmentRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/assignments")
public class AdminAssignmentController {

    private final AssignmentRepository assignmentRepository;

    public AdminAssignmentController(
            AssignmentRepository assignmentRepository) {

        this.assignmentRepository = assignmentRepository;
    }

    @GetMapping
    public List<AssignmentResponseDTO> getAllAssignments() {

        return assignmentRepository
                .findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public AssignmentResponseDTO getAssignmentById(
            @PathVariable Long id) {

        Assignment assignment =
                assignmentRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Assignment not found with id: " + id
                                )
                        );

        return convertToResponseDTO(assignment);
    }

    private AssignmentResponseDTO convertToResponseDTO(
            Assignment assignment) {

        return new AssignmentResponseDTO(
                assignment.getId(),
                assignment.getRequest().getId(),
                assignment.getVolunteer().getId(),
                assignment.getStatus(),
                assignment.getAssignedAt(),
                assignment.getNotes()
        );
    }
}