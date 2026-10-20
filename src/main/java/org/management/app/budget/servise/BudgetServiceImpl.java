package org.management.app.budget.servise;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.management.app.budget.dto.BudgetCreateDTO;
import org.management.app.budget.dto.BudgetReportDTO;
import org.management.app.budget.dto.BudgetResponseDTO;
import org.management.app.budget.model.Budget;
import org.management.app.budget.repository.BudgetRepository;
import org.management.app.category.model.Category;
import org.management.app.category.repository.CategoryRepository;
import org.management.app.department.model.Department;
import org.management.app.department.repository.DepartmentRepository;
import org.management.app.user.model.User;
import org.management.app.util.enums.UserRole;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.*;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {
    private final BudgetRepository budgetRepository;
    private final DepartmentRepository departmentRepository;
    private final CategoryRepository categoryRepository;


    @Override
    public BudgetResponseDTO createBudget(BudgetCreateDTO create) {
        Department department = departmentRepository.findById(create.departmentId())
                .orElseThrow();
        Category category = categoryRepository.findById(create.categoryId())
                .orElseThrow();

        Budget budget = Budget.builder()
                .amount(create.amount())
                .department(department)
                .category(category)
                .startPeriod(create.startPeriod().toLocalDate())
                .endPeriod(create.endPeriod().toLocalDate())
                .build();

        return mapToResponse(budgetRepository.save(budget));
    }

    @Override
    public BudgetReportDTO getBudgetReport(Long budgetId, User currentUser) {
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new EntityNotFoundException("Budget not found"));

        if (currentUser.getUserRole() == UserRole.ACCOUNTANT &&
                !currentUser.getDepartment().getId().equals(budget.getDepartment().getId())) {
            throw new AccessDeniedException("Accountants can only access their own department budget performance");
        }

        Instant startDatePeriod = budget
                .getStartPeriod()
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant();
        Instant endDatePeriod = budget
                .getEndPeriod()
                .atTime(LocalTime.MAX)
                .atZone(ZoneId.systemDefault())
                .toInstant();

        BigDecimal actualExpense = budgetRepository.calculateActualExpense(
                budget.getDepartment().getId(),
                budget.getCategory().getId(),
                startDatePeriod,
                endDatePeriod
        );

        BigDecimal remaining = budget.getAmount().subtract(actualExpense);

        return new BudgetReportDTO(
                budget.getDepartment().getId(),
                budget.getCategory().getId(),
                budget.getCategory().getName(),
                budget.getAmount(),
                actualExpense,
                remaining
        );
    }

    private BudgetResponseDTO mapToResponse(Budget budget) {
        LocalDateTime startDateTime = budget.getStartPeriod().atStartOfDay();
        LocalDateTime endDateTime = budget.getEndPeriod().atTime(LocalTime.MAX);
        return new BudgetResponseDTO(
                budget.getId(),
                budget.getDepartment().getId(),
                budget.getCategory().getId(),
                budget.getAmount(),
                startDateTime,
                endDateTime
        );
    }
}
