package com.alamin.emi.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class EMITenureCRUDRequest {
	
	   @ApiModelProperty(value = "minimumTransactionAmount", example = "1000")
	    private Integer minimumTransactionAmount;
	    
	    @ApiModelProperty(value = "maximumTransactionAmount", example = "50000")
	    private Integer maximumTransactionAmount;
	    
	    @ApiModelProperty(value = "tenure", example = "3")
	    private Integer tenure;
	    
	    @ApiModelProperty(value = "tenureType", example = "Months")
	    private String tenureType;
	    
	    @ApiModelProperty(value = "interestRate", example = "10.5")
	    private Double interestRate;
	    
	    @ApiModelProperty(value = "processingFee", example = "2.5")
	    private Double processingFee;
	    
	    @ApiModelProperty(value = "merchantFee", example = "5")
	    private Double merchantFee;
	    
	    @ApiModelProperty(value = "merchantFeeType", example = "FIXED")
	    private String merchantFeeType;
	    
	    @ApiModelProperty(value = "additionalField", example = "Field A")
	    private String additionalField;

}
