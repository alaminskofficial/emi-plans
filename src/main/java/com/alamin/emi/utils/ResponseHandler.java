package com.alamin.emi.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.alamin.emi.dto.BaseResponse;
import com.alamin.emi.dto.PosResponseParams;
import com.alamin.emi.security.DataDecryptor;
import com.alamin.emi.security.EncryptedData;
import com.alamin.emi.utils.ResponseCodesAndStates.TxnStates;

@Component
public class ResponseHandler {
	@Autowired
	private DataDecryptor dataDecryptor;
	Gson gson = new Gson();

	public synchronized String respMessage(String status, String respMsg) {
		try {
			PosResponseParams posResp = new PosResponseParams();
			posResp.setStatus(status);
			posResp.setRespMsg(respMsg);
			posResp.setErrorCode(ResponseCodesAndStates.getErrorCode().get(respMsg) == null
					? ResponseCodesAndStates.getErrorCode().get(status)
					: ResponseCodesAndStates.getErrorCode().get(respMsg));
			String respToPos = gson.toJson(posResp);
			return respToPos;
			// Logging.getInfoLog().info("Resp given to Pos : " + respToPos);
			// response.getWriter().write(respToPos);
		} catch (Exception e) {
			Logging.getErrorLog().error(Logging.getStackTrace(e));
		}
		return null;
	}

	public synchronized String softUpdaterespMessage(String status, String respMsg) {
		try {
			PosResponseParams posResp = new PosResponseParams();
			posResp.setStatus(status);
			posResp.setRespMsg(respMsg);
			posResp.setErrorCode(ResponseCodesAndStates.getErrorCode().get(TxnStates.APKUpdate.getValue()));
			String respToPos = gson.toJson(posResp);
			return respToPos;
			// Logging.getInfoLog().info("Resp given to Pos : " + respToPos);
			// response.getWriter().write(respToPos);
		} catch (Exception e) {
			Logging.getErrorLog().error(Logging.getStackTrace(e));
		}
		return null;
	}

	public synchronized void sendURLLink(String status, String respMsg, String url, HttpServletResponse response) {
		try {
			PosResponseParams posResp = new PosResponseParams();
			posResp.setStatus(status);
			posResp.setRespMsg(respMsg);
			posResp.setErrorCode(ResponseCodesAndStates.getErrorCode().get(respMsg) == null
					? ResponseCodesAndStates.getErrorCode().get(status)
					: ResponseCodesAndStates.getErrorCode().get(respMsg));
			String respToPos = gson.toJson(posResp);
			Logging.getInfoLog().info("Resp given to Pos : " + respToPos);
			response.getWriter().write(respToPos);
		} catch (Exception e) {
			Logging.getErrorLog().error(Logging.getStackTrace(e));
		}
	}

	public synchronized String getURLLink(String status, String respMsg, String url) {
		try {
			PosResponseParams posResp = new PosResponseParams();
			posResp.setStatus(status);
			posResp.setRespMsg(respMsg);
			posResp.setErrorCode(ResponseCodesAndStates.getErrorCode().get(TxnStates.Success.getValue()));
			String respToPos = gson.toJson(posResp);
			Logging.getInfoLog().info("Resp given to Pos : " + respToPos);
			return respToPos;
		} catch (Exception e) {
			Logging.getErrorLog().error(Logging.getStackTrace(e));
		}
		return null;
	}

	public synchronized void respCodeToMessage(String status, int respCode, String mTxnid,
			HttpServletResponse response) {
		try {
			PosResponseParams posResp = new PosResponseParams();
			posResp.setStatus(status);
			posResp.setRespMsg(ResponseCodesAndStates.getCodeToDesc().get(respCode));
			String respToPos = gson.toJson(posResp);
			Logging.getInfoLog().info("Resp given to Pos : " + respToPos);
			response.getWriter().write(respToPos);
		} catch (Exception e) {
			Logging.getErrorLog().error(Logging.getStackTrace(e));
		}
	}

