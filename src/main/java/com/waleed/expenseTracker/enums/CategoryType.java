package com.waleed.expenseTracker.enums;

import lombok.Getter;

@Getter
public enum CategoryType {
    INCOME, EXPENSE, SAVING;
    public static final String INVALID_MESSAGE = "Invalid Category Type";
}
