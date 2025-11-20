package com.ferb.expenseMoneyTracker.repository;

import com.ferb.expenseMoneyTracker.entity.Category;
import com.ferb.expenseMoneyTracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    List<Category> getByOwner(User owner);
    Category getByIdAndOwner(UUID id, User owner);
}
