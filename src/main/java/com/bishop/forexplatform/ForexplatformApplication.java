package com.bishop.forexplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ForexplatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(ForexplatformApplication.class, args);
	}

}
