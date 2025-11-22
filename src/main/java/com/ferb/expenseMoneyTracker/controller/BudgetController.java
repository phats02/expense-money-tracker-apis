package com.ferb.expenseMoneyTracker.controller;

import com.ferb.expenseMoneyTracker.dto.*;
import com.ferb.expenseMoneyTracker.entity.Budget;
import com.ferb.expenseMoneyTracker.service.BudgetService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/budgets")
@Tag(name="Budget apis")
public class BudgetController {
    @Autowired
    private BudgetService budgetService;

    @GetMapping("")
    public SuccessResponse<List<Budget>> getAllBudget(@AuthenticationPrincipal CustomUserDetail customUserDetail,
                                                      @Valid @ModelAttribute GetBudgetRequest getBudgetRequest) {
        return new SuccessResponse<>(budgetService.getBudgetsByOwnerAndDateRange(customUserDetail.getUser(), getBudgetRequest.getFromDate(), getBudgetRequest.getToDate()));
    }

    @PostMapping("")
    public SuccessResponse<Budget> createNewBudget(@AuthenticationPrincipal CustomUserDetail customUserDetail,
                                                   @Valid @RequestBody CreateBudgetRequest createBudgetRequest) {
        return new SuccessResponse<>(budgetService.createNewBudget(customUserDetail.getUser(), createBudgetRequest));
    }

    @PutMapping("/{id}")
    public SuccessResponse<Budget> updateBudget(@AuthenticationPrincipal CustomUserDetail customUserDetail,
                                                @Valid @PathVariable UUID id,
                                                @Valid @RequestBody UpdateBudgetRequest updateBudgetRequest) {
        return new SuccessResponse<>(budgetService.updateBudget(customUserDetail.getUser(), id, updateBudgetRequest));
    }

    @DeleteMapping("/{id}")
    public SuccessResponse<Object> deleteBudget(@AuthenticationPrincipal CustomUserDetail customUserDetail,
                                                @Valid @PathVariable UUID id) {
        budgetService.deleteBudget(customUserDetail.getUser(), id);
        return new SuccessResponse<>(null);
    }
}
