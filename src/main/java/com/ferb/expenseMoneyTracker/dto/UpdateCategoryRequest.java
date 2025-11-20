package com.ferb.expenseMoneyTracker.dto;

import com.ferb.expenseMoneyTracker.annotations.ValidEnum;
import com.ferb.expenseMoneyTracker.enums.CategoryType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import lombok.Data;

import java.util.UUID;

@Data
public class UpdateCategoryRequest {
    @ValidEnum(enumClass = CategoryType.class)
    private CategoryType type;

    private String title;

    @Valid
    private ParentAction parent;

    @Data
    public static class ParentAction {
        private UUID setTo = null;
        private Boolean clear = false;

        @AssertTrue(message = "In 'parent', exactly one of 'setTo' or 'clear' must be provided")
        public boolean isExactlyOneFieldProvided() {
            boolean hasSetTo = setTo != null;
            boolean hasClear = clear != null && clear;

            return hasSetTo != hasClear;
        }
    }
}
