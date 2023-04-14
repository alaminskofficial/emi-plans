package com.alamin.emi.exceptions;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;

@Data
public class EMIException extends Exception implements Serializable {

	private static final long serialVersionUID = 5235445288067527596L;

	private int customStatusCode;
	private String customMessage = StringUtils.EMPTY;
	private Throwable e;

	public EMIException(String message) {
		super(message);
	}

	public EMIException(String message, Throwable e) {
		super(message, e);
	}

	public EMIException(Throwable e) {
		super(e);
	}
	
	public EMIException(int statusCode, String message) {
		this.customStatusCode = statusCode;
		this.customMessage = message;
	}

	public EMIException(int statusCode, String message, Throwable e) {
		this.customStatusCode = statusCode;
		this.customMessage = message;
		this.e = e;
	}

}
