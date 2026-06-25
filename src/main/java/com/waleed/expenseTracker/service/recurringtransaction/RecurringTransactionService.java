package com.waleed.expenseTracker.service.recurringtransaction;

import com.waleed.expenseTracker.enums.CategoryType;
import com.waleed.expenseTracker.model.entity.RecurringTransaction;
import com.waleed.expenseTracker.model.request.recurringtransaction.CreateRecurringTransactionRequest;
import com.waleed.expenseTracker.model.request.recurringtransaction.UpdateRecurringTransactionRequest;

import java.util.List;

public interface RecurringTransactionService {
    List<RecurringTransaction> findAll(Long userId, CategoryType type);
    RecurringTransaction findById(Long id, Long userId);
    RecurringTransaction create(CreateRecurringTransactionRequest request, Long userId);
    RecurringTransaction update(Long id, UpdateRecurringTransactionRequest request, Long userId);
    RecurringTransaction updateActivation(Long id, boolean active, Long userId);
    void delete(Long id, Long userId);
}
