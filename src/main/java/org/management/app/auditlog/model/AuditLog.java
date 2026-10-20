package org.management.app.auditlog.model;

import jakarta.persistence.*;
import lombok.*;
import org.management.app.user.model.User;
import org.management.app.util.enums.UserRole;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "transaction_id", nullable = false)
    private Long transactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false)
    private UserRole role;

    @Column(name = "date_time_of_action", nullable = false)
    private LocalDateTime dateTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_details", nullable = false)
    private Action action;

    @PrePersist
    protected void onCreate() {
        this.dateTime = LocalDateTime.now();
    }

    public enum Action {
        CREATION,
        CONFIRMATION,
        REVERSE
    }
}