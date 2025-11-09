package com.ferb.expenseMoneyTracker.repository;

import com.ferb.expenseMoneyTracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    User findByEmail(String email);
}
