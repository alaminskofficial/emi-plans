package com.alamin.emi.exceptions;

@SuppressWarnings("serial")
public class InvalidRequestException extends Exception {
	
	private String message;
	
	public InvalidRequestException(String message) {
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
	

}
