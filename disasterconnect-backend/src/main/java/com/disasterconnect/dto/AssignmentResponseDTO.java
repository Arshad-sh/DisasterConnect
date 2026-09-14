package com.disasterconnect.dto;

import com.disasterconnect.enums.AssignmentStatus;

import java.time.LocalDateTime;

public class AssignmentResponseDTO {

    private Long id;
    private Long requestId;
    private Long volunteerId;
    private AssignmentStatus status;
    private LocalDateTime assignedAt;
    private String notes;

    public AssignmentResponseDTO(
            Long id,
            Long requestId,
            Long volunteerId,
            AssignmentStatus status,
            LocalDateTime assignedAt,
            String notes) {

        this.id = id;
        this.requestId = requestId;
        this.volunteerId = volunteerId;
        this.status = status;
        this.assignedAt = assignedAt;
        this.notes = notes;
    }

    public Long getId() {
        return id;
    }

    public Long getRequestId() {
        return requestId;
    }

    public Long getVolunteerId() {
        return volunteerId;
    }

    public AssignmentStatus getStatus() {
        return status;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public String getNotes() {
        return notes;
    }
}