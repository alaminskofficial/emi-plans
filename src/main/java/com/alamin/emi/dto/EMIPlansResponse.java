package com.alamin.emi.dto;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class EMIPlansResponse extends BaseResponse {
	@ApiModelProperty(name = "plans",  required = true)
	List<EMIPlansPayload> plans = new ArrayList<>();

}
