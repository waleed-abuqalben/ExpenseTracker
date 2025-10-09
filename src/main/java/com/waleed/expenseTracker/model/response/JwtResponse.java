package com.waleed.expenseTracker.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public record JwtResponse (
		Long userId,
		String token
){}
	



