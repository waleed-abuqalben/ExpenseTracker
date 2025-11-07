package com.waleed.expenseTracker.service.category;
import com.waleed.expenseTracker.model.entity.Category;
import com.waleed.expenseTracker.model.request.category.CreateCategoryRequest;

import java.util.List;

public interface CategoryService {
   List<Category> findAll();
   List<Category> findAll(Long userId);
   List<Category> findAllByType(String type, Long userId);
   Category findById(Long categoryId, Long userId);
   Category create(CreateCategoryRequest request, Long userId);
   Category updateName(Long categoryId, String newName, Long userId);
}
