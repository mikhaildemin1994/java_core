package org.management.app.account.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.management.app.department.model.Department;
import org.management.app.transaction.model.Transaction;
import org.management.app.util.enums.AccountType;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "accounts")
@NoArgsConstructor
@Getter
@Setter
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private AccountType type;

    @Column(name = "currency", nullable = false)
    private String currency;

    @Column(name = "balance", nullable = false)
    @Setter(AccessLevel.NONE)
    private BigDecimal balance = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToMany(mappedBy = "accountFrom")
    private List<Transaction> transactions;

    public void income (BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    public void expense (BigDecimal amount) {
        if(amount.compareTo(BigDecimal.ZERO) >= 0) {
            this.balance = this.balance.subtract(amount);
        }
    }
}
