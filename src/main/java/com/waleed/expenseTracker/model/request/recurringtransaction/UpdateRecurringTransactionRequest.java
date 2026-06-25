package com.waleed.expenseTracker.model.request.recurringtransaction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateRecurringTransactionRequest(
        @NotBlank(message = "name is required")
        String name,

        @NotNull(message = "amount is required")
        @Positive(message = "amount should be positive")
        Double amount,

        String description,

        @NotNull(message = "categoryId is required")
        Long categoryId,

        boolean active
) {
}
