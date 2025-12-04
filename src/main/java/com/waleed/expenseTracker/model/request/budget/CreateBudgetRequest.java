package com.waleed.expenseTracker.model.request.budget;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record CreateBudgetRequest(

        @Min(1) @Max(12)
        int month,

        int year
) {
}
