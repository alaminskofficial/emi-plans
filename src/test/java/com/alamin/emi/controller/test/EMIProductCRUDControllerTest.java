package com.alamin.emi.controller.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.alamin.emi.controller.EMIProductCRUDController;
import com.alamin.emi.dto.EMIProductCRUDRequest;
import com.alamin.emi.dto.EMIProductCRUDResponse;
import com.alamin.emi.dto.EMIProductListResponse;
import com.alamin.emi.exceptions.EMIException;
import com.alamin.emi.service.EMIProductCRUDService;

class EMIProductCRUDControllerTest {
	
	EMIProductCRUDController eMIProductCRUDController = new EMIProductCRUDController();

	@Mock
	EMIProductCRUDService emiProductCRUDService;
	
	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		Whitebox.setInternalState(eMIProductCRUDController, "emiProductCRUDService", emiProductCRUDService);
	}
	
	@Test
	void addEMIProductTest() {
		EMIProductCRUDRequest product = new EMIProductCRUDRequest();
		emiProductCRUDService.saveProductDetails(product);
		ResponseEntity<EMIProductCRUDResponse> response = eMIProductCRUDController.addEMIProduct(product);
		assertEquals(HttpStatus.OK, response.getStatusCode());		
	}

	@Test
	void listEMIProductTest() {
		List<EMIProductListResponse> response = new ArrayList<>();
		when(emiProductCRUDService.listProducts()).thenReturn(response);
		ResponseEntity<EMIProductCRUDResponse> responses = eMIProductCRUDController.listEMIProduct();
		assertEquals(HttpStatus.OK, responses.getStatusCode());
		
	}
	
	@Test
	void editProductTest() throws EMIException {
		EMIProductCRUDRequest product = new EMIProductCRUDRequest();
		int id = 12345;
		emiProductCRUDService.updateEMIProduct(id, product);
		ResponseEntity<EMIProductCRUDResponse> responses = eMIProductCRUDController.editProduct(id,product);
		assertEquals(HttpStatus.OK, responses.getStatusCode());
		
	}
	
	@Test
	void deleteProductTest() throws EMIException {
		int id = 12345;
		emiProductCRUDService.deleteEMIProduct(id);
		ResponseEntity<EMIProductCRUDResponse> responses = eMIProductCRUDController.deleteProduct(id);
		assertEquals(HttpStatus.OK, responses.getStatusCode());
		
	}
	
	@Test
	void viewProductTest() throws EMIException {
		int id = 12345;
		EMIProductListResponse updatedProduct = new EMIProductListResponse();
		when(emiProductCRUDService.viewEMIProduct(id)).thenReturn(updatedProduct);
		ResponseEntity<EMIProductListResponse> responses = eMIProductCRUDController.viewProduct(id);
		assertEquals(HttpStatus.OK, responses.getStatusCode());
		
	}
}
