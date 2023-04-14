package com.alamin.emi.config;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import com.alamin.emi.utils.EncAndDecAlgorithm;
import com.alamin.emi.utils.Logging;



	@Configuration
public class RemoteQueueConfiguration {
		@Autowired
		Environment env;

		@Autowired
		EncAndDecAlgorithm crypto;
		
		
		public static final String EMI_TRNASCTION_STATUS_QUEUE= "emi.transaction.status.queue";
		
		@Bean
	    @ConditionalOnProperty(name="rabbit.mq.local", havingValue="false", matchIfMissing = true)
	    public ConnectionFactory remoteConnectionFactory() throws KeyManagementException, NoSuchAlgorithmException {
	        try {
	            Logging.getInfoLog().info("Intializing remote rabbit MQ");
	            CachingConnectionFactory remoteConnectionFactory = new CachingConnectionFactory(
	                    env.getRequiredProperty("remote.queue.server.domainOrIp"));
	            remoteConnectionFactory.getRabbitConnectionFactory().useSslProtocol();
	            remoteConnectionFactory.getRabbitConnectionFactory().setVirtualHost("/");
	            remoteConnectionFactory.setPort(Integer.parseInt(env.getRequiredProperty("remote.queue.port")));
	            remoteConnectionFactory.setUsername(env.getRequiredProperty("remote.queue.userName"));
	            remoteConnectionFactory.setPassword(crypto.decrypt(env.getRequiredProperty("remote.queue.password")));
	            declareQueues(remoteConnectionFactory);
	            return remoteConnectionFactory;
	        }catch (Exception e) {
	            Logging.getErrorLog().error(Logging.getStackTrace(e));
	        }
	        return null;
	    }
	    
	    
	    @Bean
	    @ConditionalOnProperty(name="rabbit.mq.local", havingValue="true")
	    public ConnectionFactory localConnectionFactory() throws KeyManagementException, NoSuchAlgorithmException {
	        try {
	            Logging.getInfoLog().info("Intializing local rabbit MQ");
	            CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
	            declareQueues(connectionFactory);
	            return connectionFactory;
	        }catch (Exception e) {
	            Logging.getErrorLog().error(Logging.getStackTrace(e));
	        }
	        return null;
	    }
	    
	    @Autowired
	    private ConnectionFactory connectionFactory;
	    
	    private void declareQueues(CachingConnectionFactory connectionFactory) {
			  AmqpAdmin amqpAdmin = new RabbitAdmin(connectionFactory);
			  amqpAdmin.declareQueue(new Queue(EMI_TRNASCTION_STATUS_QUEUE));
	    }
	    
	    @Bean(name="remoteTemplate")
		  @Primary
		   public RabbitTemplate remoteRabbitTemplate() throws Exception {
		        return new RabbitTemplate(connectionFactory);
	   }

		@Bean
		public SimpleRabbitListenerContainerFactory listenerContainer()
				throws KeyManagementException, NoSuchAlgorithmException {
			SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
			factory.setConnectionFactory(connectionFactory);
			return factory;
		}

}
