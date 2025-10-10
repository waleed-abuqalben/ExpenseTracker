package com.waleed.expenseTracker.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryDto {
    private Long id;
    private String name;
    private String type;    // INCOME / EXPENSE as String
    private Long userId;    // nested User id
}
