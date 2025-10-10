package com.waleed.expenseTracker.model.request.category;

import jakarta.validation.constraints.NotBlank;

public record CategoryUpdateNameRequest(
        @NotBlank(message = "New Category name is required")
        String updatedName
) {
}
