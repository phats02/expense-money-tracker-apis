package com.ferb.expenseMoneyTracker.repository;

import com.ferb.expenseMoneyTracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    User findOneByEmail(String email);

    @Query("select case when count(c)> 0 then true else false end from User c where lower(c.email) like lower(:email)")
    boolean existsByName(String email);
}
