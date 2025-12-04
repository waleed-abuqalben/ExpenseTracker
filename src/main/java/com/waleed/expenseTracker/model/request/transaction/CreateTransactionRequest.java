package com.waleed.expenseTracker.model.request.transaction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record CreateTransactionRequest(
    @NotBlank(message = "name is required")
    String name,

    @Positive(message = "amount should be positive")
    Double amount,

    String description,

    @NotNull(message = "categoryId is required")
    Long categoryId,

    LocalDate issuedAt,

    @NotNull(message = "budget id required")
    Long budgetId
) {
}
