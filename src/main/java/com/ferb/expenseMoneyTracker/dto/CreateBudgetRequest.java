package com.ferb.expenseMoneyTracker.dto;

import com.ferb.expenseMoneyTracker.annotations.ValidEnum;
import com.ferb.expenseMoneyTracker.enums.BudgetRenewCycle;
import com.ferb.expenseMoneyTracker.exception.BusinessException;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class CreateBudgetRequest {
    @NotNull
    private UUID categoryId;

    @NotNull
    @Positive
    private BigDecimal amount;

    private String note;
    @NotNull
    private String title;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @NotNull
    @ValidEnum(enumClass = BudgetRenewCycle.class)
    private BudgetRenewCycle renewCycle;

    @AssertTrue
    public boolean onlyHasEndDateOrRenewCycle () {
        if (renewCycle != BudgetRenewCycle.none && endDate != null) {
            throw new BusinessException("If renewCycle exist, cannot specific the endDate.");
        }

        return true;
    }

    @AssertTrue(message = "fromDate must be before or equal to toDate")
    private boolean isValidDateRange() {
        if (startDate == null || endDate == null) {
            return true;
        }
        return !startDate.isAfter(endDate);
    }
}
