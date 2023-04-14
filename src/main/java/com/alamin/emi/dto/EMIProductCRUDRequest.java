package com.alamin.emi.dto;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class EMIProductCRUDRequest {
	
	@ApiModelProperty(value = "productType", required = true)
    private String productType;
    
    @ApiModelProperty(value = "productSubType", required = true)
    private String productSubType;
    
    @ApiModelProperty(value = "emiProvider", required = true)
    private String emiProvider;
    
    @ApiModelProperty(value = "api", required = true)
    private String api;
    
    @ApiModelProperty(value = "credentials", required = true)
    private String credentials;
    
    @ApiModelProperty(value = "callbackUrl", required = true)
    private String callbackUrl;
    
    @ApiModelProperty(value = "emiTenures", required = true)
    private List<EMITenureCRUDRequest> emiTenures;


}
