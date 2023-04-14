package com.alamin.emi.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class EMIPlansRequest {
	@ApiModelProperty(name = "transactionAmount",  required = true)
    private Double transactionAmount;

}
