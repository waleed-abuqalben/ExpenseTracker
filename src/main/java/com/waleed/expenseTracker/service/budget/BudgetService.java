package com.waleed.expenseTracker.service.budget;

import com.waleed.expenseTracker.model.entity.Budget;
import com.waleed.expenseTracker.model.request.budget.CreateBudgetRequest;

import java.util.List;

public interface BudgetService {
    Budget findById(long id, long userId);
    List<Budget> findAll(long userId);
    Budget create(CreateBudgetRequest request, long userId);
    void updateTotals(long id, double totalIncome, double totalExpense, long userId);
    boolean existsById(long id, long userId);
    boolean existsByYearAndMonth(int year, int month, long userId);
}
