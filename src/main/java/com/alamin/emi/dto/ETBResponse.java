package com.alamin.emi.dto;

public class ETBResponse {

	private String type;
	private String status;
	private String responseString;
	private String ResponseEncryptedValue;
	private String ResponseSignatureEncryptedValue;
	private String GWSymmetricKeyEncryptedValue;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getResponseString() {
		return responseString;
	}

	public void setResponseString(String responseString) {
		this.responseString = responseString;
	}

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

}
