package com.msrfyl.k24.oauth2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Oauth2Application {

	public static void main(String[] args) {
		System.setProperty("user.timezone", "Asia/Jakarta");
		Logger logger = LoggerFactory.getLogger(Oauth2Application.class);
		logger.info("starting application");
		SpringApplication.run(Oauth2Application.class, args);

	}

}
