package com.waleed.expenseTracker.model.request.auth;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Wrong Username/Password")
        String email,
        @NotBlank(message = "Wrong Username/Password")
        String password
) {
}
