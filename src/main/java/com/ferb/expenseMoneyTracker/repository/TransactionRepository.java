package com.ferb.expenseMoneyTracker.repository;

import com.ferb.expenseMoneyTracker.entity.Transaction;
import com.ferb.expenseMoneyTracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> getByOwner(User owner);
    List<Transaction> getByOwnerAndDateBetween(User owner, LocalDate fromDate, LocalDate toDate);
    Transaction getByIdAndOwner(UUID id, User owner);
}
