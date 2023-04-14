package com.alamin.emi.security;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.alamin.emi.utils.HTTPReqHandler;
import com.alamin.emi.utils.Logging;

@RestController
public class GetPublicKey {

	@Autowired
	private Environment env;

	@PostMapping("/getKey")
	public String getKey() throws NoSuchAlgorithmException, IOException {
		Gson gson = new Gson();
		try {
			byte[] publicKey = RSAUtil.readKeyFromFile(env.getProperty("publicKeyPath"), "publicKey");
			if (publicKey == null) {
				Logging.getInfoLog().info("newely generated !");
				RSAKeyPairGenerator keyPairGenerator = new RSAKeyPairGenerator();
				String dynmKeyGenUrl = env.getProperty("DynamicKeyGenURL");
				String param = "appName=" + "HDFC";
				String dynamicEncondingkey = HTTPReqHandler.processHttpPost(dynmKeyGenUrl, param, null, null, null);
				if (dynamicEncondingkey != null) {
					KeyToResponse keyToResp = gson.fromJson(dynamicEncondingkey.toString(), KeyToResponse.class);
					Logging.getInfoLog().info("Got KeyToResponse" + keyToResp);
					if (keyToResp.getPriKey().contains("u003d")) {
						keyToResp.setPriKey(keyToResp.getPriKey().replace("u003d", "="));
					}
					keyPairGenerator.writeToFile(env.getProperty("publicKeyPath"),
							Base64.getDecoder().decode(keyToResp.getPubKey().getBytes()));
					keyPairGenerator.writeToFile(env.getProperty("privateKeyPath"),
							Base64.getDecoder().decode(keyToResp.getPriKey().getBytes()));
					Logging.getInfoLog().info("PK : " + keyToResp.getPubKey());
					return getPublicKey(keyToResp.getPubKey());
				} else {
					Logging.getInfoLog().info("No Response From Server");
				}

			} else {
				Logging.getInfoLog().info("Existing keys !");
				String base64PubKey = getPublicKey(Base64.getEncoder().encodeToString(publicKey));
				Logging.getInfoLog().info("PK : " + base64PubKey);
				return base64PubKey;
			}
		} catch (Exception e) {
			Logging.getErrorLog().error(Logging.getStackTrace(e));
		}
		return null;
	}

	public static String getPublicKey(String base64PublicKey) {
		PublicKey publicKey = null;
		try {
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(base64PublicKey.getBytes()));
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			publicKey = keyFactory.generatePublic(keySpec);
			return Base64.getEncoder().encodeToString(publicKey.getEncoded());
		} catch (Exception e) {
			Logging.getErrorLog().error(Logging.getStackTrace(e));
		}
		return null;
	}

}
