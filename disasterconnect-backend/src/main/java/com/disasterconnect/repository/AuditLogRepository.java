package com.disasterconnect.repository;

import com.disasterconnect.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository
        extends JpaRepository<AuditLog, Long> {

    void deleteAllByPerformedBy_Id(Long userId);
}