package org.management.app.budget.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BudgetCreateDTO(
        @NotNull
        Long departmentId,

        @NotNull
        Long categoryId,

        @NotNull
        @Positive
        BigDecimal amount,

        @NotNull
        LocalDateTime startPeriod,

        @NotNull
        LocalDateTime endPeriod
) {}
