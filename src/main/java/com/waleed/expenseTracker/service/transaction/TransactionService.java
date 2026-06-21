package com.waleed.expenseTracker.service.transaction;

import com.waleed.expenseTracker.enums.CategoryType;
import com.waleed.expenseTracker.model.dto.TransactionDto;
import com.waleed.expenseTracker.model.entity.Transaction;
import com.waleed.expenseTracker.model.request.transaction.CreateTransactionRequest;
import com.waleed.expenseTracker.model.request.transaction.UpdateTransactionRequest;

import java.util.List;

public interface TransactionService {
    Transaction create(CreateTransactionRequest request, long userId);
    Transaction update(UpdateTransactionRequest request, long txId, long userId);
    Transaction findOne(long id, long budgetId);
    void delete(long id, long budgetId, long userId);
    List <Transaction> findByBudget(long budgetId, CategoryType type, long userId);
    List<TransactionDto> findByBudgetAndCategory(long budgetId, long categoryId, long userId);
}
