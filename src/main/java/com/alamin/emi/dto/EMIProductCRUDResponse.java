package com.alamin.emi.dto;

import org.springframework.http.HttpStatus;

import io.swagger.annotations.ApiModelProperty;

public class EMIProductCRUDResponse {
	@ApiModelProperty(name = "status",  required = true)
	public HttpStatus status;
	@ApiModelProperty(name = "data",  required = true)
	public Object data;

	public EMIProductCRUDResponse(HttpStatus ok, Object data) {
		super();
		this.status = ok;
		this.data = data;
	}
	
}
