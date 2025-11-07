package com.waleed.expenseTracker.model.request.category;

import jakarta.validation.constraints.NotBlank;

public record UpdateCategoryNameRequest(
        @NotBlank(message = "New Category name is required")
        String name
) {
}
