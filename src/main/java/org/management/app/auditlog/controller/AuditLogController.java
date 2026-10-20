package org.management.app.auditlog.controller;

import lombok.RequiredArgsConstructor;
import org.management.app.auditlog.dto.AuditLogResponseDTO;
import org.management.app.auditlog.servise.AuditLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {
   private final AuditLogService auditLogService;

   @GetMapping
   @PreAuthorize("hasRole('ADMIN','AUDITOR')")
   public ResponseEntity<Page<AuditLogResponseDTO>> getAllLogs(
          @PageableDefault (sort = "dateTime") Pageable pageable
   ) {
      return ResponseEntity.ok(auditLogService.getAllLogs(pageable));
   }
}
