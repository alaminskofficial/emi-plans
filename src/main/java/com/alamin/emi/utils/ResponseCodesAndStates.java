package com.alamin.emi.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.google.common.collect.HashBiMap;

@Component
public class ResponseCodesAndStates {

	public static enum TxnStates {
		Initiated("Initiated"),
		InProgress("InProgress"),
		Success("Success"),
		Failed("Failed"),
		SessionTimeOut("This terminalId is already in use."), 
		SessionExpired("Session expired, Please try again."),
		Expired("Expired"),
		APKUpdate("APK need to be updated"),
		INTERNAL_SERVER_ERROR("Sorry! Some internal Server Error occured, Please try after some time"),
		Invalid_Request("Invalid Request"),
		NoDataFound("No data found"),
		Data_Available("Data Available"),
		Invalid_OrderID("Invalid alamin Txnid"),
		PaymentGateWayError("No response from Payment Gateway"),
		MAND_PARAM_MISSING("Mandatory parameter is missing");
		

		private String value;

		private TxnStates(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
	
	public enum ResponseCodes{
        SUCCESS(1),
        FAILED(0),
        Pending(2),
        MAND_PARAM_MISSING(3),
        INVALID_Amount(4), 
        INVALID_SIGN(5),
        INVALID_MERCHANT_ID(6),
        MERCHANT_INACTIVE(7),
        DUPLICATE_ID(8),
        INTERNAL_SERVER_ERROR(9),
        SMS_SUCCESS(10),
        SMS_FAILED(11),
        SMS_INITIATED(12),
        Email_AND_SMS_INITIATED(13),
        MAX_RESEND_RETRIES_REACHED(14),
        SMS_ALREADY_SENT(15),
        IN_PROCESS(16),
        OTP_Failed(17),
        Invalid_PIN(18),
        Invalid_Request(19),
        Invalid_Mobile(20),
        Not_Authorized(21),
        Not_Initialized(22),
        Not_Activated(23),
        Intialized(0),
        Active(1),
        Suspended(2),
        terminated(3),
        thisuserlogininanotherdevicepleaselogoffandlogin(28),
        Invalid_User_Name(29),
        Session_Exipred(30),
        Invalid_Password(31),
        TOKEN_MISSING(32),
        Invalid_OrderID(33),
        alaminDevice(0),
        TerminalLock(1),
        TerminalUnlock(0),
        CardBillNum(1),
        CardTxnidNum(2);
       
        
		private ResponseCodes() {
		}

		private int value;

		private ResponseCodes(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
    }
	
	public enum ETBStatus{
		JWT("JWT"),
		ALGO("RS256"),
		NO_RESP_FROM_BANK("No Response from bank"),
		BANK_API_TECHINCAL_ISSUE("Technical Issue from bank"),
		API_GATEWAY_ERROR("Bank API gateway error");
		private String value;
		private ETBStatus(String value) {
			this.value = value;
		}
		public String getValue() {
			return value;
		}
	}

	private static Map<String, String> statusMessageToCode = HashBiMap.create();
	private static Map<Integer, String> codeToStatus = new HashMap<Integer, String>();
	private static Map<Integer, String> codeToDesc = new HashMap<>();

	static {

		statusMessageToCode.put(TxnStates.SessionExpired.getValue(), "E105");
		statusMessageToCode.put(TxnStates.SessionTimeOut.getValue(), "P126");
		statusMessageToCode.put(TxnStates.Success.getValue(), "S101");
		statusMessageToCode.put(TxnStates.Failed.getValue(), "S102");
		statusMessageToCode.put(TxnStates.InProgress.getValue(), "S103");
		statusMessageToCode.put(TxnStates.Expired.getValue(), "S110");
		statusMessageToCode.put(TxnStates.APKUpdate.getValue(), "L106");
		statusMessageToCode.put(TxnStates.INTERNAL_SERVER_ERROR.getValue(), "E101");
		statusMessageToCode.put(TxnStates.Invalid_Request.getValue(), "P101");
		statusMessageToCode.put(TxnStates.MAND_PARAM_MISSING.getValue(), "P142");
		statusMessageToCode.put(TxnStates.NoDataFound.getValue(), "L114");
		statusMessageToCode.put(TxnStates.Data_Available.getValue(), "L115");
		statusMessageToCode.put(TxnStates.Invalid_OrderID.getValue(), "P110");
		statusMessageToCode.put(TxnStates.PaymentGateWayError.getValue(), "E107");

		codeToStatus.put(1, TxnStates.InProgress.getValue());
		codeToStatus.put(2, TxnStates.Success.getValue());
		codeToStatus.put(3, TxnStates.Failed.getValue());
		
		codeToDesc.put(ResponseCodes.SUCCESS.getValue(), "Success");
        codeToDesc.put(ResponseCodes.FAILED.getValue(), "Failed");
        codeToDesc.put(ResponseCodes.MAND_PARAM_MISSING.getValue(), "Mandatory parameter is missing");
        codeToDesc.put(ResponseCodes.TOKEN_MISSING.getValue(), "Token is missing");
        codeToDesc.put(ResponseCodes.INVALID_Amount.getValue(), "Invalid Amount");
        codeToDesc.put(ResponseCodes.INVALID_SIGN.getValue(), "Invalid Signature");
        codeToDesc.put(ResponseCodes.INVALID_MERCHANT_ID.getValue(), "Invalid Merchant ID");
        codeToDesc.put(ResponseCodes.MERCHANT_INACTIVE.getValue(), "Merchant ID inactive");
        codeToDesc.put(ResponseCodes.DUPLICATE_ID.getValue(), "Duplicate transaction ID");
        codeToDesc.put(ResponseCodes.INTERNAL_SERVER_ERROR.getValue(), "Sorry! Some internal Server Error occured, Please try after some time");
        codeToDesc.put(ResponseCodes.SMS_SUCCESS.getValue(), "Payment SMS pushed to User");
        codeToDesc.put(ResponseCodes.SMS_FAILED.getValue(), "Payment SMS delivery Failed");
        codeToDesc.put(ResponseCodes.SMS_INITIATED.getValue(), "Payment SMS Initiated");
        codeToDesc.put(ResponseCodes.Email_AND_SMS_INITIATED.getValue(), "Payment Email and SMS Initiated");
        codeToDesc.put(ResponseCodes.MAX_RESEND_RETRIES_REACHED.getValue(), "Maximum retries reached");
        codeToDesc.put(ResponseCodes.SMS_ALREADY_SENT.getValue(), "SMS delivered. Payment is in progress");
        codeToDesc.put(ResponseCodes.IN_PROCESS.getValue(), "Transaction in Process");
        codeToDesc.put(ResponseCodes.Not_Authorized.getValue(), "Mobile is not authorized by the Merchant");
        codeToDesc.put(ResponseCodes.Not_Initialized.getValue(), "Mobile is not initialized by the Merchant");
        codeToDesc.put(ResponseCodes.Not_Activated.getValue(), "Mobile is not activated by the Merchant");
        codeToDesc.put(ResponseCodes.thisuserlogininanotherdevicepleaselogoffandlogin.getValue(), "This user logged in another device, please log off and login");
        codeToDesc.put(ResponseCodes.Invalid_User_Name.getValue(), "User name does not exists, kindly contact alamin support team support@alamin.com" );
        codeToDesc.put(ResponseCodes.Invalid_Password.getValue(), "Invalid user name or PIN");
        codeToDesc.put(ResponseCodes.Session_Exipred.getValue(), "Session Expired");
        codeToDesc.put(ResponseCodes.OTP_Failed.getValue(),"Entered Invalid OTP (Expiry or OTP mismatch)");
        codeToDesc.put(ResponseCodes.Invalid_PIN.getValue(),"Entered Invalid PIN");
        codeToDesc.put(ResponseCodes.Invalid_Request.getValue(),"Invalid Request");
        codeToDesc.put(ResponseCodes.Invalid_Mobile.getValue(),"Invalid Mobile Number");
        codeToDesc.put(ResponseCodes.Invalid_OrderID.getValue(),"Invalid alamin Txnid");

	}

	public Map<Integer, String> getCodeToStatus() {
		return codeToStatus;
	}

	static public Map<String, String> getErrorCode() {
		return statusMessageToCode;
	}
	
	static public Map<Integer, String> getCodeToDesc(){
		return codeToDesc;
	}
}
