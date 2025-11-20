package com.ferb.expenseMoneyTracker.service;

import com.ferb.expenseMoneyTracker.dto.CreateCategoryRequest;
import com.ferb.expenseMoneyTracker.dto.UpdateCategoryRequest;
import com.ferb.expenseMoneyTracker.entity.Category;
import com.ferb.expenseMoneyTracker.entity.User;
import com.ferb.expenseMoneyTracker.exception.BusinessException;
import com.ferb.expenseMoneyTracker.exception.NotFound;
import com.ferb.expenseMoneyTracker.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    UserService userService;


    public List<Category> getByOwnerEmail(String ownerEmail) {
        User owner =userService.findOneUserByEmail(ownerEmail);

        return categoryRepository.getByOwner(owner);
    }

    public Category createNewCategory(CreateCategoryRequest dto, String ownerEmail) {
        User owner =userService.findOneUserByEmail(ownerEmail);
        Category parent = null;
        if (dto.getParentId() != null) {
            parent=  categoryRepository.getByIdAndOwner(dto.getParentId(), owner);

            if (parent == null)
                throw new NotFound("Category parent");

            if (parent.getParent() != null)
                throw new BusinessException("Only support one hierarchy");
        }

        Category newCategory = Category.builder()
                .title(dto.getTitle())
                .parent(parent)
                .type(dto.getType())
                .owner(owner)
                .iconUrl(null)
                .build();
        return categoryRepository.save(newCategory);
    }

    public Category updateCategory(UUID categoryId, UpdateCategoryRequest dto, String ownerEmail) {
        User owner =userService.findOneUserByEmail(ownerEmail);

        Category category = categoryRepository.getByIdAndOwner(categoryId, owner);

        if (category == null) {
            throw new NotFound("Category");
        }

        if (dto.getParent().getClear()) {
            category.setParent(null);
        }
        else {
            UUID newParentId = dto.getParent().getSetTo();

            if (newParentId == categoryId)
                throw new BusinessException("The new parent must be different from myself.");

            Category newParent =categoryRepository.getByIdAndOwner(newParentId, owner);

            if (newParent == null)
                throw new NotFound("New parent");

            if (newParent.getParent() != null)
                throw new BusinessException("Only support one hierarchy");

            category.setParent(newParent);
        }



        if (dto.getType() != null) {
            category.setType(dto.getType());
        }

        if (dto.getTitle() != null) {
            category.setTitle(dto.getTitle());
        }

        return categoryRepository.save(category);
    }

    public void deleteCategory(UUID categoryId, String ownerEmail) {
        User owner = userService.findOneUserByEmail(ownerEmail);

        Category category = categoryRepository.getByIdAndOwner(categoryId, owner);
        if (category == null) {
            throw new NotFound("Category");
        }

        categoryRepository.delete(category);
    }
}
