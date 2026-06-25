package com.waleed.expenseTracker.repository;

import com.waleed.expenseTracker.enums.CategoryType;
import com.waleed.expenseTracker.model.entity.RecurringTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecurringTransactionRepository extends JpaRepository<RecurringTransaction, Long> {
    List<RecurringTransaction> findByUserId(Long userId);
    List<RecurringTransaction> findByUserIdAndCategoryType(Long userId, CategoryType type);
    Optional<RecurringTransaction> findByIdAndUserId(Long id, Long userId);
    List<RecurringTransaction> findByUserIdAndActiveTrue(Long userId);
}
