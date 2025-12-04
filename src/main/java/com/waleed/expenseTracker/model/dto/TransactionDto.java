package com.waleed.expenseTracker.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
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

    private CategoryDto category;

    private LocalDate issuedAt;
}
