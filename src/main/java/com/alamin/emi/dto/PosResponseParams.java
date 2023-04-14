package com.alamin.emi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class PosResponseParams {
	
	@ApiModelProperty(example = "status", allowEmptyValue = false, value = "status")
	private String status;
	@ApiModelProperty(example = "respMsg", allowEmptyValue = false, value = "Response Msg")
	private String respMsg;
	private String errorCode;

}
