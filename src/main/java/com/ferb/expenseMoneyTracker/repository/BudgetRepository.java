package com.ferb.expenseMoneyTracker.repository;

import com.ferb.expenseMoneyTracker.entity.Budget;
import com.ferb.expenseMoneyTracker.entity.User;
import com.ferb.expenseMoneyTracker.enums.BudgetRenewCycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface BudgetRepository extends JpaRepository<Budget, UUID> {
    List<Budget> getBudgetByOwner(User owner);
    Budget getBudgetByIdAndOwner(UUID id, User owner);
    @Query("""
    SELECT b FROM Budget b\s
    WHERE b.owner = :owner\s
      AND (
            (b.renewCycle = 'NONE'\s
             AND b.startDate = :fromDate\s
             AND b.endDate = :toDate)
         OR\s
            (:renewCycle != 'NONE'\s
             AND b.renewCycle = :renewCycle\s
             AND b.startDate <= :fromDate\s
             AND (b.endDate IS NULL OR b.endDate >= :toDate))
          )
   \s""")
    List<Budget> getByOwnerAndDateBetweenInCycle(
            User owner,
            LocalDate fromDate,
            LocalDate toDate,
            BudgetRenewCycle renewCycle
    );
}
