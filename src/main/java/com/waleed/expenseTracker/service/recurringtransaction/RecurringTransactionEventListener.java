package com.waleed.expenseTracker.service.recurringtransaction;

import com.waleed.expenseTracker.enums.CategoryType;
import com.waleed.expenseTracker.model.entity.Budget;
import com.waleed.expenseTracker.model.entity.RecurringTransaction;
import com.waleed.expenseTracker.model.entity.Transaction;
import com.waleed.expenseTracker.model.event.BudgetCreatedEvent;
import com.waleed.expenseTracker.repository.RecurringTransactionRepository;
import com.waleed.expenseTracker.repository.TransactionRepository;
import com.waleed.expenseTracker.service.budget.BudgetService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class RecurringTransactionEventListener {
    private final RecurringTransactionRepository recurringTransactionRepository;
    private final TransactionRepository transactionRepository;
    private final BudgetService budgetService;

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    @TransactionalEventListener
    public void onBudgetCreated(BudgetCreatedEvent event) {
        var templates = recurringTransactionRepository.findByUserIdAndActiveTrue(event.userId());
        if (templates.isEmpty()) return;

        Budget budget = budgetService.findById(event.budgetId(), event.userId());
        List<Transaction> spawned = templates.stream().map(rt -> transaction(budget, rt)).toList();
        transactionRepository.saveAll(spawned);
        log.info("Spawned {} transactions from recurring templates for budget {}", spawned.size(), event.budgetId());

        double totalIncome = transactionRepository.sumAmountByBudgetIdAndCategoryType(event.budgetId(), CategoryType.INCOME);
        double totalExpense = transactionRepository.sumAmountByBudgetIdAndCategoryType(event.budgetId(), CategoryType.EXPENSE);
        budgetService.updateTotals(event.budgetId(), totalIncome, totalExpense, event.userId());
    }

    private Transaction transaction(Budget budget, RecurringTransaction rt) {
        return Transaction.builder()
                .name(rt.getName())
                .amount(rt.getAmount())
                .description(rt.getDescription())
                .category(rt.getCategory())
                .budget(budget)
                .issuedAt(LocalDate.now())
                .build();
    }
}
