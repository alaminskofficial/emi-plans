package com.alamin.emi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class EMIProductListResponse {
	@JsonProperty( "productType")
    private String productType;
	@JsonProperty( "productSubType")
	private String productSubType;
	@JsonProperty( "emiProvider")
	private String emiProvider;
	@JsonProperty( "api")
	private String api;
	@JsonProperty( "credentials")
	private String credentials;
	@JsonProperty( "callbackUrl")
	private String callbackUrl;
	
	@JsonProperty( "minimumTransactionAmount")
	private Integer minimumTransactionAmount;
	@JsonProperty( "maximumTransactionAmount")
	private Integer maximumTransactionAmount;
	@JsonProperty( "tenure")
	private Integer tenure;
	@JsonProperty( "tenureType")
	private String tenureType;
	@JsonProperty( "interestRate")
	private Double interestRate;
	@JsonProperty( "processinFee")
	private Double processinFee;
	@JsonProperty( "merchantFee")
	private Double merchantFee;
	@JsonProperty( "merchantFeeType")
	private String merchantFeeType;
	@JsonProperty( "additionalField")
	private String additionalField;

}
