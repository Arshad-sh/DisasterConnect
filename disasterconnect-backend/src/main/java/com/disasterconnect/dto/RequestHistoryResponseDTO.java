package com.disasterconnect.dto;

import com.disasterconnect.enums.RequestStatus;

import java.time.LocalDateTime;

public class RequestHistoryResponseDTO {

    private Long id;
    private Long requestId;
    private RequestStatus oldStatus;
    private RequestStatus newStatus;
    private LocalDateTime changedAt;
    private Long changedBy;

    public RequestHistoryResponseDTO() {
    }

    public RequestHistoryResponseDTO(
            Long id,
            Long requestId,
            RequestStatus oldStatus,
            RequestStatus newStatus,
            LocalDateTime changedAt,
            Long changedBy) {

        this.id = id;
        this.requestId = requestId;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.changedAt = changedAt;
        this.changedBy = changedBy;
    }

    public Long getId() {
        return id;
    }

    public Long getRequestId() {
        return requestId;
    }

    public RequestStatus getOldStatus() {
        return oldStatus;
    }

    public RequestStatus getNewStatus() {
        return newStatus;
    }

    public LocalDateTime getChangedAt() {
        return changedAt;
    }

    public Long getChangedBy() {
        return changedBy;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public void setOldStatus(RequestStatus oldStatus) {
        this.oldStatus = oldStatus;
    }

    public void setNewStatus(RequestStatus newStatus) {
        this.newStatus = newStatus;
    }

    public void setChangedAt(LocalDateTime changedAt) {
        this.changedAt = changedAt;
    }

    public void setChangedBy(Long changedBy) {
        this.changedBy = changedBy;
    }
}