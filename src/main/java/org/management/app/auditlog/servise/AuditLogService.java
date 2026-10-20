package org.management.app.auditlog.servise;

import org.management.app.auditlog.dto.AuditLogResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuditLogService {
    Page<AuditLogResponseDTO> getAllLogs(Pageable pageable);
}
