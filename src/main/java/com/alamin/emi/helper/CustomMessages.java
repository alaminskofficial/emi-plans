package com.alamin.emi.helper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;

import com.alamin.emi.constants.EMIConstants;


@Configuration
public class CustomMessages {

	static Map<Integer, String> map = new HashMap<>();

	@PostConstruct
	public void prepare() {
		Properties properties = new Properties();
		try {
			properties.load(CustomMessages.class.getClassLoader().getResourceAsStream(EMIConstants.MESSAGE_PROPERTIES));
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (String key : properties.stringPropertyNames()) {
			map.put(Integer.valueOf(key), properties.getProperty(key));
		}

	}

	public String get(Integer key) {
		return map.get(key);
	}
}
