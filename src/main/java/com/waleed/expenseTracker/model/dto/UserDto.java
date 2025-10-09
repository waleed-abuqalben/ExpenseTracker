package com.waleed.expenseTracker.model.dto;

public record UserDto(
    Long id,
    String username,
    String email
) { }
