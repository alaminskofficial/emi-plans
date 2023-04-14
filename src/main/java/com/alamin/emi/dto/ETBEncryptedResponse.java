package com.alamin.emi.dto;

import org.springframework.stereotype.Component;

public class ETBEncryptedResponse {
	private String ResponseEncryptedValue;
	private String ResponseSignatureEncryptedValue;
	private String GWSymmetricKeyEncryptedValue;
	private String Scope;
	private String TransactionId;
	private String Status;
	public String getResponseEncryptedValue() {
		return ResponseEncryptedValue;
	}
	public void setResponseEncryptedValue(String responseEncryptedValue) {
		ResponseEncryptedValue = responseEncryptedValue;
	}
	public String getResponseSignatureEncryptedValue() {
		return ResponseSignatureEncryptedValue;
	}
	public void setResponseSignatureEncryptedValue(String responseSignatureEncryptedValue) {
		ResponseSignatureEncryptedValue = responseSignatureEncryptedValue;
	}
	public String getGWSymmetricKeyEncryptedValue() {
		return GWSymmetricKeyEncryptedValue;
	}
	public void setGWSymmetricKeyEncryptedValue(String gWSymmetricKeyEncryptedValue) {
		GWSymmetricKeyEncryptedValue = gWSymmetricKeyEncryptedValue;
	}
	public String getScope() {
		return Scope;
	}
	public void setScope(String scope) {
		Scope = scope;
	}
	public String getTransactionId() {
		return TransactionId;
	}
	public void setTransactionId(String transactionId) {
		TransactionId = transactionId;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}

	
	
}
