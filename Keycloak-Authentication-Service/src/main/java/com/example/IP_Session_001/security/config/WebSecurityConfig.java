package com.example.IP_Session_001.security.config;

import com.example.IP_Session_001.security.service.CookieAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // Disable CSRF since we are using JWT
                .csrf(csrf -> csrf.disable())

                // Stateless session (REST API)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Authorize requests
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**",
                                "/user-login/**",
                                "/roles/**",
                                "/keycloak-roles/**",          // (optional)
                                "/actuator/**"                 // allow Prometheus to scrape metrics
                        ).permitAll()
                        .anyRequest().authenticated()
                )

                // 👇 COOKIE → AUTHORIZATION HEADER
                .addFilterBefore(
                        new CookieAuthFilter(),
                        BearerTokenAuthenticationFilter.class
                )

                // Enable OAuth2 Resource Server (JWT validation with Keycloak)
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()))


                // CORS configuration
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.addAllowedOriginPattern("*");
                    configuration.addAllowedMethod("*");
                    configuration.addAllowedHeader("*");
                    configuration.setAllowCredentials(true);
                    return configuration;
                }));

        return http.build();
    }
}
