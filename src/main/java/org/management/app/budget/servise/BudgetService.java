package org.management.app.budget.servise;

import org.management.app.budget.dto.BudgetCreateDTO;
import org.management.app.budget.dto.BudgetReportDTO;
import org.management.app.budget.dto.BudgetResponseDTO;
import org.management.app.user.model.User;

public interface BudgetService {
    BudgetResponseDTO createBudget(BudgetCreateDTO create);
    BudgetReportDTO getBudgetReport(Long budgetId, User currentUser);
}
