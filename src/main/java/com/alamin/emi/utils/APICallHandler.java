package com.alamin.emi.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.stereotype.Component;

@Component
public class APICallHandler {

	public synchronized String processHttpPost(String URL, String urlParameters, String contentType) throws Exception {
		Logging.getInfoLog().info("URL :" + URL + "\nParams :" + urlParameters);
		int timeOut = 25000;
		StringBuffer urlResponse = new StringBuffer("");
		URL url = null;
		HttpURLConnection conn = null;
		OutputStream os = null;
		try {
			url = new URL(URL);
			conn = (HttpURLConnection) url.openConnection();
			if (contentType != null) {
				conn.setRequestProperty("Content-Type", contentType);
			}
			conn.setConnectTimeout(timeOut);
			conn.setReadTimeout(timeOut);
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			os = conn.getOutputStream();
			if (urlParameters != null && !urlParameters.equals("")) {
				os.write(urlParameters.getBytes());
			}
			Logging.getInfoLog().info("Received response code : " + conn.getResponseCode());
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output = "";
			while ((output = br.readLine()) != null) {
				urlResponse.append(output.trim());
			}
			conn.disconnect();
		} catch (Exception e) {
			Logging.getInfoLog().info("Error while fetching transaction !");
			Logging.getErrorLog().error(Logging.getStackTrace(e));
			throw e;
		} finally {
			try {
				if (os != null)
					os.flush();
			} catch (IOException e) {
				Logging.getErrorLog().error(Logging.getStackTrace(e));
			}
		}
		Logging.getInfoLog().info("Response from PG : " + urlResponse);
		if (urlResponse.toString().trim().equalsIgnoreCase("")
				|| (urlResponse != null && urlResponse.toString().contains("html>"))) {
			Logging.getInfoLog().info("Invalid response received !");
			return null;
		}
		return urlResponse.toString();
	}
}
