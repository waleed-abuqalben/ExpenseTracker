package com.waleed.expenseTracker.service.budget;

import com.waleed.expenseTracker.enums.BudgetStatus;
import com.waleed.expenseTracker.enums.CategoryType;
import com.waleed.expenseTracker.enums.Months;
import com.waleed.expenseTracker.exception.AppException;
import com.waleed.expenseTracker.model.entity.Budget;
import com.waleed.expenseTracker.model.entity.Transaction;
import com.waleed.expenseTracker.model.entity.User;
import com.waleed.expenseTracker.model.mappers.BudgetMapper;
import com.waleed.expenseTracker.model.request.budget.CreateBudgetRequest;
import com.waleed.expenseTracker.repository.BudgetRepository;
import com.waleed.expenseTracker.service.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BudgetServiceImpl implements BudgetService{
    private final BudgetRepository budgetRepository;
    private final UserService userService;
    private final BudgetMapper budgetMapper;


    @Value("${app.budget.min-year}")
    private  int MIN_YEAR;

    @Value("${app.budget.max-future-years}")
    private int MAX_FUTURE_YEARS;

    @Override
    public Budget findById(long id, long userId) {
        return budgetRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new AppException(String.format("Budget with id %d not found", id)));
    }

    @Override
    public List<Budget> findAll(long userId) {
        return budgetRepository.findByUserId(userId);
    }

    @Transactional
    @Override
    public Budget create(CreateBudgetRequest request, long userId) {
        log.info("Creating budget: {} for user: {}", request, userId);
        validateYear(request.year());
        if(budgetRepository.existsByYearAndMonthAndUserId(request.year(), request.month(), userId)) {
            throw new AppException(String.format("Budget %s/%d already exists",
                    Months.fromNumber(request.month()).getDisplayName(), request.year()));
        }
        User user = userService.getUserById(userId);
        Budget created = budgetRepository.save(budget(request, user));
        log.info("Budget created: {}", budgetMapper.toDto(created));
        return created;
    }

    @Transactional
    @Override
    public void updateTotals(long id, double totalIncome, double totalExpense, long userId) {
        Budget budget = findById(id, userId);
        budget.setTotalIncome(totalIncome);
        budget.setTotalExpense(totalExpense);
        budget.setNetBalance(totalIncome - totalExpense);
        budgetRepository.save(budget);
    }

    @Override
    public void raiseIfNotExist(long id, long userId) {
        if(!budgetRepository.existsByIdAndUserId(id, userId)) {
            throw new AppException(String.format("Budget with id %d not found", id));
        }
    }


    private void validateYear(int year) {
        int currentYear = Year.now().getValue();
        int maxAllowedYear = currentYear + MAX_FUTURE_YEARS;
        if(year < MIN_YEAR || year > maxAllowedYear){
            throw new AppException(String.format("Budget year %d, should between  %d and  %d", year, MIN_YEAR, maxAllowedYear));
        }
    }

    private static Budget budget(CreateBudgetRequest request, User user) {
        return Budget.builder()
                .month(request.month())
                .year(request.year())
                .status(BudgetStatus.ACTIVE)
                .user(user)
                .build();
    }


    private double totalExpense(List<Transaction> transactions) {
        return transactions.stream()
                .filter(tx -> tx.getCategory().getType() == CategoryType.EXPENSE)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    private double totalIncome(List<Transaction> transactions) {
        return transactions.stream()
                .filter(tx -> tx.getCategory().getType() == CategoryType.INCOME)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }
}
