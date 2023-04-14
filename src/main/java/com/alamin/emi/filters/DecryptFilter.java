package com.alamin.emi.filters;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.gson.Gson;
import com.alamin.emi.dto.PosResponseParams;
import com.alamin.emi.exceptions.InvalidRequestException;
import com.alamin.emi.security.DataDecryptor;
import com.alamin.emi.security.EncryptedData;
import com.alamin.emi.utils.Logging;
import com.alamin.emi.utils.ResponseCodesAndStates;

import jodd.servlet.ServletUtil;
import lombok.Cleanup;

@Component
public class DecryptFilter extends OncePerRequestFilter {

	@Value("${encryption.enable:true}")
	private boolean encryptionEnable;

	@Value("${server.servlet.context-path}")
	private String contextPath;

	@Autowired
	private DataDecryptor dataDecryptor;

	Gson gson = new Gson();

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		HttpServletRequest originalRequest = (HttpServletRequest) request;
		HttpServletResponse originalResponse = (HttpServletResponse) response;

		String originalRequestBody = ServletUtil.readRequestBodyFromStream(originalRequest);
		Logging.getInfoLog().info("originalRequestBody: " + originalRequestBody);
		EncryptedData encryptedData = null;
		String decryptedRequestBody = null;
		try {
			decryptedRequestBody = originalRequestBody;
			if (encryptionEnable) {
				encryptedData = gson.fromJson(originalRequestBody, EncryptedData.class);
				decryptedRequestBody = dataDecryptor.decryptData(encryptedData);
			}
			Logging.getInfoLog().info("modifyRequestBody " + decryptedRequestBody);
			request.setAttribute("decryptedRequestBody", decryptedRequestBody);
		} catch (InvalidRequestException | javax.crypto.BadPaddingException e) {
			String appResponse = getSessionExpiredResponse();
			originalResponse.setContentType("application/json");
			byte[] responseData = appResponse.getBytes(StandardCharsets.UTF_8);
			originalResponse.setContentLength(responseData.length);
			@Cleanup
			ServletOutputStream out = originalResponse.getOutputStream();
			out.write(responseData);
			return;
		} catch (Exception e) {
			Logging.getErrorLog().error(Logging.getStackTrace(e));
		}

		ModifyRequestBodyWrapper requestWrapper = new ModifyRequestBodyWrapper(originalRequest, decryptedRequestBody);

		ModifyResponseBodyWrapper responseWrapper = new ModifyResponseBodyWrapper(originalResponse);

		chain.doFilter(requestWrapper, responseWrapper);

		String originalResponseBody = responseWrapper.getResponseBody("UTF-8");
		Logging.getInfoLog().info("originalResponseBody: " + originalResponseBody);

		String encryptedResponse = originalResponseBody;
		try {
			if (encryptionEnable) {
				encryptedResponse = dataDecryptor.encryptResponsePayload(originalResponseBody, encryptedData);
			}
			Logging.getInfoLog().info("Encrypted Response: " + encryptedResponse);
		} catch (Exception e) {
			Logging.getErrorLog().error(Logging.getStackTrace(e));
		}
		originalResponse.setContentType(requestWrapper.getOrginalRequest().getContentType());
		byte[] responseData = encryptedResponse.getBytes(responseWrapper.getCharacterEncoding());

		originalResponse.setContentLength(responseData.length);
		@Cleanup
		ServletOutputStream out = originalResponse.getOutputStream();
		out.write(responseData);
	}

	private String getSessionExpiredResponse() {
		PosResponseParams posResponse = new PosResponseParams();
		posResponse.setStatus(ResponseCodesAndStates.TxnStates.Failed.getValue());
		posResponse.setRespMsg(ResponseCodesAndStates.TxnStates.SessionExpired.getValue());
		posResponse.setErrorCode(ResponseCodesAndStates.getErrorCode().get(posResponse.getRespMsg()) == null
				? ResponseCodesAndStates.getErrorCode().get(posResponse.getStatus())
				: ResponseCodesAndStates.getErrorCode().get(posResponse.getRespMsg()));
		return gson.toJson(posResponse);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

		// centrlised enc/dec filteration applied for the below api's, control this api
		// via encryption.enable = true / false
		List<String> urls = Arrays
				.asList(contextPath+"/check-eligibility",
						contextPath + "/initiate-transaction",
						contextPath+"/pre-approved-emi",
						contextPath+"/verify-otp",
						contextPath+"/plans"
						 );
		if (urls.contains(request.getRequestURI())) {
			return false;
		}
		return true;
	}

}
