package com.grouptwo.email;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class EmailSendingApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmailSendingApplication.class, args);
	}
}
