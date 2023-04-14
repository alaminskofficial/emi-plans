package com.alamin.emi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.alamin.emi.dto.EMIPlansRequest;
import com.alamin.emi.dto.EMIPlansResponse;
import com.alamin.emi.service.EMIPlansService;
import com.alamin.emi.utils.Logging;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ExampleProperty;

@RestController
public class EMIPlansController {
	@Autowired
	EMIPlansService eMIPlansService;
	
	@ApiOperation(value = "This is the EMI Plans API ")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "EMI Plans Response as below", response = EMIPlansResponse.class, examples = @io.swagger.annotations.Example(value = {
					@ExampleProperty(mediaType = "Success Response", value = "{\"status\": \"Success\", \"errorCode\": \"S101\", \"respMsg\": \"Success\", \"plans\" : []}"),
					@ExampleProperty(mediaType = "Failed Response", value = "{\"status\": \"Failed\", \"errorCode\": \"S102\", \"respMsg\": \"Failed\", \"plans\" : null}") })),
			@ApiResponse(code = 500, message = "Internal server error", examples = @io.swagger.annotations.Example(value = {
					@ExampleProperty(value = "{\n \"Status\": \"Failed\",\n \"respMsg\": \"Sorry! Some internal Server Error occured, Please try after some time\",\n \"errorCode\": \"E101\"\n}", mediaType = "application/json") })) })
	
	@PostMapping(value = "/plans", produces = { "application/json" }, consumes = { "application/json" })
	public ResponseEntity<EMIPlansResponse> getEMIPlans(@RequestBody EMIPlansRequest eMIPlansRequest){
		EMIPlansResponse emiResponse = new EMIPlansResponse();
		try {
			emiResponse = eMIPlansService.findEMIPlans(eMIPlansRequest);
		} catch (Exception e) {
			Logging.getErrorLog().error(Logging.getStackTrace(e));
			throw e;
		}
		
		return new ResponseEntity<>(emiResponse, HttpStatus.OK);
		
	}

	
	

}
