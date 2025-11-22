package com.ferb.expenseMoneyTracker.service;

import com.ferb.expenseMoneyTracker.checkers.DateRangeChecker;
import com.ferb.expenseMoneyTracker.dto.CreateBudgetRequest;
import com.ferb.expenseMoneyTracker.dto.UpdateBudgetRequest;
import com.ferb.expenseMoneyTracker.entity.Budget;
import com.ferb.expenseMoneyTracker.entity.Category;
import com.ferb.expenseMoneyTracker.entity.User;
import com.ferb.expenseMoneyTracker.enums.BudgetRenewCycle;
import com.ferb.expenseMoneyTracker.exception.BusinessException;
import com.ferb.expenseMoneyTracker.exception.NotFound;
import com.ferb.expenseMoneyTracker.repository.BudgetRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class BudgetService {
    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private CategoryService categoryService;

    public List<Budget> getBudgetsByOwner(User owner) {
        return budgetRepository.getBudgetByOwner(owner);
    }

    public List<Budget> getBudgetsByOwnerAndDateRange(User owner, LocalDate fromDate, LocalDate toDate) {
        DateRangeChecker.RangeType rangeType = DateRangeChecker.detectCalendarRange(fromDate, toDate);

        return budgetRepository.getByOwnerAndDateBetweenInCycle(owner, fromDate, toDate, BudgetRenewCycle.fromRangeType(rangeType));

    }

    public Budget createNewBudget(User owner, CreateBudgetRequest createBudgetRequest) {
        Category category = categoryService.getById(createBudgetRequest.getCategoryId(), owner);

        if (category == null) {
            throw new NotFound("Category");
        }

        Budget newBudget = Budget.builder()
                .amount(createBudgetRequest.getAmount())
                .title(createBudgetRequest.getTitle())
                .note(createBudgetRequest.getNote())
                .startDate(createBudgetRequest.getStartDate())
                .endDate(createBudgetRequest.getEndDate())
                .renewCycle(createBudgetRequest.getRenewCycle())
                .owner(owner)
                .category(category)
                .build();

        return budgetRepository.save(newBudget);
    }

    public Budget updateBudget(User owner, UUID budgetId, UpdateBudgetRequest updateBudgetRequest) {
        Budget budget = budgetRepository.getBudgetByIdAndOwner(budgetId, owner);

        if (budget == null) {
            throw new NotFound("Budget");
        }
        if (budget.getRenewCycle() != BudgetRenewCycle.none && budget.getEndDate() != null) {
            if ((updateBudgetRequest.getStartDate() != null && !updateBudgetRequest.getStartDate().isEqual(budget.getStartDate()))
                    || (updateBudgetRequest.getEndDate() != null && !updateBudgetRequest.getEndDate().isEqual(budget.getEndDate())))
                throw new BusinessException("Ended cycle cannot edit start date or end date");
        }
        if (updateBudgetRequest.getCategoryId() != null) {
            Category category = categoryService.getById(updateBudgetRequest.getCategoryId(), owner);
            if (category == null) {
                throw new NotFound("Category");
            }
            budget.setCategory(category);
        }

        if (updateBudgetRequest.getStartDate() != null) {
            budget.setStartDate(updateBudgetRequest.getStartDate());
        }
        if (updateBudgetRequest.getEndDate() != null) {
            budget.setEndDate(updateBudgetRequest.getEndDate());
        }
        if (updateBudgetRequest.getTitle() != null) {
            budget.setTitle(updateBudgetRequest.getTitle());
        }
        if (updateBudgetRequest.getNote() != null) {
            budget.setNote(updateBudgetRequest.getNote());
        }
        if (updateBudgetRequest.getAmount() != null) {
            budget.setAmount(updateBudgetRequest.getAmount());
        }
        if (budget.getEndDate() != null && !budget.getStartDate().isBefore(budget.getEndDate())) {
            throw new BusinessException("Start date must before end date");
        }
        if (updateBudgetRequest.getRenewCycle() != null && updateBudgetRequest.getRenewCycle() != budget.getRenewCycle()) {
            if (budget.getRenewCycle() == BudgetRenewCycle.none) {
                // Turn renew on
                budget.setEndDate(null);
            } else if (updateBudgetRequest.getRenewCycle() == BudgetRenewCycle.none) {
                // Turn renew off
                LocalDate today = LocalDate.now(owner.getTimeZoneId());
                budget.setEndDate(today);
            }
            budget.setRenewCycle(updateBudgetRequest.getRenewCycle());
        }


        return budgetRepository.save(budget);
    }

    public void deleteBudget(User owner, UUID id) {
        Budget budget = budgetRepository.getBudgetByIdAndOwner(id, owner);

        if (budget == null) {
            throw new NotFound("Budget");
        }

        budgetRepository.delete(budget);
    }
}
