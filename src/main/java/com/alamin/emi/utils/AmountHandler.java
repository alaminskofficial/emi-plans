package com.alamin.emi.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;



public class AmountHandler {
	static DecimalFormat df = new DecimalFormat("#.##");
	
	@SuppressWarnings("deprecation")
	public static Integer getStringAmontToPaisa(String amount) {
		if(Double.valueOf(amount) > 1000000) {
			Logging.getInfoLog().info("Amont is more than 10Lakhs");
		}
		return Integer.valueOf(String.valueOf(BigDecimal.valueOf(Double.valueOf(amount)*100).setScale(0, BigDecimal.ROUND_HALF_UP).toBigInteger()));
	}
	
	public static String getAmountInPaisaAsString(Integer amount) {
		DecimalFormat df2 = new DecimalFormat("###0.00");
		return String.valueOf(df2.format(Double.valueOf(amount)/100));
	}
	public static void main(String[] args) {
		Logging.getInfoLog().info(getAmountInPaisaAsString(100));
	}
	
	public static String convertFromPaisaToRupee(String amount) {
		Integer inPaisa = Integer.valueOf(amount);
		double d = inPaisa / 100f;
		return String.valueOf(df.format(d));
	}
	
}
