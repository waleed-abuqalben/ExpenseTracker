package com.waleed.expenseTracker.model.request.transaction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record UpdateTransactionRequest(

        @NotBlank(message = "Transaction name required")
        String name,

        @NotNull(message = "Amount required")
        @Positive(message = "Amount should be positive")
        Double amount,

        String description,

        @NotNull(message = "Category id required")
        Long categoryId,


        LocalDate issuedAt,

        @NotNull(message = "budget id required")
        Long budgetId
) {
}
