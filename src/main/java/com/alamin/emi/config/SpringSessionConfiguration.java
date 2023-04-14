package com.alamin.emi.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.session.SessionRepository;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;

import com.alamin.emi.utils.Logging;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;

@Configuration
public class SpringSessionConfiguration {

	@Autowired
	private Environment env;
	@Autowired
	private RedisTemplate redisTemplate;

	@Bean
	public SessionRepository sessionRepository() {
		RedisIndexedSessionRepository sessionRepository = new RedisIndexedSessionRepository(redisTemplate);
		sessionRepository.setDefaultMaxInactiveInterval(
				Integer.parseInt(env.getRequiredProperty("spring.session.redis.maxInactiveInterval.seconds")));
		return sessionRepository;
	}

	private Set<HostAndPort> getNodes() {
		Set<HostAndPort> jedisClusterNode = new HashSet<>();
		HostAndPort hostNode = new HostAndPort(env.getProperty("spring.redis.host"),
				Integer.parseInt(env.getProperty("spring.redis.port")));
		jedisClusterNode.add(hostNode);
		for (String hostAndPort : env.getProperty("spring.redis.cluster.nodes").split(",")) {
			String[] details = hostAndPort.split(":");
			HostAndPort node = new HostAndPort(details[0], Integer.parseInt(details[1]));
			jedisClusterNode.add(node);
		}
		return jedisClusterNode;
	}

	@Bean
	@ConditionalOnProperty(name = "redis.cache.local", havingValue = "false", matchIfMissing = true)
	public JedisCommands getRedisCluster() {
		Logging.getInfoLog().info("Intializing remote Redis");
		return new JedisCluster(getNodes());
	}

	@Bean
	@ConditionalOnProperty(name = "redis.cache.local", havingValue = "true")
	public JedisCommands jedis() {
		Logging.getInfoLog().info("Intializing local Redis");
		return new Jedis();
	}

}
