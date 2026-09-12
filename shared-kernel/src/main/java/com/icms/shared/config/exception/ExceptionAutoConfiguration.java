package com.icms.shared.config.exception;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icms.shared.exceptions.CustomSecurityExceptionHandler;

@AutoConfiguration 
public class ExceptionAutoConfiguration {

    @Bean 
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    @Bean
    public CustomSecurityExceptionHandler customSecurityExceptionHandler(ObjectMapper objectMapper) {
        return new CustomSecurityExceptionHandler(objectMapper);
    }

}
