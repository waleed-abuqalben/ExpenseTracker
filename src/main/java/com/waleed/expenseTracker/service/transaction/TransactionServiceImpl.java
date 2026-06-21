package com.waleed.expenseTracker.service.transaction;

import com.waleed.expenseTracker.enums.CategoryType;
import com.waleed.expenseTracker.exception.AppException;
import com.waleed.expenseTracker.model.dto.TransactionDto;
import com.waleed.expenseTracker.model.entity.Budget;
import com.waleed.expenseTracker.model.entity.Category;
import com.waleed.expenseTracker.model.entity.Transaction;
import com.waleed.expenseTracker.model.request.transaction.CreateTransactionRequest;
import com.waleed.expenseTracker.model.request.transaction.UpdateTransactionRequest;
import com.waleed.expenseTracker.repository.TransactionRepository;
import com.waleed.expenseTracker.service.budget.BudgetService;
import com.waleed.expenseTracker.service.category.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.waleed.expenseTracker.exception.AppException.raiseIf;

@Slf4j
@RequiredArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {
    private static final String BUDGET_NOT_EXIST = "Budget does not exist";
    private final CategoryService categoryService;
    private final BudgetService budgetService;
    private final TransactionRepository transactionRepository;

    @Transactional
    @Override
    public Transaction create(CreateTransactionRequest request, long userId) {
        long budgetId = request.budgetId();
        log.info("Creating Transaction: {} to Budget {}, userId: {}", request, budgetId, userId);
        Budget budget = budgetService.findById(budgetId, userId);
        Category category = categoryService.findById(request.categoryId(), userId);
        Transaction created = createTransaction(request, category, budget);
        Transaction saved =  transactionRepository.save(created);
        this.updateTotals(budgetId, userId);
        return saved;
    }

    @Override
    public Transaction update(UpdateTransactionRequest request, long txId, long userId) {
        long budgetId = request.budgetId();
        log.info("Updating Transaction {}: {} to Budget {}, userId: {}", txId, request, budgetId, userId);
        raiseIf(budgetService.exists(budgetId, userId), BUDGET_NOT_EXIST);
        Category category = categoryService.findById(request.categoryId(), userId);
        Transaction updated = updateTransaction(request, findOne(txId, budgetId), category);
        Transaction saved = transactionRepository.save(updated);
        this.updateTotals(budgetId, userId);
        return saved;
    }

    @Override
    public List<Transaction> findByBudget(long budgetId, CategoryType type, long userId) {
        raiseIf(!budgetService.exists(budgetId, userId), BUDGET_NOT_EXIST);
        return type == null
                ? transactionRepository.findByBudgetId(budgetId)
                : transactionRepository.findByBudgetIdAndBudgetUserIdAndCategoryType(budgetId, userId, type);
    }


    @Override
    public Transaction findOne(long txId, long budgetId) {
        return transactionRepository.findByIdAndBudgetId(txId, budgetId)
                .orElseThrow(() -> new AppException("Transaction not found"));
    }

    @Override
    public void delete(long txId, long budgetId, long userId) {
        log.info("Deleting Transaction {} from Budget {}, userId: {}", txId, budgetId, userId);
        raiseIf(!budgetService.exists(budgetId, userId), BUDGET_NOT_EXIST);
        transactionRepository.delete(findOne(txId, budgetId));
        updateTotals(budgetId, userId);
    }


    @Override
    public List<TransactionDto> findByBudgetAndCategory(long budgetId, long categoryId, long userId) {
        return List.of();
    }


    private Transaction createTransaction(CreateTransactionRequest request, Category category, Budget budget) {
        Transaction tx = new Transaction();
        tx.setName(request.name());
        tx.setDescription(request.description());
        tx.setAmount(request.amount());
        tx.setCategory(category);
        tx.setBudget(budget);
        tx.setIssuedAt(request.issuedAt() == null ? LocalDate.now() : request.issuedAt());
        return tx;
    }

    private Transaction updateTransaction(UpdateTransactionRequest request, Transaction existing, Category category) {
        existing.setName(request.name());
        existing.setDescription(request.description());
        existing.setAmount(request.amount());
        existing.setCategory(category);
        existing.setIssuedAt(request.issuedAt());
        return existing;
    }


    protected void updateTotals(long budgetId, long userId) {
        Double totalIncome = transactionRepository.sumAmountByBudgetIdAndCategoryType(budgetId, CategoryType.INCOME);
        Double totalExpense = transactionRepository.sumAmountByBudgetIdAndCategoryType(budgetId, CategoryType.EXPENSE);
        budgetService.updateTotals(budgetId, totalIncome, totalExpense, userId);

    }
}
