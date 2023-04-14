package com.alamin.emi.utils;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingUtility {

	private static final Logger LOGGER = LogManager.getLogger(LoggingUtility.class);

	@Around("@annotation(com.alamin.emi.helper.Loggable)")
	public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
		LOGGER.debug("==============================================================");
		LOGGER.debug("Enter: {}.{}() with argument[s] = {}", joinPoint.getSignature().getDeclaringTypeName(),
				joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
		LOGGER.debug("==============================================================");

		Object result = joinPoint.proceed();
		LOGGER.debug("Exit: {}.{}() with result = {}", joinPoint.getSignature().getDeclaringTypeName(),
				joinPoint.getSignature().getName(), result);
		LOGGER.debug("==============================================================");
		return result;

	}

}
