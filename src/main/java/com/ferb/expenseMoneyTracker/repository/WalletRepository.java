package com.ferb.expenseMoneyTracker.repository;

import com.ferb.expenseMoneyTracker.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {
}
