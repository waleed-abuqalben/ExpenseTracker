package com.waleed.expenseTracker.service.transaction;

import com.waleed.expenseTracker.enums.CategoryType;
import com.waleed.expenseTracker.exception.AppException;
import com.waleed.expenseTracker.model.dto.TransactionDto;
import com.waleed.expenseTracker.model.entity.Budget;
import com.waleed.expenseTracker.model.entity.Category;
import com.waleed.expenseTracker.model.entity.Transaction;
import com.waleed.expenseTracker.model.mappers.TransactionMapper;
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

@Slf4j
@RequiredArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {
    private final CategoryService categoryService;
    private final BudgetService budgetService;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper mapper;

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
        log.info("Updating Transaction [txId: {}]: {} to Budget {}, userId: {}", txId, request, budgetId, userId);
        budgetService.raiseIfNotExist(budgetId, userId);
        Category category = categoryService.findById(request.categoryId(), userId);
        Transaction updated = updateTransaction(request, findOne(txId, budgetId), category);
        Transaction saved = transactionRepository.save(updated);
        this.updateTotals(budgetId, userId);
        return saved;
    }


    @Override
    public Transaction findOne(long txId, long budgetId) {
        return transactionRepository.findByIdAndBudgetId(txId, budgetId)
                .orElseThrow(() -> new AppException("Transaction not found"));
    }

    @Override
    public void delete(long txId, long budgetId, long userId) {
        log.info("Deleting Transaction txId: {} to Budget: {}, userId: {}", txId, budgetId, userId);
        budgetService.raiseIfNotExist(budgetId, userId);
        this.raiseIfNotExist(budgetId, userId);

    }
    @Override
    public List<TransactionDto> findByBudget(long budgetId, long userId) {
        return List.of();
    }

    @Override
    public List<TransactionDto> findByBudgetAndCategory(long budgetId, long categoryId, long userId) {
        return List.of();
    }

    @Override
    public void raiseIfNotExist(long id, long budgetId) {
        if(!transactionRepository.existsByIdAndBudgetId(id, budgetId)) {
            throw new AppException("Transaction not found");
        }
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
        Double totalIncome = transactionRepository.sumByBudgetIdAndCategoryType(budgetId, CategoryType.INCOME);
        Double totalExpense = transactionRepository.sumByBudgetIdAndCategoryType(budgetId, CategoryType.EXPENSE);
        budgetService.updateTotals(budgetId, totalIncome, totalExpense, userId);

    }
}
