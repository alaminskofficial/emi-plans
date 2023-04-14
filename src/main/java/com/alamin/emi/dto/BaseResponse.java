package com.alamin.emi.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BaseResponse {

	@ApiModelProperty(name = "status", example = "[\"Success\", \"Failed\"]", required = true)
	private String status;
	@ApiModelProperty(name = "errorCode", example = "[\"S101\", \"YBL002\",\"YBL003\"]", required = true)
	private String errorCode;
	@ApiModelProperty(name = "respMsg", example = "[\"Success\", \"Failure Response received from bank\",\"No Response received from bank\",\"error in sending request to bank\"]", required = true)
	private String respMsg;

}
