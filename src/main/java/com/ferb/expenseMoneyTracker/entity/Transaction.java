package com.ferb.expenseMoneyTracker.entity;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.ferb.expenseMoneyTracker.abstracts.Auditable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Table(name = "transactions")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column( updatable = false, nullable = false)
    private UUID id;

    @Column
    private LocalDate date;

    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( nullable = false)
    @JsonIdentityReference(alwaysAsId = true)
    private Category category;

    @Column(length = 1000)
    private String note;

    @Column(nullable = false)
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JsonIdentityReference(alwaysAsId = true)
    private Wallet wallet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @JsonIdentityReference(alwaysAsId = true)
    private User owner;
}
