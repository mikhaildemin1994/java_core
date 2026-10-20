package org.management.app.budget.model;

import jakarta.persistence.*;
import lombok.*;
import org.management.app.category.model.Category;
import org.management.app.department.model.Department;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "budgets")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "start_of_period", nullable = false)
    private LocalDate startPeriod;

    @Column(name = "end_of_period", nullable = false)
    private LocalDate endPeriod;
}