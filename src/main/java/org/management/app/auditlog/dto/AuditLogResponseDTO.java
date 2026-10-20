package org.management.app.auditlog.dto;

import org.management.app.auditlog.model.AuditLog;
import org.management.app.util.enums.UserRole;

import java.time.LocalDateTime;

public record AuditLogResponseDTO(
        Long id,
        Long transactionId,
        Long userId,
        UserRole userRole,
        LocalDateTime dateTime,
        AuditLog.Action action
) {}
