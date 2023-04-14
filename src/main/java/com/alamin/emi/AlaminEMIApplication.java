package com.alamin.emi;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import com.alamin.emi.utils.Logging;

@EnableRabbit
@SpringBootApplication
@EntityScan("com.alamin.*")
public class AlaminEMIApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlaminEMIApplication.class, args);
		Logging.getInfoLog().info("*** alamin EMI Application Started ***");
	}

}
