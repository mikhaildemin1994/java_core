package org.management.app.auditlog.servise;

import lombok.RequiredArgsConstructor;
import org.management.app.auditlog.dto.AuditLogResponseDTO;
import org.management.app.auditlog.model.AuditLog;
import org.management.app.auditlog.repository.AuditLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService{
    private final AuditLogRepository auditLogRepository;

    @Override
    public Page<AuditLogResponseDTO> getAllLogs(Pageable pageable) {
        return auditLogRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    public AuditLogResponseDTO mapToResponse(AuditLog auditLog) {
        return new AuditLogResponseDTO(
                auditLog.getId(),
                auditLog.getTransactionId(),
                auditLog.getUser().getId(),
                auditLog.getRole(),
                auditLog.getDateTime(),
                auditLog.getAction()
        );
    }
}
