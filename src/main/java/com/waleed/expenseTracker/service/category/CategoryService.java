package com.waleed.expenseTracker.service.category;

import com.waleed.expenseTracker.model.dto.CategoryDto;
import com.waleed.expenseTracker.model.entity.Category;
import com.waleed.expenseTracker.model.request.category.CreateCategoryRequest;

import java.util.List;

public interface CategoryService {
   List<CategoryDto> findAll();
   List<CategoryDto> findAll(Long userId);
   List<CategoryDto> findAllByType(String type, Long userId);
   Category findById(Long categoryId, Long userId);
   CategoryDto create(CreateCategoryRequest request, Long userId);
   CategoryDto updateName(Long categoryId, String newName, Long userId);
   List<CategoryDto> toDto(List<Category> categoryList);
   CategoryDto toDto(Category category);
}
