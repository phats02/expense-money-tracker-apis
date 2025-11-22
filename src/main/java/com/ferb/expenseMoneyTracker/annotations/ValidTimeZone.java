package com.ferb.expenseMoneyTracker.annotations;

import com.ferb.expenseMoneyTracker.validators.ValidTimeZoneValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidTimeZoneValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTimeZone {

    String message() default "Invalid timezone. Must be a valid IANA timezone (e.g., Europe/Paris, America/New_York)";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
