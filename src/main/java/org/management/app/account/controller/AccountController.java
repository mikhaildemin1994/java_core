package org.management.app.account.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.management.app.account.dto.AccountCreateDTO;
import org.management.app.account.dto.AccountResponseDTO;
import org.management.app.account.servise.AccountServiceImpl;
import org.management.app.user.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountServiceImpl accountServiceImpl;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AccountResponseDTO> create(
            @Valid @RequestBody AccountCreateDTO create
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(accountServiceImpl.create(create));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCE_MANAGER', 'ACCOUNTANT', 'AUDITOR')")
    public ResponseEntity<AccountResponseDTO> getById(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.ok(accountServiceImpl.getById(id, currentUser));
    }

    @GetMapping("/department/{departmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCE_MANAGER', 'AUDITOR')")
    public ResponseEntity<List<AccountResponseDTO>> getByDepartment(
            @PathVariable Long departmentId
    ) {
        return ResponseEntity
                .ok(accountServiceImpl.getAccountsByDepartment(departmentId));
    }
}
