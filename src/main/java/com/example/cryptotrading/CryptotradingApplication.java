package com.example.cryptotrading;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CryptotradingApplication {

	public static void main(String[] args) {
		SpringApplication.run(CryptotradingApplication.class, args);
	}

}
