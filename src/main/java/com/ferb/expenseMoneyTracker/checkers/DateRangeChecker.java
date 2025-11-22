package com.ferb.expenseMoneyTracker.checkers;

import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.temporal.TemporalAdjusters;

public class DateRangeChecker {
    public enum RangeType {
        WEEK, MONTH, YEAR, NONE
    }

    public static RangeType detectCalendarRange(LocalDate fromDate, LocalDate toDate) {
        if (fromDate == null || toDate == null || fromDate.isAfter(toDate)) {
            return RangeType.NONE;
        }

        // Check for full calendar week: Monday to Sunday
        boolean isMondayToSunday = fromDate.getDayOfWeek() == DayOfWeek.MONDAY
                && toDate.getDayOfWeek() == DayOfWeek.SUNDAY
                && fromDate.plusDays(6).equals(toDate); // exactly 6 days later

        if (isMondayToSunday) {
            return RangeType.WEEK;
        }

        // Check for full calendar month: 1st to last day of same month
        boolean isFullMonth = fromDate.getDayOfMonth() == 1
                && toDate.equals(fromDate.with(TemporalAdjusters.lastDayOfMonth()))
                && fromDate.getMonth() == toDate.getMonth()
                && fromDate.getYear() == toDate.getYear();

        if (isFullMonth) {
            return RangeType.MONTH;
        }

        // Check for full calendar year: Jan 1 to Dec 31 of same year
        boolean isFullYear = fromDate.getDayOfYear() == 1
                && toDate.getDayOfYear() == toDate.lengthOfYear() // last day of the year
                && fromDate.getYear() == toDate.getYear();

        if (isFullYear) {
            return RangeType.YEAR;
        }

        return RangeType.NONE;
    }

    // Convenience methods
    public static boolean isFullWeek(LocalDate from, LocalDate to) {
        return detectCalendarRange(from, to) == RangeType.WEEK;
    }

    public static boolean isFullMonth(LocalDate from, LocalDate to) {
        return detectCalendarRange(from, to) == RangeType.MONTH;
    }

    public static boolean isFullYear(LocalDate from, LocalDate to) {
        return detectCalendarRange(from, to) == RangeType.YEAR;
    }
}
