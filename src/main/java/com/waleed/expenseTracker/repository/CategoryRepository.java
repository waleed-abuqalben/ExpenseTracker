package com.waleed.expenseTracker.repository;

import com.waleed.expenseTracker.enums.CategoryType;
import com.waleed.expenseTracker.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByUserId(Long userId);
    List<Category> findByTypeAndUserId(CategoryType type, Long userId);
    Optional<Category> findByIdAndUserId(Long categoryId, Long userId);
    Optional<Category> findByNameAndTypeAndUserId(String nama, CategoryType type, Long userId);
}
