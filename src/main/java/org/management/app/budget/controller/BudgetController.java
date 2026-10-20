package org.management.app.budget.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.management.app.budget.dto.BudgetCreateDTO;
import org.management.app.budget.dto.BudgetReportDTO;
import org.management.app.budget.dto.BudgetResponseDTO;
import org.management.app.budget.servise.BudgetService;
import org.management.app.user.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/budgets")
@RequiredArgsConstructor
public class BudgetController {
    private final BudgetService budgetService;

    @PostMapping
    @PreAuthorize("hasAnyRole('FINANCE_MANAGER', 'ADMIN')")
    public ResponseEntity<BudgetResponseDTO> create (
            @Valid @RequestBody BudgetCreateDTO create
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(budgetService.createBudget(create));
    }

    @GetMapping("/{id}/performance")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCE_MANAGER', 'ACCOUNTANT', 'AUDITOR')")
    public ResponseEntity<BudgetReportDTO> getBudgetReport(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(budgetService.getBudgetReport(id, currentUser));
    }
}
