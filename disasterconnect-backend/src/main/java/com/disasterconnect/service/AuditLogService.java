package com.disasterconnect.service;

import com.disasterconnect.entity.AuditLog;
import com.disasterconnect.entity.User;
import com.disasterconnect.repository.AuditLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(
            AuditLogRepository auditLogRepository) {

        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public AuditLog logAction(
            String action,
            User performedBy,
            String entityType,
            Long entityId,
            String details) {

        AuditLog auditLog = new AuditLog();

        auditLog.setAction(action);
        auditLog.setPerformedBy(performedBy);
        auditLog.setEntityType(entityType);
        auditLog.setEntityId(entityId);
        auditLog.setDetails(details);
        auditLog.setCreatedAt(LocalDateTime.now());

        return auditLogRepository.save(auditLog);
    }
}