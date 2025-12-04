package com.waleed.expenseTracker.repository;

import com.waleed.expenseTracker.enums.CategoryType;
import com.waleed.expenseTracker.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    Optional<Transaction> findByIdAndBudgetId(long id, long budgetId);
    boolean existsByIdAndBudgetId(long id, long budgetId);

    @Query("""
        SELECT COALESCE(SUM(t.amount), 0)
        FROM Transaction t
        WHERE t.budget.id = :budgetId AND t.category.type = :type
    """)
    Double sumByBudgetIdAndCategoryType(@Param("budgetId") Long budgetId,
                                              @Param("type") CategoryType type);
}
