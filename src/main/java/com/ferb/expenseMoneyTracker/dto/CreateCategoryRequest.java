package com.ferb.expenseMoneyTracker.dto;

import com.ferb.expenseMoneyTracker.annotations.ValidEnum;
import com.ferb.expenseMoneyTracker.enums.CategoryType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateCategoryRequest {
    @NotNull
    @ValidEnum(enumClass = CategoryType.class)
    private CategoryType type;

    @NotNull
    private String title;

    UUID parentId;
}
