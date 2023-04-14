package com.alamin.emi.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alamin.emi.dto.EMIProductCRUDRequest;
import com.alamin.emi.dto.EMIProductListResponse;
import com.alamin.emi.dto.EMITenureCRUDRequest;
import com.alamin.emi.entities.EMIProducts;
import com.alamin.emi.entities.EMITenures;
import com.alamin.emi.exceptions.EMIException;
import com.alamin.emi.repository.EMIProductCRUDRepository;
import com.alamin.emi.repository.EMITenuresCRUDRepository;

@Service
public class EMIProductCRUDService {
	
	@Autowired
	EMIProductCRUDRepository emiProductCRUDRepository;
	
	@Autowired
	EMITenuresCRUDRepository emiTenuresCRUDRepository;

	public void saveProductDetails(EMIProductCRUDRequest product) {
		EMIProducts saveProductData = saveProductData(product);
		saveTenureData(product, saveProductData);
	}

	public List<EMIProductListResponse> listProducts() {
		List<EMIProductListResponse> listData = new ArrayList<>();
		List<EMITenures> tenures = emiTenuresCRUDRepository.findAll();
		tenures.forEach(tenure ->{
			listData.add(getEMIProductListData(tenure));
		});
		return listData;
	}
	
	public void updateEMIProduct(int id, EMIProductCRUDRequest product) throws EMIException {
		EMITenures emiTenures = emiTenuresCRUDRepository.getTenureByProductId(id);
		if (ObjectUtils.isEmpty(emiTenures)) {
			throw new EMIException("Product Not Found");
		}
		 editEMIProductListData(id , product);
	}
	
	private void editEMIProductListData(int id, EMIProductCRUDRequest editData) {
		EMIProducts emiProduct = emiProductCRUDRepository.getProductById(id);
		EMIProducts saveEmiProduct = saveEMIProduct(editData, emiProduct);
		EMITenures emiTenures = emiTenuresCRUDRepository.getTenureByProductId(id);
		saveEMITenureData(editData, saveEmiProduct, emiTenures);
		
	}
	public EMIProductListResponse deleteEMIProduct(int id) {
		return null;
	}

	public EMIProductListResponse viewEMIProduct(int id) {
		EMIProductListResponse emiProductResponse = new EMIProductListResponse();
		EMIProducts emiProduct = emiProductCRUDRepository.getProductById(id);
		EMITenures emiTenures = emiTenuresCRUDRepository.getTenureByProductId(id);
		modifiedProductResponseData(emiTenures, emiProductResponse, emiProduct);
		return emiProductResponse;
	}

	private EMIProductListResponse getEMIProductListData(EMITenures tenure) {
		EMIProductListResponse emiProductResponse = new EMIProductListResponse();
		EMIProducts getProduct = emiProductCRUDRepository.getProductById(tenure.getEmiProducts().getId());
		modifiedProductResponseData(tenure, emiProductResponse, getProduct);
		
		return emiProductResponse;
	}
	
	private EMIProducts saveProductData(EMIProductCRUDRequest product) {
		EMIProducts emiProduct = new EMIProducts();
		EMIProducts saveEmiProduct = saveEMIProduct(product, emiProduct);
		return saveEmiProduct;
	}
	
	private void saveTenureData(EMIProductCRUDRequest product , EMIProducts saveProductData) {
		EMITenures emiTenures = new EMITenures();
		saveEMITenureData(product, saveProductData, emiTenures);
	}
	
	private void saveEMITenureData(EMIProductCRUDRequest product, EMIProducts saveProductData, EMITenures emiTenures) {
		List<EMITenureCRUDRequest> tenures = product.getEmiTenures();
		tenures.forEach(tenure -> { saveEMITenuresData(tenure ,saveProductData);} );
	}
	
	private void saveEMITenuresData(EMITenureCRUDRequest tenure, EMIProducts saveProductData) {
		EMITenures emiTenures = new EMITenures();
		emiTenures.setEmiProducts(saveProductData);
		emiTenures.setMinimumTransactionAmount(tenure.getMinimumTransactionAmount());
		emiTenures.setMaximumTransactionAmount(tenure.getMaximumTransactionAmount());
		emiTenures.setTenure(tenure.getTenure());
		emiTenures.setTenureType(tenure.getTenureType());
		emiTenures.setInterestRate(tenure.getInterestRate());
		emiTenures.setProcessinFee(tenure.getProcessingFee());
		emiTenures.setMerchantFee(tenure.getMerchantFee());
		emiTenures.setMerchantFeeType(tenure.getMerchantFeeType());
		emiTenures.setAdditionalField(tenure.getAdditionalField());
		emiTenuresCRUDRepository.save(emiTenures);
		
	}
	private void modifiedProductResponseData(EMITenures tenure, EMIProductListResponse emiProductResponse,EMIProducts getProduct) {
		emiProductResponse.setProductType(getProduct.getProductType());
		emiProductResponse.setProductSubType(getProduct.getProductSubType());
		emiProductResponse.setEmiProvider(getProduct.getEmiProvider());
		emiProductResponse.setApi(getProduct.getApi());
		emiProductResponse.setCredentials(getProduct.getCredentials());
		emiProductResponse.setCallbackUrl(getProduct.getCallbackUrl());
		emiProductResponse.setMinimumTransactionAmount(tenure.getMinimumTransactionAmount());
		emiProductResponse.setMaximumTransactionAmount(tenure.getMaximumTransactionAmount());
		emiProductResponse.setTenure(tenure.getTenure());
		emiProductResponse.setTenureType(tenure.getTenureType());
		emiProductResponse.setInterestRate(tenure.getInterestRate());
		emiProductResponse.setProcessinFee(tenure.getProcessinFee());
		emiProductResponse.setMerchantFee(tenure.getMerchantFee());
		emiProductResponse.setMerchantFeeType(tenure.getMerchantFeeType());
		emiProductResponse.setAdditionalField(tenure.getAdditionalField());
	}

	private EMIProducts saveEMIProduct(EMIProductCRUDRequest product, EMIProducts emiProduct) {
		emiProduct.setProductType(product.getProductType());
		emiProduct.setProductSubType(product.getProductSubType());
		emiProduct.setEmiProvider(product.getEmiProvider());
		emiProduct.setApi(product.getApi());
		emiProduct.setCredentials(product.getCredentials());
		emiProduct.setCallbackUrl(product.getCallbackUrl());
		EMIProducts saveEmiProduct = emiProductCRUDRepository.save(emiProduct);
		return saveEmiProduct;
	}

	

}
