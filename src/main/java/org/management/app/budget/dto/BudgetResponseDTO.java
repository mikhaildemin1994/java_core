package org.management.app.budget.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BudgetResponseDTO(
   Long id,
   Long departmentId,
   Long categoryId,
   BigDecimal amount,
   LocalDateTime startPeriod,
   LocalDateTime endPeriod
) {}
