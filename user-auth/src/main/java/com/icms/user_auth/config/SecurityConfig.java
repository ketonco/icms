package com.icms.user_auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.icms.shared.exceptions.CustomSecurityExceptionHandler;

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
    
    private final CustomSecurityExceptionHandler securityExceptionHandler;

    public SecurityConfig(CustomSecurityExceptionHandler securityExceptionHandler) {
        this.securityExceptionHandler = securityExceptionHandler;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        .csrf(csrf -> csrf.disable())
        .exceptionHandling(exception -> exception
                // FIX: Redirige los fallos de seguridad a tu RestResponse JSON
                .authenticationEntryPoint(securityExceptionHandler)
                .accessDeniedHandler(securityExceptionHandler)
            )
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/error").permitAll()
                .requestMatchers("/api/v1/auth/languages/**").permitAll()

                .anyRequest().authenticated()
            )
            .formLogin(form -> form.disable())
            .httpBasic(httpBasic -> httpBasic.disable());

        return http.build();
    }
}
