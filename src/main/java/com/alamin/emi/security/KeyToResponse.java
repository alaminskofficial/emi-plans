package com.alamin.emi.security;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class KeyToResponse {

	@JsonProperty
	private String appName;
	@JsonProperty
	private String pubKey;
	@JsonProperty
	private String priKey;
	@JsonProperty
	private String status;

	@Override
	public String toString() {
		return "KeyToResponse [appName=" + appName + ", pubKey=" + pubKey + ", priKey=" + priKey + "]";
	}

}
