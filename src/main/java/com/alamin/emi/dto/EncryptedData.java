package com.alamin.emi.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EncryptedData {
	@NotNull
	@NotBlank
	private String KEY;
	@NotNull
	@NotBlank
	private String IV;
	@NotNull
	@NotBlank
	private String PAYLOAD;
	
	@JsonProperty("KEY")
	public String getKEY() {
		return KEY;
	}
	@JsonProperty("KEY")
	public void setKEY(String kEY) {
		KEY = kEY;
	}
	public String getIV() {
		return IV;
	}
	@JsonProperty("IV")
	public void setIV(String iV) {
		IV = iV;
	}
	public String getPAYLOAD() {
		return PAYLOAD;
	}
	@JsonProperty("PAYLOAD")
	public void setPAYLOAD(String pAYLOAD) {
		PAYLOAD = pAYLOAD;
	}
	@Override
	public String toString() {
		return "EncryptedData [KEY=" + KEY + ", IV=" + IV + ", PAYLOAD=" + PAYLOAD + "]";
	}
}
