package com.waleed.expenseTracker.model.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        String username,

        @Email(message = "Invalid email format")
        String email,

        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = "Password must be at least 8 characters long, contain one lowercase, one uppercase, one digit, and one special character (@$!%*?&)."
        )
        String password
) {
}