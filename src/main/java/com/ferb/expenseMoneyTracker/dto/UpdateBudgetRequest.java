package com.ferb.expenseMoneyTracker.dto;

import com.ferb.expenseMoneyTracker.annotations.ValidEnum;
import com.ferb.expenseMoneyTracker.enums.BudgetRenewCycle;
import com.ferb.expenseMoneyTracker.exception.BusinessException;
import jakarta.validation.constraints.AssertTrue;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class UpdateBudgetRequest {
    private UUID categoryId;

    private BigDecimal amount;

    private String note;

    private String title;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @ValidEnum(enumClass = BudgetRenewCycle.class)
    private BudgetRenewCycle renewCycle;

    @AssertTrue
    public boolean onlyHasEndDateOrRenewCycle () {
        if (renewCycle == null) {
            return true;
        }

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
