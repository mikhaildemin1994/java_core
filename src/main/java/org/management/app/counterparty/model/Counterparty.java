package org.management.app.counterparty.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.management.app.util.enums.CounterpartyType;

@Entity
@Table(name = "counterparties")
@NoArgsConstructor
@Getter
@Setter
public class Counterparty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "identity", nullable = false, unique = true)
    private String identityNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "counterparty_type", nullable = false)
    private CounterpartyType type;
}