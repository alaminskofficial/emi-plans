package com.alamin.emi.exceptions;

@SuppressWarnings("serial")
public class InternalServerError extends Exception {
	private String message;

	public InternalServerError(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

}
