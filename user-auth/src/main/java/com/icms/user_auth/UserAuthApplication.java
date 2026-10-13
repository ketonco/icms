package com.icms.user_auth;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.scheduling.annotation.EnableAsync;

//@SpringBootApplication(scanBasePackages = {"com.icms.user_auth", "com.icms.shared"})
@SpringBootApplication
@EntityScan(basePackages = {"com.icms.user_auth", "com.icms.shared"})
@EnableAsync 
public class UserAuthApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(UserAuthApplication.class);

		// if detecting the 'task' profile, disable the web server
        if (Arrays.asList(args).contains("--spring.profiles.active=task")) {
            app.setWebApplicationType(WebApplicationType.NONE);
        }
        
        app.run(args);
	}

}
