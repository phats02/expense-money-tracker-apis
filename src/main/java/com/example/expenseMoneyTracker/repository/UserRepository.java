package com.example.expenseMoneyTracker.repository;

import com.example.expenseMoneyTracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    User findByEmail(String email);
}
