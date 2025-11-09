package com.ferb.expenseMoneyTracker.entity;

import com.ferb.expenseMoneyTracker.abstracts.Auditable;
import com.ferb.expenseMoneyTracker.enums.BudgetRenewCycle;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name= "budgets")
public class Budget extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column( nullable = false)
    private LocalDate startDate;

    @Column( nullable = true)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column()
    private BudgetRenewCycle renewCycle = BudgetRenewCycle.none;

}
