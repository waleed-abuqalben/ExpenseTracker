package com.waleed.expenseTracker.model.response.auth;

import com.waleed.expenseTracker.enums.AuthenticationStatus;

public record LoginResponse(
        AuthenticationStatus status,
        String message,
        String token
) {
}
