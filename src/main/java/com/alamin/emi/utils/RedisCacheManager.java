package com.alamin.emi.utils;

import java.lang.reflect.Type;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.alamin.emi.security.KeyToResponse;

import redis.clients.jedis.JedisCommands;

@Service
public class RedisCacheManager {
	@Autowired
	Environment env;
	@Autowired
	private JedisCommands jedis;

	Gson gson = new Gson();

	public <T> T getKeyValue(String key, Type typeOfT) {
		try {
			String cachedString = jedis.get(key);
			T object = new Gson().fromJson(cachedString, typeOfT);
			return object;
		} catch (Exception e) {
			Logging.getErrorLog().error("unable to get key from redis " + key);
			Logging.getErrorLog().error(Logging.getStackTrace(e));
		}
		return null;
	}

	public String getPrivateKey() {
		String apiResp = null;
		KeyToResponse serverKey = null;
		String resp = jedis.get("priKey");
		if (resp == null) {
			try {
				apiResp = HTTPReqHandler.processHttpPost(env.getProperty("PublicKeyGenerateURL"), null,
						"application/json");
			} catch (Exception e) {
				Logging.getErrorLog().error(Logging.getStackTrace(e));
			}
			if (apiResp != null) {
				serverKey = gson.fromJson(apiResp, KeyToResponse.class);
				return serverKey.getPriKey();
			} else {
				return null;
			}
		}
		return resp;
	}

	public String getPublicKey() {
		KeyToResponse serverKey = null;
		String apiResp = null;
		String resp = jedis.get("pubKey");
		if (resp == null) {
			try {
				apiResp = HTTPReqHandler.processHttpPost(env.getProperty("PublicKeyGenerateURL"), null,
						"application/json");
			} catch (Exception e) {
				Logging.getErrorLog().error(Logging.getStackTrace(e));
			}
			if (apiResp != null) {
				serverKey = gson.fromJson(apiResp, KeyToResponse.class);
				return serverKey.getPubKey();
			} else {
				return null;
			}
		}
		return resp;
	}

}
