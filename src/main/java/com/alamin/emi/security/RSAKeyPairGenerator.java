package com.alamin.emi.security;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;

import com.alamin.emi.utils.Logging;

//@Service
public class RSAKeyPairGenerator {

	private PrivateKey privateKey;
	private PublicKey publicKey;
	@Autowired
	private Environment env;

	@Scheduled(cron = "${reGenarate_KeyPair}")
	public void reGenerateKeyPair() throws NoSuchAlgorithmException, IOException {
		try {
			Logging.getInfoLog().info("Re-Generating Key Pair !");
			RSAKeyPairGenerator keyPairGenerator = new RSAKeyPairGenerator();
			keyPairGenerator.writeToFile(env.getProperty("publicKeyPath"),
					keyPairGenerator.getPublicKey().getEncoded());
			keyPairGenerator.writeToFile(env.getProperty("privateKeyPath"),
					keyPairGenerator.getPrivateKey().getEncoded());
			Logging.getInfoLog().info(
					"Public Key : " + Base64.getEncoder().encodeToString(keyPairGenerator.getPublicKey().getEncoded()));
		} catch (Exception e) {
			Logging.getErrorLog().error(Logging.getStackTrace(e));
		}
	}

	public RSAKeyPairGenerator() throws NoSuchAlgorithmException {
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		keyGen.initialize(1024); // 16384
		KeyPair pair = keyGen.generateKeyPair();
		this.privateKey = pair.getPrivate();
		this.publicKey = pair.getPublic();
	}

	public void writeToFile(String path, byte[] key) throws IOException {

		File f = new File(path);
		f.getParentFile().mkdirs();

		FileOutputStream fos = new FileOutputStream(f);
		fos.write(key);
		fos.flush();
		fos.close();
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

}
