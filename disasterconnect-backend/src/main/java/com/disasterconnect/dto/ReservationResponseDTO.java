package com.disasterconnect.dto;

import com.disasterconnect.enums.ReservationStatus;

import java.time.LocalDateTime;

public class ReservationResponseDTO {

    private Long id;
    private Long requestId;
    private Long resourceId;
    private Integer quantity;
    private ReservationStatus status;
    private LocalDateTime createdAt;

    public ReservationResponseDTO(
            Long id,
            Long requestId,
            Long resourceId,
            Integer quantity,
            ReservationStatus status,
            LocalDateTime createdAt) {

        this.id = id;
        this.requestId = requestId;
        this.resourceId = resourceId;
        this.quantity = quantity;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getRequestId() {
        return requestId;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}