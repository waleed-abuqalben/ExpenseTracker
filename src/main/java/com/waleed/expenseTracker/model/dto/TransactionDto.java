package com.waleed.expenseTracker.model.dto;

import com.waleed.expenseTracker.enums.CategoryType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class TransactionDto {
    private Long id;
    private String name;
    private double amount;
    private String description;
    private CategoryType categoryType;
    private String categoryName;
    private LocalDate issuedAt;
}
