package com.alamin.emi.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ErrorMessage {
	private int code;
	private String status;
	private Date timestamp;
	private String message;

	public ErrorMessage(int statusCode, String status, Date timestamp, String message) {
		this.code = statusCode;
		this.status = status;
		this.timestamp = timestamp;
		this.message = message;
	}
}
