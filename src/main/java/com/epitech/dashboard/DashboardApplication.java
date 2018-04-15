package com.epitech.dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = UserRepository.class)
public class DashboardApplication {

    public static void main(String[] args) {
		SpringApplication.run(DashboardApplication.class, args);
	}
}
