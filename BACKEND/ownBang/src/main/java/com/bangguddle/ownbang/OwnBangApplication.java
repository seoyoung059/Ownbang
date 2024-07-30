package com.bangguddle.ownbang;

import com.bangguddle.ownbang.domain.search.service.LocationApiService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.bangguddle.ownbang.domain")
@EnableElasticsearchRepositories(basePackages = "com.bangguddle.ownbang.domain.search.repository")

public class OwnBangApplication {

	public static void main(String[] args) {
		SpringApplication.run(OwnBangApplication.class, args);
	}

	// school data 입력
	@Bean
//	public CommandLineRunner run(SchoolApiService schoolApiService) {
//		return args -> {
//			schoolApiService.getSchoolInfo();
//		};
//	}
	// location data 입력
	public CommandLineRunner run(LocationApiService locationApiService) {
		return args -> {
			locationApiService.getLocationInfo();
		};
	}
}
