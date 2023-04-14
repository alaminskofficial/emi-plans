package com.alamin.emi.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import com.alamin.emi.utils.Logging;

public class RSAUtil {
	RSAUtil() {

	}

	private static final String padding = "RSA/ECB/OAEPWithSHA-1AndMGF1Padding";

	public static byte[] encrypt(byte[] data, String publicKey) throws BadPaddingException, IllegalBlockSizeException,
			InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
		Cipher cipher = Cipher.getInstance(padding);
		cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey));
		return cipher.doFinal(data);
	}

	public static PublicKey getPublicKey(String base64PublicKey) {
		PublicKey publicKey = null;
		try {
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(base64PublicKey.getBytes()));
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			publicKey = keyFactory.generatePublic(keySpec);
			return publicKey;
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			Logging.getErrorLog().error(Logging.getStackTrace(e));
		}
		return publicKey;
	}

	static byte[] readKeyFromFile(String path, String fileType) {

		if (path != null) {
			File file = new File(path);
			try (FileInputStream fin = new FileInputStream(file)) {
				byte[] fileContent = new byte[(int) file.length()];
				int count = 0;
				while ((count = fin.read(fileContent)) > 0) {
					//
				}
				return fileContent;
			} catch (IOException e) {
				Logging.getErrorLog().error(Logging.getStackTrace(e));
			}
			return new byte[0];
		} else {
			ClassPathResource classPathResource = new ClassPathResource(fileType);
			byte[] binaryData = null;
			try {
				binaryData = FileCopyUtils.copyToByteArray(classPathResource.getInputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Logging.getErrorLog().error(Logging.getStackTrace(e));
			}
			return binaryData;
		}
	}

	public void writeToFile(String path, byte[] key) throws IOException {
		File f = new File(path);
		f.getParentFile().mkdirs();
		try (FileOutputStream fos = new FileOutputStream(f)) {
			fos.write(key);
			fos.flush();
		} catch (Exception e) {
			Logging.getErrorLog().error(Logging.getStackTrace(e));
		}
	}

}
