package com.waleed.expenseTracker.exception;

public class AppException extends RuntimeException{
	public AppException(String message) {
		super(message);
	}
	public static void raiseIf(boolean condition, String message) {
		if (condition) {raise(message);}
	}
	public static void raise(String message){
		throw new AppException(message);
	}

}
