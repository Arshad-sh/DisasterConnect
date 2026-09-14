package com.disasterconnect.dto;

import com.disasterconnect.enums.RequestStatus;
import jakarta.validation.constraints.NotNull;

public class StatusUpdateRequestDTO {

    @NotNull(message = "Status is required")
    private RequestStatus status;

    public StatusUpdateRequestDTO() {
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }
}