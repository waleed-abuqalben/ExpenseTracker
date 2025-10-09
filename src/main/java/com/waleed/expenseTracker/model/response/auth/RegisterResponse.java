package com.waleed.expenseTracker.model.response.auth;

public record RegisterResponse(
        Long userId,
        String username,
        String email,
        String token
) {
}
