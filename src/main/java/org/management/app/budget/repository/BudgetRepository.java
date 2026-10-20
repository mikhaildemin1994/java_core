package org.management.app.budget.repository;

import org.management.app.budget.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    List<Budget> findByDepartmentId(Long departmentId);

    @Query("""
        SELECT COALESCE(SUM(t.amount), 0.0) 
        FROM Transaction t 
        WHERE t.accountFrom.department.id = :departmentId 
          AND t.category.id = :categoryId 
          AND t.type = 'EXPENSE' 
          AND t.status = 'CONFIRMED' 
          AND t.date >= :startDate 
          AND t.date <= :endDate
    """)
    BigDecimal calculateActualExpense(
            @Param("departmentId") Long departmentId,
            @Param("categoryId") Long categoryId,
            @Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate
    );
}