	public synchronized void respMessage(String status, String respMsg, String mTxnid, HttpServletResponse response) {
		try {
			PosResponseParams posResp = new PosResponseParams();
			posResp.setStatus(status);
			posResp.setRespMsg(respMsg);
			posResp.setErrorCode(ResponseCodesAndStates.getErrorCode().get(respMsg));
			String respToPos = gson.toJson(posResp);
//			return respToPos;
			Logging.getInfoLog().info("Resp given to Pos : " + respToPos);
			response.getWriter().write(respToPos);
		} catch (Exception e) {
			Logging.getErrorLog().error(Logging.getStackTrace(e));
		}
	}

	public synchronized void respMessage(String status, int respCode, String mTxnid, HttpServletResponse response) {
		try {
			PosResponseParams posResp = new PosResponseParams();
			posResp.setStatus(status);
			posResp.setRespMsg(ResponseCodesAndStates.getCodeToDesc().get(respCode));
			String respToPos = gson.toJson(posResp);
			Logging.getInfoLog().info("Resp given to Pos : " + respToPos);
			response.getWriter().write(respToPos);
		} catch (Exception e) {
			Logging.getErrorLog().error(Logging.getStackTrace(e));
		}
	}

	public synchronized String sendEncryptedRespToPos(PosResponseParams posResponseParams, HttpServletResponse response,
			EncryptedData encryptedData) throws Exception {
		try {
//			posResponseParams.setErrorCode(ResponseCodesAndStates.getErrorCode().get(posResponseParams.getRespMsg()));
			posResponseParams
					.setErrorCode(ResponseCodesAndStates.getErrorCode().get(posResponseParams.getRespMsg()) == null
							? ResponseCodesAndStates.getErrorCode().get(posResponseParams.getStatus())
							: ResponseCodesAndStates.getErrorCode().get(posResponseParams.getRespMsg()));
			String respToPos = gson.toJson(posResponseParams);
			Logging.getInfoLog().info("Resp given to Pos : " + respToPos);
			String encData = dataDecryptor.encryptResponsePayload(respToPos, encryptedData);
			Logging.getInfoLog().info("ENC Resp given to Pos : " + encData);
			response.getWriter().write(encData);
		} catch (Exception e) {
			Logging.getErrorLog().error(Logging.getStackTrace(e));
			throw e;
		}
		return null;
	}

	public synchronized String sendRespToPos(PosResponseParams posResponseParams, HttpServletResponse response)
			throws Exception {
		try {
			posResponseParams
					.setErrorCode(ResponseCodesAndStates.getErrorCode().get(posResponseParams.getRespMsg()) == null
							? ResponseCodesAndStates.getErrorCode().get(posResponseParams.getStatus())
							: ResponseCodesAndStates.getErrorCode().get(posResponseParams.getRespMsg()));
			String respToPos = gson.toJson(posResponseParams);
			Logging.getInfoLog().info("Resp given to Pos : " + respToPos);
			response.getWriter().write(respToPos);
		} catch (Exception e) {
			throw e;
		}
		return null;
	}

	public synchronized String partnerRespMessage(String status, HttpServletResponse response) {
		try {
			/*
			 * PosResponseParams posResp = new PosResponseParams();
			 * posResp.setStatus(status); posResp.setRespMsg(respMsg);
			 * posResp.setErrorCode(ResponseCodesAndStates.getErrorCode().get(respMsg) ==
			 * null ? ResponseCodesAndStates.getErrorCode().get(status) :
			 * ResponseCodesAndStates.getErrorCode().get(respMsg)); String respToPos =
			 * gson.toJson(posResp);
			 */
			response.getWriter().write(status);
			// Logging.getInfoLog().info("Resp given to Pos : " + respToPos);
			// response.getWriter().write(respToPos);
		} catch (Exception e) {
			Logging.getErrorLog().error(Logging.getStackTrace(e));
		}
		return null;
	}

	public static String getUri(HttpServletRequest request) {
		return request.getRequestURI().replace("/emi", "");
	}

	public BaseResponse getServerErrorResponse() {
		BaseResponse resp = new BaseResponse();
		resp.setStatus(ResponseCodesAndStates.TxnStates.Failed.getValue());
		resp.setRespMsg(ResponseCodesAndStates.TxnStates.INTERNAL_SERVER_ERROR.getValue());
		return resp;
	}
}
