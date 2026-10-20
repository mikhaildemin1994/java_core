package org.management.app.transaction.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.management.app.transaction.dto.TransactionCreateDTO;
import org.management.app.transaction.dto.TransactionResponseDTO;
import org.management.app.transaction.servise.TransactionService;
import org.management.app.user.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ACCOUNTANT', 'FINANCE_MANAGER', 'ADMIN')")
    public ResponseEntity<TransactionResponseDTO> createTransaction(
            @Valid @RequestBody TransactionCreateDTO request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(transactionService.createDraught(request, currentUser));
    }

    @PostMapping("/{id}/confirm")
    @PreAuthorize("hasAnyRole('FINANCE_MANAGER', 'ADMIN')")
    public ResponseEntity<TransactionResponseDTO> confirmTransaction(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.confirmTransaction(id));
    }

    @PostMapping("/{id}/reverse")
    @PreAuthorize("hasAnyRole('ACCOUNTANT', 'FINANCE_MANAGER', 'ADMIN')")
    public ResponseEntity<TransactionResponseDTO> reverseTransaction(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(transactionService.reverseTransaction(id, currentUser));
    }

}
