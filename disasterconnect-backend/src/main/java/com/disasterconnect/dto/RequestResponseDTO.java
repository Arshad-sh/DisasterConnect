package com.disasterconnect.dto;

import com.disasterconnect.enums.RequestStatus;
import com.disasterconnect.enums.Urgency;

public class RequestResponseDTO {

    private Long id;
    private String title;
    private String description;
    private String location;
    private Urgency urgency;
    private RequestStatus status;
    private Long userId;

    // =========================================================
    // DEFAULT CONSTRUCTOR
    // =========================================================

    public RequestResponseDTO() {
    }

    // =========================================================
    // ALL-ARGS CONSTRUCTOR
    // =========================================================

    public RequestResponseDTO(
            Long id,
            String title,
            String description,
            String location,
            Urgency urgency,
            RequestStatus status,
            Long userId) {

        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.urgency = urgency;
        this.status = status;
        this.userId = userId;
    }

    // =========================================================
    // GETTERS AND SETTERS
    // =========================================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Urgency getUrgency() {
        return urgency;
    }

    public void setUrgency(Urgency urgency) {
        this.urgency = urgency;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}