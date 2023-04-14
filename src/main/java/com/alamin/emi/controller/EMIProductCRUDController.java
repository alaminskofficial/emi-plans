package com.alamin.emi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.alamin.emi.dto.EMIProductCRUDRequest;
import com.alamin.emi.exceptions.EMIException;
import com.alamin.emi.dto.EMIProductCRUDResponse;
import com.alamin.emi.dto.EMIProductListResponse;
import com.alamin.emi.service.EMIProductCRUDService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ExampleProperty;

@RestController
public class EMIProductCRUDController {
	@Autowired
	EMIProductCRUDService emiProductCRUDService;
	
	@ApiOperation(value = "This is the EMI product CRUD API ")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Pre Approved Response as below", response = EMIProductCRUDResponse.class, examples = @io.swagger.annotations.Example(value = {
					@ExampleProperty(mediaType = "Success Response", value = "{\"status\": \"Success\", \"errorCode\": \"S101\", \"respMsg\": \"Success\", \"preApprovedProducts\" : [\"DCEMI\", \"Flexipay\"]}"),
					@ExampleProperty(mediaType = "Failed Response", value = "{\"status\": \"Failed\", \"errorCode\": \"S102\", \"respMsg\": \"Failed\", \"preApprovedProducts\" : null}") })),
			@ApiResponse(code = 500, message = "Internal server error", examples = @io.swagger.annotations.Example(value = {
					@ExampleProperty(value = "{\n \"Status\": \"Failed\",\n \"respMsg\": \"Sorry! Some internal Server Error occured, Please try after some time\",\n \"errorCode\": \"E101\"\n}", mediaType = "application/json") })) })

	@PostMapping(value = "/add-product", produces = { "application/json" }, consumes = { "application/json" })
	public ResponseEntity<EMIProductCRUDResponse> addEMIProduct(@RequestBody EMIProductCRUDRequest product)  {
		
		emiProductCRUDService.saveProductDetails(product);
		return new ResponseEntity<>(new EMIProductCRUDResponse(HttpStatus.OK, "Product saved successfully"), HttpStatus.OK);
	}
	
	@GetMapping(value = "/list-product", produces = { "application/json" })
	public ResponseEntity<EMIProductCRUDResponse> listEMIProduct()  {
		
		List<EMIProductListResponse> listProducts =  emiProductCRUDService.listProducts();
		return new ResponseEntity<>(new EMIProductCRUDResponse(HttpStatus.OK, listProducts), HttpStatus.OK);
	}
	@PostMapping(value ="/edit-product/{id}" ,produces = { "application/json" }, consumes = { "application/json" })
    public ResponseEntity<EMIProductCRUDResponse> editProduct(@PathVariable int id ,@RequestBody EMIProductCRUDRequest product) throws EMIException {
		
		emiProductCRUDService.updateEMIProduct(id ,product);
		return new ResponseEntity<>(new EMIProductCRUDResponse(HttpStatus.OK, "Successfully Edited!"), HttpStatus.OK);
    }
	@GetMapping(value ="/delete-product/{id}" ,produces = { "application/json" })
    public ResponseEntity<EMIProductCRUDResponse> deleteProduct(@PathVariable int id) {
		
		emiProductCRUDService.deleteEMIProduct(id);
		return new ResponseEntity<>(new EMIProductCRUDResponse(HttpStatus.OK, "Successfully Deleted!"), HttpStatus.OK);
    }
	@GetMapping(value ="/view-product/{id}" ,produces = { "application/json" })
    public ResponseEntity<EMIProductListResponse> viewProduct(@PathVariable int id) {
		
		EMIProductListResponse updatedProduct = emiProductCRUDService.viewEMIProduct(id);
        return ResponseEntity.ok(updatedProduct);
    }
}
