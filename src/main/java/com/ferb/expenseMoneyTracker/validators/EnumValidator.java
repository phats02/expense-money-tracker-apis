package com.ferb.expenseMoneyTracker.validators;

import com.ferb.expenseMoneyTracker.annotations.ValidEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class EnumValidator implements ConstraintValidator<ValidEnum, Enum<?>> {
    private Set<String> allowedValues;

    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        allowedValues = Arrays.stream(constraintAnnotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // @NotNull should handle nulls
        }
        return allowedValues.contains(value.name());
    }
}
