package com.alamin.emi.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alamin.emi.constants.EMIConstants;
import com.alamin.emi.dto.EMIPlansPayload;
import com.alamin.emi.dto.EMIPlansRequest;
import com.alamin.emi.dto.EMIPlansResponse;
import com.alamin.emi.entities.EMIProducts;
import com.alamin.emi.entities.EMITenures;
import com.alamin.emi.repository.EMIProductCRUDRepository;
import com.alamin.emi.repository.EMITenuresCRUDRepository;
import com.alamin.emi.utils.Logging;
import com.alamin.emi.utils.ResponseCodesAndStates;

@Service
public class EMIPlansService {
	@Autowired
	EMIProductCRUDRepository emiProductCRUDRepository;

	@Autowired
	EMITenuresCRUDRepository emiTenuresCRUDRepository;

	public EMIPlansResponse findEMIPlans(EMIPlansRequest eMIPlansRequest) {
		EMIPlansResponse response = new EMIPlansResponse();
		double transactionAmount = eMIPlansRequest.getTransactionAmount();
		List<EMITenures> tenures = emiTenuresCRUDRepository.findAll();
		List<EMIPlansPayload> listOfPlans = new ArrayList<>();
		try {
			tenures.forEach(tenure -> {
				double minAmount = tenure.getMinimumTransactionAmount();
				double maxAmount = tenure.getMaximumTransactionAmount();
				if (transactionAmount >= minAmount && transactionAmount <= maxAmount) {
					listOfPlans.add(getEMIPlansData(tenure, transactionAmount));
				}

			});

			if (CollectionUtils.isEmpty(listOfPlans)) {
				response.setStatus(ResponseCodesAndStates.TxnStates.Failed.getValue());
				response.setStatus(ResponseCodesAndStates.TxnStates.Failed.getValue());
				response.setErrorCode(ResponseCodesAndStates.getErrorCode().get(response.getRespMsg()));

			} else {
				response.setStatus(ResponseCodesAndStates.TxnStates.Success.getValue());
				response.setRespMsg(ResponseCodesAndStates.TxnStates.Success.getValue());
				response.setErrorCode(ResponseCodesAndStates.getErrorCode().get(response.getRespMsg()));
				response.setPlans(listOfPlans);
			}
		} catch (Exception e) {
			Logging.getErrorLog().error(Logging.getStackTrace(e));
			response.setStatus(ResponseCodesAndStates.TxnStates.Failed.getValue());
			response.setRespMsg(ResponseCodesAndStates.TxnStates.INTERNAL_SERVER_ERROR.getValue());
			response.setErrorCode(ResponseCodesAndStates.getErrorCode().get(response.getRespMsg()));
		}

		return response;
	}

	private EMIPlansPayload getEMIPlansData(EMITenures tenure, double transactionAmount) {
		EMIPlansPayload emiPlansPayload = new EMIPlansPayload();
		double monthlyRate = (tenure.getInterestRate() / 12) / 100;
		int numberOfPayments = 0;
	    if (EMIConstants.MONTHS.equalsIgnoreCase(tenure.getTenureType())) {
	        numberOfPayments = tenure.getTenure();
	    } else if (EMIConstants.DAYS.equalsIgnoreCase(tenure.getTenureType())) {
	        numberOfPayments = (tenure.getTenure()/30) * 12;
	    }
		double emi = (transactionAmount * monthlyRate * Math.pow(1 + monthlyRate, numberOfPayments))/ (Math.pow(1 + monthlyRate, numberOfPayments) - 1);
		
		setEmiPlanPayload(tenure, transactionAmount,numberOfPayments, emiPlansPayload, emi);
		return emiPlansPayload;
	}

	private void setEmiPlanPayload(EMITenures tenure, double transactionAmount, int numberOfPayments, EMIPlansPayload emiPlansPayload, double emi) {
		
		EMIProducts emiProduct = emiProductCRUDRepository.getProductById(tenure.getEmiProducts().getId());
		emiPlansPayload.setBrandName(emiProduct.getProductSubType());
		emiPlansPayload.setProductType(emiProduct.getProductType());
		
		emiPlansPayload.setInterestRate((Double.toString(tenure.getInterestRate())));
		
		double interestAmount = (emi * numberOfPayments) - transactionAmount;
		emiPlansPayload.setInterestAmount(Long.toString((long)interestAmount));
		
		emiPlansPayload.setEmi(Long.toString((long)emi));
		
		double processingFeeDouble = tenure.getProcessinFee();
		emiPlansPayload.setProcessingFee( Long.toString((long)processingFeeDouble));
		
		emiPlansPayload.setTenure(tenure.getTenure());
		emiPlansPayload.setTenureType(tenure.getTenureType());
		
		emiPlansPayload.setTransactionAmount(Long.toString((long)transactionAmount));
	}

}
