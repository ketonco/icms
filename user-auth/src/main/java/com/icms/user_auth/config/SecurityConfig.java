package com.icms.user_auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/*
 * Security configuration for the application.
 *
 * This configuration ensures that security measures such as authentication and authorization are applied to the application endpoints.
 * 
 * This configuration is excluded when the 'task' profile is active.
 * 
 * this allows the application to bypass security configurations when running task-related operations. by example, scheduled tasks or background jobs can run without being affected by security constraints.
 */
@Configuration
@Profile("!task") // Exclude this configuration when the 'task' profile is active because it is not needed for task-related operations
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/v1/auth/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form.disable())
            .httpBasic(httpBasic -> {});

        return http.build();
    }
}
