package com.alamin.emi.utils;


import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;
import com.rabbitmq.client.Channel;

@Component
public class QueueUtils {
	
	public void processAck(boolean isProcessDone,Message message,Channel channel) {
		try {
			if(channel.isOpen()) {
				if(isProcessDone) {
					channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
					Logging.getInfoLog().info("Ack Done !");
				}else {
					channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
					Logging.getInfoLog().info("Ack pending !");
				}
			}else {
				Logging.getInfoLog().info("channel closed !");
			}
			
		}catch (Exception e) {
			Logging.getInfoLog().error(Logging.getStackTrace(e));
		}
		
	}
}
