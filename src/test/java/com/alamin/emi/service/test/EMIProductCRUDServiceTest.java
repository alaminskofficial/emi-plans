package com.alamin.emi.service.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;

import com.alamin.emi.dto.EMIProductCRUDRequest;
import com.alamin.emi.dto.EMIProductListResponse;
import com.alamin.emi.entities.EMIProducts;
import com.alamin.emi.entities.EMITenures;
import com.alamin.emi.exceptions.EMIException;
import com.alamin.emi.repository.EMIProductCRUDRepository;
import com.alamin.emi.repository.EMITenuresCRUDRepository;
import com.alamin.emi.service.EMIProductCRUDService;

class EMIProductCRUDServiceTest {
	
	EMIProductCRUDService emiProductCRUDService = new EMIProductCRUDService();
	
	@Mock
	EMIProductCRUDRepository emiProductCRUDRepository;
	
	@Mock
	EMITenuresCRUDRepository emiTenuresCRUDRepository;
	
	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		Whitebox.setInternalState(emiProductCRUDService, "emiProductCRUDRepository", emiProductCRUDRepository);
		Whitebox.setInternalState(emiProductCRUDService, "emiTenuresCRUDRepository", emiTenuresCRUDRepository);
	}

	@Test
	void saveProductDetailsTest() {
		EMIProductCRUDRequest product = new EMIProductCRUDRequest();
		product.setProductType("EMI");
		product.setProductSubType("DCEMI");
		product.setEmiProvider("YESB");
		product.setApi("Dummy Api");
		product.setCredentials("Dummy");
		product.setCallbackUrl("CallBack");
		EMIProducts emiProduct = new EMIProducts();
		emiProduct.setProductType(product.getProductType());
		emiProduct.setProductSubType(product.getProductSubType());
		emiProduct.setEmiProvider(product.getEmiProvider());
		emiProduct.setApi(product.getApi());
		emiProduct.setCredentials(product.getCredentials());
		emiProduct.setCallbackUrl(product.getCallbackUrl());
		when(emiProductCRUDRepository.save(emiProduct)).thenReturn(emiProduct);
		emiProductCRUDService.saveProductDetails(product);
	}
	
	@Test
	void listProducts() {
		List<EMIProductListResponse> listData = new ArrayList<>();
		List<EMITenures> tenures = new ArrayList<>();
		EMITenures tenure = new EMITenures();
		tenure.setTenureType("Monthly");
		tenures.add(tenure);
		EMIProducts products = new EMIProducts();
		products.setId(123);
		tenure.setEmiProducts(products);
		EMIProductListResponse emiProductResponse = new EMIProductListResponse();
		when(emiTenuresCRUDRepository.findAll()).thenReturn(tenures);
		when(emiProductCRUDRepository.getProductById(tenure.getEmiProducts().getId())).thenReturn(products);
		emiProductResponse.setTenureType(tenure.getTenureType());
		listData.add(emiProductResponse);
		assertEquals(listData, emiProductCRUDService.listProducts());
	}
	
	@Test
	void updateEMIProductTest() throws EMIException {
		EMIProductCRUDRequest product = new EMIProductCRUDRequest();
		EMITenures emiTenures = new EMITenures();
		when(emiTenuresCRUDRepository.getTenureByProductId(123)).thenReturn(emiTenures);
		EMIProducts emiProduct = new EMIProducts();
		when(emiProductCRUDRepository.getProductById(123)).thenReturn(emiProduct);
		when(emiProductCRUDRepository.save(emiProduct)).thenReturn(emiProduct);
		emiTenuresCRUDRepository.save(emiTenures);
		emiProductCRUDService.updateEMIProduct(123, product);
	}
	
	@Test
	void updateEMIProductTestForNullValue() throws EMIException {
		EMIProductCRUDRequest product = new EMIProductCRUDRequest();
		EMITenures emiTenures = null;
		when(emiTenuresCRUDRepository.getTenureByProductId(123)).thenReturn(emiTenures);
		assertThrows(EMIException.class, () ->emiProductCRUDService.updateEMIProduct(123, product));
	}
	
	@Test
	void viewEMIProductTest() {
		int id = 123;
		EMIProductListResponse emiProductResponse = new EMIProductListResponse();
		EMIProducts emiProduct = new EMIProducts();
		when(emiProductCRUDRepository.getProductById(id)).thenReturn(emiProduct);
		EMITenures emiTenures = new EMITenures();
		when(emiTenuresCRUDRepository.getTenureByProductId(id)).thenReturn(emiTenures);
		assertEquals(emiProductResponse, emiProductCRUDService.viewEMIProduct(id));
	}
	
	@Test
	void deleteEMIProductTest() {
		int id = 123;
		assertEquals(null, emiProductCRUDService.deleteEMIProduct(id));
	}

}
