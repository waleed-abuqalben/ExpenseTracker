package com.waleed.expenseTracker.repository;

import com.waleed.expenseTracker.model.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    Optional<Budget> findByIdAndUserId(Long id, Long userId);
    boolean existsByYearAndMonthAndUserId(Integer year, Integer month, Long userId);
    boolean existsByIdAndUserId(Long id, Long userId);

    List<Budget> findByUserId(long userId);
}
