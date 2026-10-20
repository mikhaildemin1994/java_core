package org.example.app.budget;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.management.app.budget.dto.BudgetReportDTO;
import org.management.app.budget.model.Budget;
import org.management.app.budget.repository.BudgetRepository;
import org.management.app.budget.servise.BudgetServiceImpl;
import org.management.app.category.model.Category;
import org.management.app.department.model.Department;
import org.management.app.user.model.User;
import org.management.app.util.enums.UserRole;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BudgetServiceImplTest {
    @Mock
    private BudgetRepository budgetRepository;

    @InjectMocks
    private BudgetServiceImpl budgetService;

    private Budget mockBudget;
    private Department mockDepartment;
    private Category mockCategory;

    @BeforeEach
    void setUp() {
        mockDepartment = new Department();
        mockDepartment.setId(10L);

        mockCategory = new Category();
        mockCategory.setId(20L);
        mockCategory.setName("Маркетинг");

        mockBudget = Budget.builder()
                .id(1L)
                .amount(new BigDecimal("10000.00"))
                .department(mockDepartment)
                .category(mockCategory)
                .startPeriod(LocalDate.of(2026, 9, 1))
                .endPeriod(LocalDate.of(2026, 9, 30))
                .build();
    }

    @Test
    @DisplayName("Позитивный сценарий: " +
            "Расчет исполнения бюджета должен корректно вычислять фактические расходы и остаток")
    void getBudgetReport_Success() {
        // Given
        User financeManager = new User();
        financeManager.setUserRole(UserRole.FINANCE_MANAGER);

        BigDecimal mockActualExpense = new BigDecimal("4500.50");
        BigDecimal expectedRemaining = new BigDecimal("5499.50");

        when(budgetRepository.findById(1L)).thenReturn(Optional.of(mockBudget));
        when(budgetRepository.calculateActualExpense(
                eq(mockDepartment.getId()),
                eq(mockCategory.getId()),
                any(Instant.class),
                any(Instant.class)
        )).thenReturn(mockActualExpense);

        // When
        BudgetReportDTO report = budgetService.getBudgetReport(1L, financeManager);

        // Then
        assertNotNull(report);
        assertEquals(mockDepartment.getId(), report.departmentId());
        assertEquals(mockCategory.getId(), report.categoryId());
        assertEquals("Маркетинг", report.categoryName());
        assertEquals(new BigDecimal("10000.00"), report.plannedAmount());
        assertEquals(mockActualExpense, report.actualExpenseAmount());
        assertEquals(expectedRemaining, report.remainingAmount());

        verify(
                budgetRepository,
                times(1)).calculateActualExpense(any(),
                any(),
                any(),
                any()
        );
    }

    @Test
    @DisplayName("Негативный сценарий: " +
            "ACCOUNTANT пытается посмотреть бюджет чужого подразделения — ожидается AccessDeniedException")
    void getBudgetReport_WhenAccountantAccessesOtherDepartmentBudget_ShouldThrowAccessDeniedException() {
        // Given
        Department otherDepartment = new Department();
        otherDepartment.setId(99L);

        User accountantUser = new User();
        accountantUser.setUserRole(UserRole.ACCOUNTANT);
        accountantUser.setDepartment(otherDepartment);

        when(budgetRepository.findById(1L)).thenReturn(Optional.of(mockBudget));

        // When & Then
        assertThrows(AccessDeniedException.class, () -> {
            budgetService.getBudgetReport(1L, accountantUser);
        });

        verify(budgetRepository, never()).calculateActualExpense(any(), any(), any(), any());
    }

    @Test
    @DisplayName("Позитивный сценарий: " +
            "ACCOUNTANT имеет право беспрепятственно смотреть бюджет своего собственного подразделения")
    void getBudgetReport_WhenAccountantAccessesOwnDepartmentBudget_ShouldAllow() {
        // Given
        User accountantUser = new User();
        accountantUser.setUserRole(UserRole.ACCOUNTANT);
        accountantUser.setDepartment(mockDepartment); // Департаменты совпадают (10L)

        when(budgetRepository.findById(1L)).thenReturn(Optional.of(mockBudget));
        when(budgetRepository.calculateActualExpense(any(),
                any(),
                any(),
                any())).thenReturn(BigDecimal.ZERO);

        // When
        BudgetReportDTO report = budgetService.getBudgetReport(1L, accountantUser);

        // Then
        assertNotNull(report);
        assertEquals(new BigDecimal("10000.00"), report.remainingAmount());
    }
}
