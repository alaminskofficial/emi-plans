package com.alamin.emi.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.ParameterizedMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.alamin.emi.constants.EMIConstants;
import com.alamin.emi.exceptions.EMIException;
import com.alamin.emi.helper.Loggable;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

@Configuration
public class OkHttpUtility {

	private static final Logger LOGGER = LogManager.getLogger(OkHttpUtility.class);

	@Autowired
	ResourceLoader resourceLoader;

	@Value("${http.connectTimeoutSeconds}")
	private int httpConnectTimeOutSeconds;

	@Value("${http.readTimeoutSeconds}")
	private int httpReadTimeOutSeconds;

	@Value("${http.writeTimeoutSeconds}")
	private int httpWriteTimeOutSeconds;

	@Value("${vpn.enabled:true}")
	private boolean isVpnEnabled;
	
	@Autowired
	Environment env;

	// @SuppressWarnings("deprecation")
	@Loggable
	public <T> T doPost(String url, String requestBody, String auth, Class<T> clazz)
			throws EMIException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		OkHttpClient okHttpClient = new OkHttpClient.Builder()
				.connectTimeout(httpConnectTimeOutSeconds, TimeUnit.SECONDS)
				.writeTimeout(httpWriteTimeOutSeconds, TimeUnit.SECONDS)
				.readTimeout(httpReadTimeOutSeconds, TimeUnit.SECONDS).build();

