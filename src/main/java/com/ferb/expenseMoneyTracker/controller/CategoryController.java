package com.ferb.expenseMoneyTracker.controller;

import com.ferb.expenseMoneyTracker.dto.CreateCategoryRequest;
import com.ferb.expenseMoneyTracker.dto.CustomUserDetail;
import com.ferb.expenseMoneyTracker.dto.SuccessResponse;
import com.ferb.expenseMoneyTracker.dto.UpdateCategoryRequest;
import com.ferb.expenseMoneyTracker.entity.Category;
import com.ferb.expenseMoneyTracker.service.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/category")
@Tag(name = "Category apis")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping("")
    public SuccessResponse<List<Category>> getAllCategory (@AuthenticationPrincipal CustomUserDetail customUserDetail){
        return new SuccessResponse<>(categoryService.getByOwnerEmail(customUserDetail.getUsername()));
    }

    @PostMapping("")
    public SuccessResponse<Category> createNewCategory(@AuthenticationPrincipal CustomUserDetail customUserDetail,
                                                       @Valid @RequestBody CreateCategoryRequest createCategoryRequest) {
        return new SuccessResponse<>(
                categoryService.createNewCategory(createCategoryRequest, customUserDetail.getUsername())
        );
    }

    @PutMapping("/{id}")
    public SuccessResponse<Category> editCategory(@PathVariable UUID id,
                                                  @AuthenticationPrincipal CustomUserDetail customUserDetail,
                                                  @Valid @RequestBody UpdateCategoryRequest updateCategoryRequest) {
        return new SuccessResponse<>(categoryService.updateCategory(id, updateCategoryRequest, customUserDetail.getUsername()));
    }

    @DeleteMapping("/{id}")
    public SuccessResponse<Object> deleteCategory(@PathVariable UUID id,
                                                  @AuthenticationPrincipal CustomUserDetail customUserDetail) {
        categoryService.deleteCategory(id, customUserDetail.getUsername());

        return new SuccessResponse<>(null);
    }
}
