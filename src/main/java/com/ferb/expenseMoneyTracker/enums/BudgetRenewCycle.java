package com.ferb.expenseMoneyTracker.enums;

import com.ferb.expenseMoneyTracker.checkers.DateRangeChecker;

public enum BudgetRenewCycle {
    none, weekly ,monthly, yearly;

    public static BudgetRenewCycle fromRangeType(DateRangeChecker.RangeType rangeType) {
        if (rangeType == null) {
            return none;
        }

        return switch (rangeType) {
            case WEEK  -> weekly;
            case MONTH -> monthly;
            case YEAR  -> yearly;
            case NONE  -> none;
        };
    }
}
