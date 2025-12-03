package com.example.IP_Session_001.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
<<<<<<< HEAD
                .csrf(csrf -> csrf.disable()) // Disable CSRF
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/actuator/**").permitAll()   // Allow actuator endpoints
                        .requestMatchers("/customers/**").permitAll()  // Allow customer endpoints via API Gateway
                        .anyRequest().authenticated()                  // Any other request requires authentication
=======
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/v3/**", "/swagger-ui/**").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/customers/**").permitAll()
                        .anyRequest().authenticated()
>>>>>>> 9cdcdc0 (Initial monorepo commit)
                );

        return http.build();
    }
}
