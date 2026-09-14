package com.disasterconnect.dto;

import jakarta.validation.constraints.NotNull;

public class AssignmentRequestDTO {

    @NotNull(message = "Request ID is required")
    private Long requestId;

    @NotNull(message = "Volunteer ID is required")
    private Long volunteerId;

    private String notes;

    public AssignmentRequestDTO() {
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Long getVolunteerId() {
        return volunteerId;
    }

    public void setVolunteerId(Long volunteerId) {
        this.volunteerId = volunteerId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}