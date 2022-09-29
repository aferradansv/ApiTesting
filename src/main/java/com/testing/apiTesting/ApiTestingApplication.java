package com.testing.apiTesting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("")
public class ApiTestingApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiTestingApplication.class, args);
	}

}
