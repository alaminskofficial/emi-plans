package com.alamin.emi.security;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.alamin.emi.exceptions.InternalServerError;
import com.alamin.emi.exceptions.InvalidRequestException;
import com.alamin.emi.utils.Logging;
import com.alamin.emi.utils.RedisCacheManager;

@Component
public class DataDecryptor {

	@Autowired
	Environment env;
	Gson gson = new Gson();
	@Autowired
	RedisCacheManager cacheManager;

	public String decryptData(EncryptedData encryptedData) throws InvalidKeyException, IllegalBlockSizeException,
			BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidRequestException {

		try {
			byte[] rawKey = org.bouncycastle.util.encoders.Base64.decode(encryptedData.getKEY());
			// String base64PrivateKey =
			// Base64.getEncoder().encodeToString(RSAUtil.readKeyFromFile(env.getProperty("privateKeyPath")));
			String base64PrivateKey = cacheManager.getPrivateKey();
			byte[] decryptedSessionKey = decryptKey(rawKey, getPrivateKey(base64PrivateKey));
			Logging.getInfoLog().info("saving back decrypted session ket to obj ");
			encryptedData.setKEY(Base64.getEncoder().encodeToString((decryptedSessionKey)));
			byte[] decryptedData = decryptPayload(encryptedData, decryptedSessionKey);
			Logging.getInfoLog().info("Decrypted Payload : " + new String((decryptedData)));
			return new String(decryptedData);
		} catch (Exception e) {
			Logging.getInfoLog().info("Data can't be decrypted !");
			throw new InvalidRequestException("Invalid Request");
		}
	}

	public String encryptResponsePayload(String respPayload, EncryptedData encryptedData)
			throws Exception, NoSuchProviderException, NoSuchPaddingException {
		try {
//				byte[] rawKey = org.bouncycastle.util.encoders.Base64.decode(encryptedData.getKEY());
//		    	String base64PrivateKey = Base64.getEncoder().encodeToString(RSAUtil.readKeyFromFile(env.getProperty("privateKeyPath")));
			byte[] decryptedSessionKey = Base64.getDecoder().decode(encryptedData.getKEY().getBytes());
			Security.addProvider(new BouncyCastleProvider());
			Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", BouncyCastleProvider.PROVIDER_NAME);
			SecretKey key = new SecretKeySpec(decryptedSessionKey, "AES");
			byte[] iv = Arrays.copyOfRange(org.bouncycastle.util.encoders.Base64.decode(encryptedData.getIV()), 0,
					cipher.getBlockSize());
			AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);
			cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
			byte[] encryptedRespPayload = cipher.doFinal(respPayload.getBytes());
			String encRespPayload = new String(org.bouncycastle.util.encoders.Base64.encode(encryptedRespPayload));
			Logging.getInfoLog().info("EncRespPayload : " + encRespPayload);
			return encRespPayload;
		} catch (Exception e) {
			Logging.getInfoLog().info("Data can't be Encrypted while giving response to app !");
			throw new InternalServerError("Internal server error");
		}
	}

	private static PrivateKey getPrivateKey(String base64PrivateKey) {
		PrivateKey privateKey = null;
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(base64PrivateKey.getBytes()));
		KeyFactory keyFactory = null;
		try {
			keyFactory = KeyFactory.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		try {
			privateKey = keyFactory.generatePrivate(keySpec);
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return privateKey;
	}

	private static byte[] decryptKey(byte[] data, PrivateKey privateKey)
			throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException,
			IllegalBlockSizeException, UnsupportedEncodingException {
		Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return (cipher.doFinal(data));
	}

	private static byte[] decryptPayload(EncryptedData encryptedData, byte[] decryptedKey)
			throws Exception, InvalidAlgorithmParameterException {
		Security.addProvider(new BouncyCastleProvider());
		Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", BouncyCastleProvider.PROVIDER_NAME);
		byte[] iv = Arrays.copyOfRange(org.bouncycastle.util.encoders.Base64.decode(encryptedData.getIV()), 0,
				cipher.getBlockSize());
		AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);
		SecretKey key = new SecretKeySpec(decryptedKey, "AES");
		cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
		return cipher.doFinal(org.bouncycastle.util.encoders.Base64.decode(encryptedData.getPAYLOAD()));
		// return Base64.getEncoder().encodeToString(decryPtedPayload);
	}

}
