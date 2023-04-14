package com.alamin.emi.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@Data
public class EMIPlansPayload {
	
	@ApiModelProperty(name = "productType",  required = true)
	private String productType;
	@ApiModelProperty(name = "brandName",  required = true)
    private String brandName;
	@ApiModelProperty(name = "transactionAmount",  required = true)
    private String transactionAmount;
	@ApiModelProperty(name = "tenure",  required = true)
    private int tenure;
	@ApiModelProperty(name = "tenureType",  required = true)
    private String tenureType;
	@ApiModelProperty(name = "interestRate",  required = true)
    private String interestRate;
	@ApiModelProperty(name = "interestAmount",  required = true)
    private String interestAmount;
	@ApiModelProperty(name = "emi",  required = true)
    private String emi;
	@ApiModelProperty(name = "processingFee",  required = true)
    private String processingFee;

}
