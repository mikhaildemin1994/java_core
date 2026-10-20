package org.management.app.budget.dto;

import java.math.BigDecimal;

public record BudgetReportDTO(
        Long departmentId,
        Long categoryId,
        String categoryName,
        BigDecimal plannedAmount,
        BigDecimal actualExpenseAmount,
        BigDecimal remainingAmount
) {}
