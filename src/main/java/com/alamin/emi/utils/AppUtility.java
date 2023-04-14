package com.alamin.emi.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.util.internal.ThreadLocalRandom;

@Configuration
public class AppUtility {
	public String getFileName(String documentType, String loginId, String extension) {
		return (new Date()).getTime() + "_" + documentType + "_" + (new Date()).getTime() + "_" + UUID.randomUUID()
				+ "." + extension;
	}

	public String getDate(String format) {
		Date date = new Date();
		String modifiedDate = new SimpleDateFormat(format).format(date);
		return modifiedDate;
	}

	public Map<String, String> getHeaders(HttpServletRequest httpRequest) {
		Map<String, String> headers = new HashMap<>();
		Enumeration<String> headerNames = httpRequest.getHeaderNames();
		if (headerNames != null) {
			while (headerNames.hasMoreElements()) {
				String name = headerNames.nextElement();
				headers.put(name, httpRequest.getHeader(name));
			}
		}
		return headers;
	}

	public InputStream decryptFile(String encodedFile) throws IllegalArgumentException {
		byte[] decodedBytes = Base64.getDecoder().decode(encodedFile);
		InputStream fileInputStream = new ByteArrayInputStream(decodedBytes);
		return fileInputStream;
	}

	public String getMimeType(String fileName) {
		return URLConnection.guessContentTypeFromName(fileName);
	}

	public static Object getValueFromJsonString(String jsonString, String key) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = mapper.readValue(jsonString, Map.class);
		Object result = map.get(key);
		return result;
	}

	public static boolean isValidMobile(String mobileNumber) {
		// 1) Begins with 0 or 91
		// 2) Then contains 7 or 8 or 9.
		// 3) Then contains 9 digits
		Pattern p = Pattern.compile("(0/91)?[6-9][0-9]{9}");

		Matcher m = p.matcher(mobileNumber);
		return (m.find() && m.group().equals(mobileNumber));
	}
	
	public static String getSHA256(String data) throws SignatureException {
		try {
			MessageDigest mac = MessageDigest.getInstance("SHA-256");
			mac.update(data.getBytes("UTF-8"));
			BigInteger bi = new BigInteger(1, mac.digest());
			return String.format("%0" + (mac.digest().length << 1) + "X", bi).toLowerCase();
		} catch (Exception e) {
			throw new SignatureException(e);
		}
	}
	
	public static synchronized String generateTxnid() {
		
		return ThreadLocalRandom.current().nextInt(1, 9999999)
				+ "" + System.currentTimeMillis(); 
	}
}
