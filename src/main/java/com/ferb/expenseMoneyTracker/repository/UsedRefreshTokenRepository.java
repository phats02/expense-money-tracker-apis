package com.ferb.expenseMoneyTracker.repository;

import com.ferb.expenseMoneyTracker.entity.UsedRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UsedRefreshTokenRepository extends JpaRepository<UsedRefreshToken, UUID> {
}
