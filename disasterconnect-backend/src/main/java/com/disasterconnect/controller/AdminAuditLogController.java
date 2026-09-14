package com.disasterconnect.controller;

import com.disasterconnect.entity.AuditLog;
import com.disasterconnect.repository.AuditLogRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/audit-logs")
public class AdminAuditLogController {

    private final AuditLogRepository auditLogRepository;

    public AdminAuditLogController(
            AuditLogRepository auditLogRepository) {

        this.auditLogRepository = auditLogRepository;
    }

    @GetMapping
    public List<AuditLogResponse> getAllAuditLogs() {

        return auditLogRepository
                .findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public AuditLogResponse getAuditLogById(
            @PathVariable Long id) {

        AuditLog auditLog =
                auditLogRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Audit log not found with id: "
                                                + id
                                )
                        );

        return convertToResponse(auditLog);
    }

    private AuditLogResponse convertToResponse(
            AuditLog auditLog) {

        return new AuditLogResponse(
                auditLog.getId(),
                auditLog.getAction(),
                auditLog.getPerformedBy() != null
                        ? auditLog.getPerformedBy().getId()
                        : null,
                auditLog.getEntityType(),
                auditLog.getEntityId(),
                auditLog.getDetails(),
                auditLog.getCreatedAt()
        );
    }

    public record AuditLogResponse(
            Long id,
            String action,
            Long performedBy,
            String entityType,
            Long entityId,
            String details,
            java.time.LocalDateTime createdAt
    ) {
    }
}