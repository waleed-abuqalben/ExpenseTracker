package com.waleed.expenseTracker.model.dto;

import com.waleed.expenseTracker.enums.CategoryType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecurringTransactionDto {
    private Long id;
    private String name;
    private double amount;
    private String description;
    private boolean active;
    private Long categoryId;
    private String categoryName;
    private CategoryType categoryType;
}
