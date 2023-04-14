package com.alamin.emi.interceptors;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.alamin.emi.session.services.SessionData;
import com.alamin.emi.session.services.Sessionutils;
import com.alamin.emi.session.services.TidInfo;
import com.alamin.emi.utils.Logging;

@Service
public class MyCustomInterceptor implements HandlerInterceptor {
	@Autowired
	private Sessionutils readSession;

	private static final String SESSION_NOT_PRESENT = "Session Id is not present";
	private static final String SESSION_NOT_AUTHRORIZED = "Session is UnAuthorized";
	@Value("${server.servlet.context-path}")
	private String contextPath;
	@Autowired
	private org.springframework.core.env.Environment env;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		return validateSession(request, response, handler);

	}

	private boolean validateSession(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws IOException {
		String sessionId = request.getHeader("sessionId");
		if (sessionId == null || sessionId.isEmpty()) {
			Logging.getInfoLog().info("Endpoint : " + request.getRequestURI() + " : " + SESSION_NOT_PRESENT);
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, SESSION_NOT_PRESENT);
			return false;
		}
		SessionData sessionData = readSession.readSessionData(sessionId);
		if (sessionData == null || !sessionData.isAuthStatus()) {
			Logging.getInfoLog().info("Endpoint : " + request.getRequestURI() + " : " + SESSION_NOT_AUTHRORIZED);
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, SESSION_NOT_AUTHRORIZED);
			return false;
		}
		if (sessionData.isAuthStatus()) {
			String exclusionList = env.getProperty("api_exculsion_list_for_loginId_authentication");
			if (exclusionList != null && !exclusionList.isEmpty()) {
				String[] apis = exclusionList.split(",");
				for (String api : apis) {
					if ((contextPath + api).equals(request.getRequestURI())) {
						return true;
					}
				}
			}
			if (!isLoginAuthrorized(request, response, sessionData)) {
				sendInvalidResponse(request, response);
				return false;
			}
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// No action after handling
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// No action after completion
	}

	private boolean isLoginAuthrorized(HttpServletRequest request, HttpServletResponse response,
			SessionData sessionData) {
		try {
			LoginRequest posRequest = getBody(request);

			if (posRequest != null) {
				String terminalId = posRequest.getTerminalId();
				Map<String, TidInfo> sessionList = getSessionTids(sessionData);
				if (terminalId != null) {
					if (sessionList.get(terminalId) == null) {
						Logging.getInfoLog().info("Not found in session :" + terminalId);
						return false;
					}
				}
			}
		} catch (Exception e) {
			Logging.getErrorLog().error(Logging.getStackTrace(e));
		}
		return true;

	}

	public static LoginRequest getBody(HttpServletRequest request) throws IOException {
		String decryptedRequestBody = (String) request.getAttribute("decryptedRequestBody");
		Gson gson = new Gson();
		return gson.fromJson(decryptedRequestBody, LoginRequest.class);
	}

	private void sendInvalidResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "FORBIDDEN");
	}

	private Map<String, TidInfo> getSessionTids(SessionData sessionData) {
		Map<String, TidInfo> tidMap = new HashMap<>();
		for (Map<String, TidInfo> map : sessionData.getTidList()) {
			for (Map.Entry<String, TidInfo> entry : map.entrySet()) {
				tidMap.put(entry.getKey(), entry.getValue());
			}
		}
		return tidMap;
	}
}
