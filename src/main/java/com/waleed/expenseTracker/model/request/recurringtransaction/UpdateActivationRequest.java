package com.waleed.expenseTracker.model.request.recurringtransaction;

import jakarta.validation.constraints.NotNull;

public record UpdateActivationRequest(
        @NotNull(message = "active is required")
        Boolean active
) {
}
