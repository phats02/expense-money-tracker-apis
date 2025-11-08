package com.ferb.expenseMoneyTracker.repository;

import com.ferb.expenseMoneyTracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    User findByEmail(String email);
}
