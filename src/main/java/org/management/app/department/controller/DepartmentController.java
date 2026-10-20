package org.management.app.department.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.management.app.department.dto.DepartmentRequestDTO;
import org.management.app.department.dto.DepartmentResponseDTO;
import org.management.app.department.servise.DepartmentServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentServiceImpl departmentServiceImpl;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DepartmentResponseDTO> create(
            @Valid @RequestBody DepartmentRequestDTO request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(departmentServiceImpl.create(request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCE_MANAGER', 'AUDITOR', 'ACCOUNTANT')")
    public ResponseEntity<DepartmentResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(departmentServiceImpl.getById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCE_MANAGER', 'AUDITOR')")
    public ResponseEntity<Page<DepartmentResponseDTO>> getAll(
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        return ResponseEntity.ok(departmentServiceImpl.getAll(pageable));
    }
}
