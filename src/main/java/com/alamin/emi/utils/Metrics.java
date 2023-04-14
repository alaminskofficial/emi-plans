package com.alamin.emi.utils;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class Metrics {
	int reqCount = 0;
	int exceptionCount = 0;
	long minTime = 0, maxTime = 0, totalTime = 0, avgTime = 0;
	long lastLoggedTime = 0;
	long startTime = 0, endTime;
	String apiStartTime, apiEndTime, api = null;

}
