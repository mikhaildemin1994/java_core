package org.management.app.auditlog.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.management.app.auditlog.model.AuditLog;
import org.management.app.auditlog.repository.AuditLogRepository;
import org.management.app.transaction.dto.TransactionResponseDTO;
import org.management.app.user.model.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditLogAspect {
    private final AuditLogRepository auditLogRepository;

    @AfterReturning(pointcut = "@annotation(auditable)", returning = "result")
    public void audit(JoinPoint joinPoint,
            AuditAnnotation annotation,
            TransactionResponseDTO result) {

        User currentUser = Arrays.stream(joinPoint.getArgs())
                .filter(User.class::isInstance)
                .map(User.class::cast)
                .findFirst()
                .orElse(null);

        if (currentUser == null || result == null) {
            return;
        }

        AuditLog auditLog = AuditLog.builder()
                .transactionId(result.id())
                .user(currentUser)
                .role(currentUser.getUserRole())
                .dateTime(LocalDateTime.now())
                .action(annotation.action())
                .build();

        auditLogRepository.save(auditLog);
    }
}
