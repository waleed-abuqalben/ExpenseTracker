package com.waleed.expenseTracker.service.transaction;

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
    List <TransactionDto> findByBudget(long budgetId, long userId);
    List<TransactionDto> findByBudgetAndCategory(long budgetId, long categoryId, long userId);
    void raiseIfNotExist(long id, long budgetId);
}
