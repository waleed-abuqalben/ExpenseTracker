package com.waleed.expenseTracker.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

public record ApiResponse<T> (
	 String message,
	 T data,
	 Instant timestamp

){
	public static <T> ApiResponse<T> of(String message, T data) {
		return new ApiResponse<>(message, data, Instant.now());
	}
}



