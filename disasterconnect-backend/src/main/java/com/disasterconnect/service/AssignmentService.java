package com.disasterconnect.service;

import com.disasterconnect.dto.AssignmentRequestDTO;
import com.disasterconnect.dto.AssignmentResponseDTO;
import com.disasterconnect.entity.Assignment;
import com.disasterconnect.entity.Request;
import com.disasterconnect.entity.User;
import com.disasterconnect.entity.VolunteerProfile;
import com.disasterconnect.enums.AssignmentStatus;
import com.disasterconnect.exception.BusinessRuleException;
import com.disasterconnect.exception.ResourceNotFoundException;
import com.disasterconnect.repository.AssignmentRepository;
import com.disasterconnect.repository.RequestRepository;
import com.disasterconnect.repository.UserRepository;
import com.disasterconnect.repository.VolunteerProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final VolunteerProfileRepository volunteerProfileRepository;

    public AssignmentService(
            AssignmentRepository assignmentRepository,
            RequestRepository requestRepository,
            UserRepository userRepository,
            VolunteerProfileRepository volunteerProfileRepository) {

        this.assignmentRepository = assignmentRepository;
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.volunteerProfileRepository = volunteerProfileRepository;
    }

    public AssignmentResponseDTO createAssignment(
            AssignmentRequestDTO requestDTO) {

        // Find request
        Request request = requestRepository
                .findById(requestDTO.getRequestId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Request not found with id: "
                                        + requestDTO.getRequestId()
                        )
                );

        // Find volunteer
        User volunteer = userRepository
                .findById(requestDTO.getVolunteerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Volunteer not found with id: "
                                        + requestDTO.getVolunteerId()
                        )
                );

        // Verify that the selected user is a volunteer
        if (volunteer.getRole() == null
                || !volunteer.getRole().name()
                .equals("VOLUNTEER")) {

            throw new BusinessRuleException(
                    "Selected user is not a volunteer"
            );
        }

        // Verify volunteer profile exists
        VolunteerProfile volunteerProfile =
                volunteerProfileRepository
                        .findByUserId(volunteer.getId())
                        .orElseThrow(() ->
                                new BusinessRuleException(
                                        "Volunteer profile not found"
                                )
                        );

        // Verify volunteer is available
        if (!volunteerProfile.isAvailability()) {

            throw new BusinessRuleException(
                    "Volunteer is not available"
            );
        }

        // Completed requests cannot receive assignments
        if (request.getStatus() == null
                || request.getStatus().name()
                .equals("COMPLETED")) {

            throw new BusinessRuleException(
                    "Request cannot be assigned"
            );
        }

        // Create assignment
        Assignment assignment = new Assignment();

        assignment.setRequest(request);
        assignment.setVolunteer(volunteer);

        // New assignment starts as PENDING
        assignment.setStatus(
                AssignmentStatus.PENDING
        );

        // Server controls assignment time
        assignment.setAssignedAt(
                LocalDateTime.now()
        );

        assignment.setNotes(
                requestDTO.getNotes()
        );

        Assignment savedAssignment =
                assignmentRepository.save(assignment);

        return convertToResponseDTO(
                savedAssignment
        );
    }

    public java.util.List<AssignmentResponseDTO> getAssignmentsForVolunteer(User volunteer) {
        return assignmentRepository.findByVolunteerIdOrderByAssignedAtDesc(volunteer.getId())
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    public AssignmentResponseDTO getAssignmentForVolunteer(Long id, User volunteer) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with id: " + id));

        if (!assignment.getVolunteer().getId().equals(volunteer.getId())) {
            throw new BusinessRuleException("You are not allowed to view this assignment");
        }

        return convertToResponseDTO(assignment);
    }

    // Validate assignment status workflow
    public boolean isValidStatusTransition(
            AssignmentStatus currentStatus,
            AssignmentStatus newStatus) {

        if (currentStatus == null || newStatus == null) {
            return false;
        }

        return switch (currentStatus) {

            case PENDING ->
                    newStatus == AssignmentStatus.ACCEPTED;

            case ACCEPTED ->
                    newStatus == AssignmentStatus.IN_PROGRESS;

            case IN_PROGRESS ->
                    newStatus == AssignmentStatus.COMPLETED;

            default ->
                    false;
        };
    }

    // Update assignment status
    @Transactional
    public AssignmentResponseDTO updateAssignmentStatus(
            Long id,
            AssignmentStatus newStatus,
            User authenticatedVolunteer) {

        Assignment assignment = assignmentRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Assignment not found with id: " + id
                        )
                );

        // Only the assigned volunteer can update the assignment
        if (!assignment.getVolunteer().getId()
                .equals(authenticatedVolunteer.getId())) {

            throw new BusinessRuleException(
                    "You are not allowed to modify this assignment"
            );
        }

        AssignmentStatus currentStatus =
                assignment.getStatus();

        // Validate status transition
        if (!isValidStatusTransition(
                currentStatus,
                newStatus)) {

            throw new BusinessRuleException(
                    "Invalid assignment status transition from "
                            + currentStatus
                            + " to "
                            + newStatus
            );
        }

        assignment.setStatus(newStatus);

        Assignment updatedAssignment =
                assignmentRepository.save(assignment);

        // Complete the related request when delivery is completed
        if (newStatus == AssignmentStatus.COMPLETED) {

            Request request = assignment.getRequest();

            request.setStatus(
                    com.disasterconnect.enums.RequestStatus.COMPLETED
            );

            requestRepository.save(request);
        }

        return convertToResponseDTO(
                updatedAssignment
        );
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