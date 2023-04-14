package com.alamin.emi.session.services;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class SessionData implements Serializable{
	
	private String loginId;
	private String loginType;
	private String deviceId;
	private boolean authStatus;
	private String appVersion;
	private List<Map<String, TidInfo>> tidList;

}
