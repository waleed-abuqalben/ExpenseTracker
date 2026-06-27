package com.waleed.expenseTracker.model.event;

public record BudgetCreatedEvent(long budgetId, long userId) {}
