package com.bangguddle.ownbang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

public class OwnBangApplication {

	public static void main(String[] args) {
		SpringApplication.run(OwnBangApplication.class, args);
	}


	// school data 입력 (25044개)
//	@Bean
//	public CommandLineRunner run(SchoolApiService schoolApiService) {
//		return args -> {
//			schoolApiService.getSchoolInfo();
//		};
//	}
//	// location data 입력 (49863개)
//	public CommandLineRunner run(LocationApiService locationApiService) {
//		return args -> {
//			locationApiService.getLocationInfo();
//		};
//	}
	// STATION DATA 입력 (1070개)
//	public CommandLineRunner run( SearchService searchService) {
//		return args -> {
//			searchService.importExcelData();
//		};
//	}
//
//	UNIVERSITY
//	public CommandLineRunner run(UniversityApiService universityApiService) {
//		return args -> {
//			universityApiService.getUniversityInfo();
//		};
//	}
}
