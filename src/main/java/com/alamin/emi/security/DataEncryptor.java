package com.alamin.emi.security;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.alamin.emi.utils.Logging;
import com.alamin.emi.utils.RedisCacheManager;

@Component
public class DataEncryptor {
	@Autowired
	Environment env;
	@Autowired
	GetPublicKey gKey;
	@Autowired
	RedisCacheManager cacheManager;

	private static final SecureRandom secureRandom = new SecureRandom();

	/*
	 * @RequestMapping("/EncodeData") public Map<String, String> encryptedData()
	 * throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
	 * InvalidAlgorithmParameterException, IllegalBlockSizeException,
	 * BadPaddingException, NoSuchProviderException, IOException { return
	 * encrypt("Hi"); }
	 */
	public Map<String, String> encrypt(String stringToBeEncrypted) throws NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IOException,
			IllegalBlockSizeException, BadPaddingException, NoSuchProviderException {
		Map<String, String> result = new HashMap<>(3);
		byte[] rawKey = new byte[16];
		secureRandom.nextBytes(rawKey);
		try {
			Security.addProvider(new BouncyCastleProvider());
			Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", BouncyCastleProvider.PROVIDER_NAME);
			SecretKey key = new SecretKeySpec(rawKey, "AES");
			byte[] iv = new byte[cipher.getBlockSize()];
			secureRandom.nextBytes(iv);
			AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);
			cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
			byte[] encryptedData = cipher.doFinal(stringToBeEncrypted.getBytes());

//				String dynamicKeyGenURL = "http://localhost:8195/HDFC/getKey";
//				String dynamicEncondingkey = HTTPGetHandler.processHttpGet(dynamicKeyGenURL);
			// String dynamicEncondingkey =gKey.getKey();
			String dynamicEncondingkey = cacheManager.getPublicKey();
			// Logging.getInfoLog().info("rawkey : " + new String(Base64.encode(rawKey)));
			byte[] encrtptedSessionKey = RSAUtil.encrypt(rawKey, dynamicEncondingkey);
			result.put("IV", new String(Base64.encode(iv)));
			result.put("KEY", new String(Base64.encode(encrtptedSessionKey)));
			result.put("PAYLOAD", new String(Base64.encode(encryptedData)));
			Logging.getInfoLog().info("Encrypted Payloads : " + result);
			return result;
		} catch (Exception e) {
			Logging.getErrorLog().error(Logging.getStackTrace(e));
		}
		return result;
	}

	public static String getPublicKey(String base64PublicKey) {
		PublicKey publicKey = null;
		try {
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(
					java.util.Base64.getDecoder().decode(base64PublicKey.getBytes()));
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			publicKey = keyFactory.generatePublic(keySpec);
			return java.util.Base64.getEncoder().encodeToString(publicKey.getEncoded());
		} catch (Exception e) {
			Logging.getErrorLog().error(Logging.getStackTrace(e));
		}
		return null;
	}

}
