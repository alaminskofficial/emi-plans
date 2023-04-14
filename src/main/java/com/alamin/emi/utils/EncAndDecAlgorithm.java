package com.alamin.emi.utils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class EncAndDecAlgorithm {

	@Autowired
	Environment env;
	private static final String INVALID_INPUT_STRING = "Invalid input String";

	/**
	 * Constructor
	 */
	public EncAndDecAlgorithm() {
	}

	/**
	 * This method is used to init encryption
	 * 
	 * @param key
	 * @param mode cipherencryption or decryption mode
	 * @throws Exception
	 */
	public Cipher initCipher(String key, int mode)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
		try {
			SecretKeySpec skeySpec = new SecretKeySpec(HexUtil.HexfromString(key), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(mode, skeySpec);
			return cipher;
		} catch (NoSuchAlgorithmException nsae) {
			throw new NoSuchAlgorithmException("Invalid Java Version");
		} catch (NoSuchPaddingException nse) {
			throw new NoSuchPaddingException("Invalid Key");
		}
	}

	/**
	 * This method is used to return encrypted data.
	 * 
	 * @param instr
	 * @return String
	 * @throws IllegalBlockSizeException
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws Exception
	 */

	public String encrypt(String message, String enckey) throws BadPaddingException, IllegalBlockSizeException,
			InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
		try {
			Cipher cipher = initCipher(enckey, Cipher.ENCRYPT_MODE);
			byte[] encstr = cipher.doFinal(message.getBytes());
			return HexUtil.HextoString(encstr);
		} catch (BadPaddingException nse) {
			throw new BadPaddingException();
		}
	}

	public String encrypt(String message) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException {
		return encrypt(message, env.getRequiredProperty("AES_Working_Key"));
	}

	/**
	 * This method is used to return decrypted data.
	 * 
	 * @param instr
	 * @return String
	 * @throws Exception
	 */
	public String decrypt(String message, String decKey) throws BadPaddingException, IllegalBlockSizeException,
			InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
		try {
			Cipher cipher = initCipher(decKey, Cipher.DECRYPT_MODE);
			byte[] encstr = cipher.doFinal(HexUtil.HexfromString(message));
			return new String(encstr);
		} catch (BadPaddingException nse) {
			throw new BadPaddingException(INVALID_INPUT_STRING);
		}
	}

	public String decrypt(String message) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException {
		return decrypt(message, env.getRequiredProperty("AES_Working_Key"));
	}

	public static void main(String[] args) throws Exception {
		EncAndDecAlgorithm algo = new EncAndDecAlgorithm();
		System.out.println(algo.encrypt("postgres@123", "142DCBEBF6F34F8519D5EF41213F6334"));
		System.out.println(algo.encrypt("welcome", "142DCBEBF6F34F8519D5EF41213F6334"));
	}

}
