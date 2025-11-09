package com.ferb.expenseMoneyTracker.repository;

import com.ferb.expenseMoneyTracker.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
}
