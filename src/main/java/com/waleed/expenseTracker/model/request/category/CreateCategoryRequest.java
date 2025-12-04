package com.waleed.expenseTracker.model.request.category;

import com.waleed.expenseTracker.annotation.enumValue.EnumValue;
import com.waleed.expenseTracker.enums.CategoryType;
import jakarta.validation.constraints.NotBlank;

public record CreateCategoryRequest(
        @NotBlank(message = "Category name required")
        String name,

        @EnumValue(enumClass = CategoryType.class, message = CategoryType.INVALID_MESSAGE)
        String type
) {

}
