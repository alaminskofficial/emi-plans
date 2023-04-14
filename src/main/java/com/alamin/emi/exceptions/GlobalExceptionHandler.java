package com.alamin.emi.exceptions;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.alamin.emi.constants.EMIConstants;
import com.alamin.emi.dto.ErrorMessage;
import com.alamin.emi.helper.CustomMessages;
import com.alamin.emi.helper.Loggable;

@ControllerAdvice
public class GlobalExceptionHandler {

	@Autowired
	private CustomMessages customMessages;

	@ExceptionHandler(value = { EMIException.class })

	@Loggable
	public ResponseEntity<ErrorMessage> EMIException(EMIException ex) {
		HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		String msg = ex.getMessage();
		if (customMessages.get(ex.getCustomStatusCode()) != null) {
			msg = ex.getCustomMessage() + " : " + customMessages.get(ex.getCustomStatusCode());
		}
		if (ex.getCustomStatusCode() > 5000) {
			httpStatus = HttpStatus.OK;
		}
		ErrorMessage message = new ErrorMessage(ex.getCustomStatusCode(), EMIConstants.ERROR.toString(), new Date(),
				msg);
		return new ResponseEntity<>(message, null, httpStatus);
	}
}
