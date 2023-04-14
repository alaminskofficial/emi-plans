package com.alamin.emi.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class HTTPReqHandler {

	static long minTime = 0, maxTime = 0, totalTime = 0, avgTime = 0, startTime = 0;
	static HashMap<String, Metrics> hashMap = new HashMap<>();
	static SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY  HH:mm:ss:SSS");
	static StringBuilder sb = null;

	public synchronized static String processHttpPost(String URL, String urlParameters, String contentType,
			String pgName, String paymentType) throws Exception {
		Logging.getInfoLog().info("URL :" + URL + "\n Params :" + urlParameters);
		beforeAPICall(pgName);
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
			// conn = HttpTracesHeaders.addHttpHeaderSleuthTraces(conn);
			os = conn.getOutputStream();
			if (urlParameters != null && !urlParameters.equals("")) {
				os.write(urlParameters.getBytes());
			}
			if (conn.getResponseCode() == 200) {
				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				String output = "";
				while ((output = br.readLine()) != null) {
					urlResponse.append(output.trim());
				}
			} else {
				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
				String output = "";
				while ((output = br.readLine()) != null) {
					urlResponse.append(output);
				}
			}
			conn.disconnect();
		} finally {
			try {
				os.flush();
				afterControllerExcecution(pgName);
			} catch (IOException e) {
				Logging.getErrorLog().error(Logging.getStackTrace(e));
				throw e;
			}
		}
		Logging.getInfoLog().info("Response from PG : " + urlResponse);
		return urlResponse.toString();
	}

	public synchronized static String processHttpGet(String url, String urlParameters, String contentType,
			String pgName, String paymentType, boolean isheader, String timestamp) throws Exception {
		Logging.getInfoLog().info("URL :" + url + "\n Params :" + urlParameters);
		URL obj = null;
		if (isheader) {
			String encParams = urlParameters;
			obj = new URL(url + encParams);
		} else {
			obj = new URL(url + urlParameters);
		}
		Logging.getInfoLog().info("Request sending is : " + obj.toString());
		int timeOut = 25000;
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		// con.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
		con.setConnectTimeout(timeOut);
		con.setReadTimeout(timeOut);
		con.setDoOutput(true);
		con = HttpTracesHeaders.addHttpHeaderSleuthTraces(con);
		StringBuffer response = new StringBuffer();
		if (isheader) {
			con.setRequestProperty("merchantId", "A37G51QNUHUJBD");
			con.setRequestProperty("timestamp", timestamp);
			con.addRequestProperty("isSandbox", "false");
		}
		// add request header
		// con.setRequestProperty("User-Agent", "Mozilla/5.0");
		int responseCode = con.getResponseCode();
		Logging.getInfoLog().info("Response Code from PG : " + responseCode);

		if (con.getResponseCode() == 200) {
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
		} else {
			BufferedReader br = new BufferedReader(new InputStreamReader((con.getErrorStream())));
			String output = "";
			while ((output = br.readLine()) != null) {
				response.append(output);
			}
			br.close();
		}

		Logging.getInfoLog().info("Response from PG: " + response.toString());

		// Read JSON response and print
		JSONObject myResponse = new JSONObject(response.toString());
		return myResponse.toString();

	}

	private static void beforeAPICall(String pgName) {
		Metrics metrics = null;
		if (hashMap.get(pgName) == null) {
			metrics = new Metrics();
			metrics.setReqCount(1);
			metrics.setStartTime(System.currentTimeMillis());
			hashMap.put(pgName, metrics);
		} else {
			metrics = hashMap.get(pgName);
			metrics.setStartTime(System.currentTimeMillis());
			metrics.setReqCount(hashMap.get(pgName).getReqCount() + 1);
			hashMap.put(pgName, metrics);
		}
		metrics.setApi(pgName);
		metrics.setApiStartTime(sdf.format(new Date()));
	}

	public static void afterControllerExcecution(String pgName) {
		Metrics metrics = null;
		long elapsedTime = 0;
		if (hashMap.get(pgName) == null) {
			metrics = new Metrics();
			elapsedTime = System.currentTimeMillis() - metrics.getStartTime();
			maxTime = (metrics.getMaxTime() == 0 ? elapsedTime : metrics.getMaxTime());
			minTime = (metrics.getMinTime() == 0 ? elapsedTime : metrics.getMinTime());
			metrics.setMaxTime(Math.max(maxTime, elapsedTime));
			metrics.setMinTime((Math.min(minTime, elapsedTime)));
			metrics.setTotalTime(elapsedTime);
			if (metrics.getReqCount() != 0)
				metrics.setAvgTime(metrics.getTotalTime() / metrics.getReqCount());
			hashMap.put(pgName, metrics);
		} else {
			metrics = hashMap.get(pgName);
			elapsedTime = System.currentTimeMillis() - metrics.getStartTime();
			maxTime = (metrics.getMaxTime() == 0 ? elapsedTime : metrics.getMaxTime());
			minTime = (metrics.getMinTime() == 0 ? elapsedTime : metrics.getMinTime());
			metrics.setMaxTime(Math.max(maxTime, elapsedTime));
			metrics.setMinTime((Math.min(minTime, elapsedTime)));
			metrics.setTotalTime(metrics.getTotalTime() + elapsedTime);
			metrics.setAvgTime(metrics.getTotalTime() / metrics.getReqCount());
			hashMap.put(pgName, metrics);
		}
		printMetrics(pgName);
	}

	public static void printMetrics(String controllerName) {
		Metrics metrics = hashMap.get(controllerName);
		sb = new StringBuilder();
		sb.append("\n--------------------------------");
		sb.append("\nAPI Call to : " + controllerName);
		sb.append("\nStart time :" + metrics.getApiStartTime());
		sb.append("\nEnd Time :" + sdf.format(new Date()) + "\nRequest-Count : " + metrics.getReqCount()
				+ "\nException-Count : " + metrics.getExceptionCount() + "\nTotal-Time : " + metrics.getTotalTime()
				+ "\nMax-Time : " + metrics.getMaxTime() + "\nMin-Time : " + metrics.getMinTime() + "\nAvg-Time : "
				+ metrics.getAvgTime() + "\n--------------------------------");
		Logging.getMetricsLog().info(sb.toString());
	}

	public synchronized String processPartnerCallback(String URL, String urlParameters, String contentType) {
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
			conn = HttpTracesHeaders.addHttpHeaderSleuthTraces(conn);
			os = conn.getOutputStream();
			if (urlParameters != null && !urlParameters.equals("")) {
				os.write(urlParameters.getBytes());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output = "";
			while ((output = br.readLine()) != null) {
				urlResponse.append(output.trim());
			}
			if (conn.getResponseCode() == 200) {
				conn.disconnect();
				return "Success";
			}
		} catch (Exception e) {
			Logging.getInfoLog().info("Error while fetching transaction !");
			Logging.getErrorLog().error(Logging.getStackTrace(e));
			return null;
		} finally {
			try {
				if (os != null)
					os.flush();
			} catch (IOException e) {
				Logging.getErrorLog().error(Logging.getStackTrace(e));
			}
		}
		return null;
	}

	public static synchronized String processHttpPost(String URL, String urlParameters, String contentType)
			throws Exception {
		Logging.getInfoLog().info("URL :" + URL + "\n Params :" + urlParameters);
		int timeOut = 25000;
		StringBuilder urlResponse = new StringBuilder("");
		URL url = null;
		HttpURLConnection conn = null;
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
			// conn = HttpTracesHeaders.addHttpHeaderSleuthTraces(conn);
			OutputStream os = conn.getOutputStream();
			if (urlParameters != null && !urlParameters.equals("")) {
				os.write(urlParameters.getBytes());
			}
			if (conn.getResponseCode() == 200) {
				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				String output = "";
				while ((output = br.readLine()) != null) {
					urlResponse.append(output.trim());
				}
			} else {
				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
				String output = "";
				while ((output = br.readLine()) != null) {
					urlResponse.append(output);
				}
			}
			conn.disconnect();
			os.flush();
			os.close();
		} catch (Exception e) {
			Logging.getErrorLog().error(Logging.getStackTrace(e));
		}
		Logging.getInfoLog().debug("Response from PG : " + urlResponse);
		return urlResponse.toString();
	}

	public static synchronized String processHttpPostWithQueryString(String URL, String urlParameters,
			String contentType) throws Exception {
		Logging.getInfoLog().info("URL :" + URL + "\n Params :" + urlParameters);
		int timeOut = 25000;
		StringBuilder urlResponse = new StringBuilder("");
		URL url = null;
		HttpURLConnection conn = null;
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
			OutputStream os = conn.getOutputStream();
			if (urlParameters != null && !urlParameters.equals("")) {
				os.write(urlParameters.getBytes());
			}
			if (conn.getResponseCode() == 200) {
				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				String output = "";
				while ((output = br.readLine()) != null) {
					urlResponse.append(output.trim());
				}
			} else {
				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
				String output = "";
				while ((output = br.readLine()) != null) {
					urlResponse.append(output);
				}
			}
			conn.disconnect();
			os.flush();
			os.close();
		} catch (Exception e) {
			Logging.getErrorLog().error(Logging.getStackTrace(e));
		}
		Logging.getInfoLog().debug("Response from PG : " + urlResponse);
		return urlResponse.toString();
	}

}
