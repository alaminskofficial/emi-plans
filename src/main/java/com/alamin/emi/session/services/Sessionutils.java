package com.alamin.emi.session.services;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

@Service
public class Sessionutils {
	@Autowired
	private SessionRepository redisRepository = null;
	Gson gson = new Gson();
	
	public SessionData readSessionData(String sessionId) {
		String data = null;
		Session session = null;
		try {
			session = redisRepository.findById(sessionId);
			data = session.getAttribute("sessionData");
		}catch (NullPointerException e) {
			return null;
		}
		session.setLastAccessedTime(Instant.now());
		redisRepository.save(session);
		SessionData sessionData = gson.fromJson(data, SessionData.class);
		return sessionData;
		
	}


}
