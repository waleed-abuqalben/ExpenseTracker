package com.waleed.expenseTracker.model.dto;

import com.waleed.expenseTracker.enums.BudgetStatus;
import lombok.Builder;
import lombok.Data;

import java.util.List;

import static com.waleed.expenseTracker.enums.BudgetStatus.ACTIVE;
@Data
@Builder
public class BudgetDto {
    private long id;
    private int year;
    private int month;
    private BudgetStatus status;
    private double totalIncome;
    private double totalExpense;
    private double netBalance;
    private List<TransactionDto> transactions;
    private long userId;
}
