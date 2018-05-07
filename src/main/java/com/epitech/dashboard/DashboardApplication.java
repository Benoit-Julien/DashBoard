package com.epitech.dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = UserRepository.class)
public class DashboardApplication {
	private static final String encryptionKey = "encryptor dashboard key";

    public static void main(String[] args) {
    	AES.setKey(encryptionKey);
		SpringApplication.run(DashboardApplication.class, args);
	}
}
