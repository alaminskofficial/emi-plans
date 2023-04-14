package com.alamin.emi.utils;


import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.google.gson.Gson;
import com.alamin.emi.exceptions.InvalidRequestException;


@ControllerAdvice()
public class ExHandler{ 
	@Autowired
	ResponseHandler responseHandler;
	Gson gson = new Gson();
	
	
	   @ExceptionHandler(value = { Exception.class,InvalidRequestException.class})
	   public void exceptionHandle(Exception exception, HttpServletResponse response) throws Exception {
		Logging.getInfoLog().info("exception is: " + exception.getMessage());
		response.setContentType("application/json");
		StringBuilder mailText = new StringBuilder();
		try {
			if (exception instanceof Exception) {
				Logging.getInfoLog().info("*** Exception Email Alert Handling ***");
				Logging.getInfoLog().info("exception is: " + exception.getMessage());
	        //	Logging.getInfoLog().info("exception is: " +ExHandler.getStackTrace(exception));
				mailText.append("Exception trace: "+ ExHandler.getStackTrace(exception));
				if(exception instanceof HttpRequestMethodNotSupportedException) {
					Logging.getInfoLog().info(" return when exception is: " + exception.getMessage());
					return;
				}
				if(exception.getMessage().contains("not supported") || exception.getMessage().contains("Required request body is missing")) {
					responseHandler.respMessage(ResponseCodesAndStates.TxnStates.Failed.getValue(), ResponseCodesAndStates.TxnStates.Invalid_Request.getValue(), null, response);
				}
				else if(exception.getClass().getSimpleName().contains("MethodArgumentNotValidException")){
					responseHandler.respMessage(ResponseCodesAndStates.TxnStates.Failed.getValue(),
							ResponseCodesAndStates.TxnStates.MAND_PARAM_MISSING.getValue(), null, response);
				}
				else if(exception.getClass().getSimpleName().contains("InvalidRequestException") ||   (exception instanceof InvalidRequestException)) {
					responseHandler.respMessage(ResponseCodesAndStates.TxnStates.Failed.getValue(), ResponseCodesAndStates.TxnStates.SessionExpired.getValue(), null, response);
				}else if(exception.getMessage().contains("InternalServerError")){
					responseHandler.respMessage(ResponseCodesAndStates.TxnStates.Failed.getValue(), ResponseCodesAndStates.TxnStates.INTERNAL_SERVER_ERROR.getValue(), null, response);
				}else {
					responseHandler.respMessage(ResponseCodesAndStates.TxnStates.Failed.getValue(), ResponseCodesAndStates.TxnStates.INTERNAL_SERVER_ERROR.getValue(), null, response);
				}
				//emailSender.sendEmailDetails("Exception Alert |" + exception.getMessage(),mailText.toString(),Exceptions,null);
//				emailSender.sendTextMail(toAddress, ccAddress,mailText.toString(), "Exception Alert |" + exception.getMessage());
			}else {
				Logging.getInfoLog().info("Invalid exp ");
			}
		}catch (Exception e) {
			Logging.getErrorLog().error("{WARNING}Response will be overrided !!!!");
			Logging.getErrorLog().error(e.getStackTrace());
		}
		
	   }
	
	
	public synchronized static String getStackTrace(final Throwable throwable) {
		final StringWriter sw = new StringWriter();
		final PrintWriter pw = new PrintWriter(sw, true);
		throwable.printStackTrace(pw);
		return sw.getBuffer().toString();
	}
	
}