		Logging.getInfoLog().info("Bank Url: " + url + "\n Bank Payload: " + requestBody);
		MediaType mediaType = MediaType.parse(org.springframework.http.MediaType.APPLICATION_JSON_VALUE);
		String response = null;
		try {
			// String encRequestBody = apiEncryption.encrypt(requestBody);
			RequestBody body = RequestBody.create(mediaType, requestBody);
			Logging.getInfoLog().info("Request Body:" + requestBody);
			// Create Request
			Builder requestBuilder = new Request.Builder().url(url).post(body);

			if (StringUtils.isNotBlank(auth)) {
				requestBuilder.addHeader(EMIConstants.AUTHORIZATION, auth);
			}
			Request request = requestBuilder.build();
			Call call = okHttpClient.newCall(request);
			Response networkResponse = call.execute();
			response = networkResponse.body().string();
			// String encResponseBody = apiEncryption.decrypt(response);
			Logging.getInfoLog().info("decrypted Response body: " + response);
			return new Gson().fromJson(response, clazz);
		} catch (IOException e) {
			LOGGER.error(new ParameterizedMessage("Error in sending request for url:: {} with response:: {}", url,
					response, e));
			throw new EMIException(5010, e.getMessage(), e);
		} catch (Exception e) {
			throw e;
		}
	}

	@Loggable
	public <T> T doCustomPost(String url, Map<String, Object> requestBody, String auth, Class<T> clazz)
			throws EMIException {
		OkHttpClient okHttpClient = new OkHttpClient.Builder()
				.connectTimeout(httpConnectTimeOutSeconds, TimeUnit.SECONDS)
				.writeTimeout(httpWriteTimeOutSeconds, TimeUnit.SECONDS)
				.readTimeout(httpReadTimeOutSeconds, TimeUnit.SECONDS).build();

		MediaType mediaType = MediaType.parse(org.springframework.http.MediaType.APPLICATION_JSON_VALUE);
		Gson gson = new Gson();
		String requestPayload = gson.toJson(requestBody);
		ResponseBody respBody = null;
		String response = null;
		try {
			RequestBody body = RequestBody.create(mediaType, requestPayload);

			// Create Request
			Builder requestBuilder = new Request.Builder().url(url).post(body);

			if (StringUtils.isNotBlank(auth)) {
				requestBuilder.addHeader(EMIConstants.AUTHORIZATION, auth);
			}
			Request request = requestBuilder.build();
			Call call = okHttpClient.newCall(request);
			Response networkResponse = call.execute();
			respBody = networkResponse.body();
			response = respBody.string();
			Object uniqueReferenceId = AppUtility.getValueFromJsonString(response, "id");
			int code = networkResponse.code();
			String responseWithAdditionalData = StringUtils.removeEnd(response, "}") + ",\"statusCode\":" + code
					+ ",\"unique_reference_id\":" + uniqueReferenceId + "}";
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(responseWithAdditionalData, clazz);

		} catch (IOException e) {
			LOGGER.error(new ParameterizedMessage("Error in sending request for url:: {} with response:: {}", url,
					response, e));
			throw new EMIException(5010, e.getMessage(), e);
		}
	}

	@Loggable
	public <T> T doPostwithConfig(String apiUrl, Map<String, Object> requestBody, String auth, Class<T> clazz)
			throws Exception {
		if (isVpnEnabled) {
			return doCustomPostWithSSL(apiUrl, null, auth, clazz);
		} else {
			return doCustomPost(apiUrl, requestBody, auth, clazz);
		}

	}

	@Loggable
	public <T> T doCustomPostWithSSL(String apiUrl, String requestBody, String auth, Class<T> clazz) throws Exception {

		StringBuilder urlResponse = new StringBuilder("");
		URL url = null;
		HttpsURLConnection conn = null;
		OutputStream os = null;
		try {
			Logging.getInfoLog().info("Request sending to WL::: " + requestBody);
			try (InputStream keystoreStream = new FileInputStream(env.getProperty("P12FilePath"))) {

				KeyManagerFactory keyManagerFactory = KeyManagerFactory
						.getInstance(KeyManagerFactory.getDefaultAlgorithm());
				KeyStore keyStore = KeyStore.getInstance("PKCS12");
				keyStore.load(keystoreStream, "changeit".toCharArray());
				keyManagerFactory.init(keyStore, "changeit".toCharArray());
				KeyManager[] keyManagers = keyManagerFactory.getKeyManagers();

				/*
				 * SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
				 * sslContext.init(keyManagers, null, new SecureRandom());
				 */
				SSLContext sslContext = insecureContext();
				HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

				url = new URL(apiUrl);
				conn = (HttpsURLConnection) url.openConnection();

				conn.setRequestProperty("Content-Type", org.springframework.http.MediaType.APPLICATION_JSON_VALUE);
				if (auth != null) {
					conn.setRequestProperty("Authorization", auth);
				}
				conn.setConnectTimeout(httpConnectTimeOutSeconds * 1000);
				conn.setReadTimeout(httpReadTimeOutSeconds * 1000);
				conn.setRequestMethod("POST");
				conn.setDoOutput(true);

				os = conn.getOutputStream();
				if (requestBody != null && !requestBody.equals("")) {
					os.write(requestBody.getBytes());
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
				LOGGER.info("Response from PG : " + urlResponse);
				String decryptedRespone = urlResponse.toString();
				// return new Gson().fromJson(decryptedRespone, clazz);
				return new ObjectMapper().readValue(decryptedRespone, clazz);
			}
		} catch (IOException e) {
			LOGGER.error("error occured while calling {} ", apiUrl, e);
			throw new EMIException(5010, e.getMessage(), e);
		} catch (Exception e) {
			if (conn.getResponseCode() == 500) {
				throw new EMIException(500, "Internal Server Error", e);
			}
			Logging.getErrorLog().error(Logging.getStackTrace(e));
			throw e;
		} finally {
			try {
				os.flush();
			} catch (IOException e) {
				LOGGER.error("error occured while calling {} ", apiUrl, e);
			}
		}

	}
	
	public <T> T doCustomPostWithSSLForDRE(String apiUrl, String requestBody, String subscriptionKey, Class<T> clazz) throws Exception {

		StringBuilder urlResponse = new StringBuilder("");
		URL url = null;
		HttpsURLConnection conn = null;
		OutputStream os = null;
		try {
			Logging.getInfoLog().info("Request sending to WL::: " + requestBody);
			try (InputStream keystoreStream = new FileInputStream(env.getProperty("P12FilePath"))) {

				KeyManagerFactory keyManagerFactory = KeyManagerFactory
						.getInstance(KeyManagerFactory.getDefaultAlgorithm());
				KeyStore keyStore = KeyStore.getInstance("PKCS12");
				keyStore.load(keystoreStream, "changeit".toCharArray());
				keyManagerFactory.init(keyStore, "changeit".toCharArray());
				KeyManager[] keyManagers = keyManagerFactory.getKeyManagers();

				/*
				 * SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
				 * sslContext.init(keyManagers, null, new SecureRandom());
				 */
				SSLContext sslContext = insecureContext();
				HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

				url = new URL(apiUrl);
				conn = (HttpsURLConnection) url.openConnection();

				conn.setRequestProperty("Content-Type", org.springframework.http.MediaType.APPLICATION_JSON_VALUE);
				if (StringUtils.isNotBlank(subscriptionKey)) {
					conn.setRequestProperty("OCP-APIM-Subscription-key", subscriptionKey);
				}
				conn.setConnectTimeout(httpConnectTimeOutSeconds * 1000);
				conn.setReadTimeout(httpReadTimeOutSeconds * 1000);
				conn.setRequestMethod("POST");
				conn.setDoOutput(true);

				os = conn.getOutputStream();
				if (requestBody != null && !requestBody.equals("")) {
					os.write(requestBody.getBytes());
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
				LOGGER.info("Response from PG : " + urlResponse);
				String decryptedRespone = urlResponse.toString();
				// return new Gson().fromJson(decryptedRespone, clazz);
				return new ObjectMapper().readValue(decryptedRespone, clazz);
			}
		} catch (IOException e) {
			LOGGER.error("error occured while calling {} ", apiUrl, e);
			throw new EMIException(5010, e.getMessage(), e);
		} catch (Exception e) {
			if (conn.getResponseCode() == 500) {
				throw new EMIException(500, "Internal Server Error", e);
			}
			Logging.getErrorLog().error(Logging.getStackTrace(e));
			throw e;
		} finally {
			try {
				os.flush();
			} catch (IOException e) {
				LOGGER.error("error occured while calling {} ", apiUrl, e);
			}
		}

	}

	private static SSLContext sslContext(String keystoreFile, String password) throws EMIException {
		try {
			KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());

			try (InputStream in = new FileInputStream(keystoreFile)) {
				keystore.load(in, password.toCharArray());
			}
			KeyManagerFactory keyManagerFactory = KeyManagerFactory
					.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			keyManagerFactory.init(keystore, password.toCharArray());

			TrustManagerFactory trustManagerFactory = TrustManagerFactory
					.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			trustManagerFactory.init(keystore);
			SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
			sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(),
					new SecureRandom());

			return sslContext;
		} catch (Exception ex) {
			LOGGER.error(new ParameterizedMessage("Error occred while creating sslContext {}", ex));
			throw new EMIException(5010, ex.getMessage(), ex);
		}
	}

	public <T> T doGet(String url, String auth, Type listType) throws EMIException {
		OkHttpClient okHttpClient = new OkHttpClient.Builder()
				.connectTimeout(httpConnectTimeOutSeconds, TimeUnit.SECONDS)
				.writeTimeout(httpWriteTimeOutSeconds, TimeUnit.SECONDS)
				.readTimeout(httpReadTimeOutSeconds, TimeUnit.SECONDS).build();
		try {
			// Create Request
			Builder requestBuilder = new Request.Builder().url(url).get();

			if (StringUtils.isNotBlank(auth)) {
				requestBuilder.addHeader(EMIConstants.AUTHORIZATION, auth);
			}
			Request request = requestBuilder.build();
			Call call = okHttpClient.newCall(request);
			String responseBody = call.execute().body().string();
			return new Gson().fromJson(responseBody, listType);
		} catch (IOException e) {
			LOGGER.error("Error in sending reuest for url::  " + url);
			throw new EMIException(5010, e.getMessage(), e);
		}
	}

	/*
	 * private static SSLContext getPKCS12Path() { try(InputStream keystoreStream =
	 * new FileInputStream(env.getProperty("P12FilePath"))){
	 * 
	 * KeyManagerFactory keyManagerFactory =
	 * KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
	 * KeyStore keyStore = KeyStore.getInstance("PKCS12");
	 * keyStore.load(keystoreStream, "changeit".toCharArray());
	 * keyManagerFactory.init(keyStore, "changeit".toCharArray()); KeyManager[]
	 * keyManagers = keyManagerFactory.getKeyManagers();
	 * 
	 * SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
	 * 
	 * sslContext.init(keyManagers, null, new SecureRandom());
	 * HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
	 * }catch (Exception e) { Logging.getErrorLog().error(Logging.getStackTrace(e));
	 * } }
	 */

	@Loggable
	public <T> T getJWTokenWithSSL(String apiUrl, String requestBody, String auth, Class<T> clazz) throws EMIException {

		StringBuffer urlResponse = new StringBuffer("");
		URL url = null;
		HttpsURLConnection conn = null;
		OutputStream os = null;
		Gson gson = new Gson();
		try {
			Logging.getInfoLog().info("Request sending to WL::: " + requestBody);
			try (InputStream keystoreStream = new FileInputStream(env.getProperty("P12FilePath"))) {

				KeyManagerFactory keyManagerFactory = KeyManagerFactory
						.getInstance(KeyManagerFactory.getDefaultAlgorithm());
				KeyStore keyStore = KeyStore.getInstance("PKCS12");
				keyStore.load(keystoreStream, "changeit".toCharArray());
				keyManagerFactory.init(keyStore, "changeit".toCharArray());
				KeyManager[] keyManagers = keyManagerFactory.getKeyManagers();

				// SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
				SSLContext sslContext = insecureContext();
				// sslContext.init(keyManagers, null, new SecureRandom());
				HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

				url = new URL(apiUrl);
				conn = (HttpsURLConnection) url.openConnection();

				conn.setRequestProperty("Content-Type", org.springframework.http.MediaType.APPLICATION_JSON_VALUE);
				if (auth != null) {
					conn.setRequestProperty("Authorization", auth);
				}
				conn.setConnectTimeout(httpConnectTimeOutSeconds * 1000);
				conn.setReadTimeout(httpReadTimeOutSeconds * 1000);
				conn.setRequestMethod("POST");
				conn.setDoOutput(true);

				os = conn.getOutputStream();
				if (requestBody != null && !requestBody.equals("")) {
					os.write(requestBody.getBytes());
				}
				int code = conn.getResponseCode();
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
				// String responseWithCode =
				// StringUtils.removeEnd(urlResponse.toString(),"}")+",\"statusCode\":"+code+"}";
				conn.disconnect();
				LOGGER.info("Response from PG : " + urlResponse);

				// ObjectMapper mapper = new ObjectMapper();
				// return mapper.readValue(responseWithCode, clazz);
				return new Gson().fromJson(urlResponse.toString(), clazz);
			}
		} catch (Exception e) {
			LOGGER.error("error occured while calling {} ", apiUrl, e);
			throw new EMIException(5010, e.getMessage(), e);
		} finally {
			try {
				os.flush();
			} catch (IOException e) {
				LOGGER.error("error occured while calling {} ", apiUrl, e);
			}
		}

	}

	static SSLContext insecureContext() {
		TrustManager[] noopTrustManager = new TrustManager[] { new X509TrustManager() {

			@Override
			public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
					throws CertificateException {
				// TODO Auto-generated method stub

			}

			@Override
			public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
					throws CertificateException {
				// TODO Auto-generated method stub

			}

			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				// TODO Auto-generated method stub
				return null;
			}
		} };
		try {
			SSLContext sc = SSLContext.getInstance("ssl");
			sc.init(null, noopTrustManager, null);
			return sc;
		} catch (KeyManagementException | NoSuchAlgorithmException ex) {
		}
		return null;
	}

}
