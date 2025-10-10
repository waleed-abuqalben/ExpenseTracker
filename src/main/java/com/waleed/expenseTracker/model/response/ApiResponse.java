package com.waleed.expenseTracker.model.response;

import java.time.Instant;

public record ApiResponse<T> (
	 String message,
	 T data,
	 Instant timestamp

){
	public static <T> ApiResponse<T> of(String message, T data) {
		return new ApiResponse<>(message, data, Instant.now());
	}
	public static <T> ApiResponse<T> of(T data) {
		return new ApiResponse<>(null, data, Instant.now());
	}

}



