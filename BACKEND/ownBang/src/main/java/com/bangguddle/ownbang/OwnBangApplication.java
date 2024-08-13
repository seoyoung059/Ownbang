package com.bangguddle.ownbang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class OwnBangApplication {

	public static void main(String[] args) {
		SpringApplication.run(OwnBangApplication.class, args);
	}

}
