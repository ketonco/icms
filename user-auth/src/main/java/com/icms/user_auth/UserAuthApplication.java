package com.icms.user_auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;

@SpringBootApplication(scanBasePackages = {"com.icms.user_auth", "com.icms.shared"})
@EntityScan(basePackages = {"com.icms.user_auth", "com.icms.shared"})
public class UserAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserAuthApplication.class, args);
	}

}
