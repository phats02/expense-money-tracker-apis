package com.ferb.expenseMoneyTracker.validators;

import com.ferb.expenseMoneyTracker.annotations.ValidTimeZone;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.ZoneId;
import java.util.Set;

public class ValidTimeZoneValidator implements ConstraintValidator<ValidTimeZone, String> {

    private static final Set<String> AVAILABLE_ZONE_IDS = ZoneId.getAvailableZoneIds();

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // use @NotNull separately if you want to enforce non-null
        }
        return AVAILABLE_ZONE_IDS.contains(value);
    }
}
