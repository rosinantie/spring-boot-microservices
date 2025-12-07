package com.example.IP_Session_001.security.config;

import com.example.IP_Session_001.security.service.AuthEntryPointJwt;
import com.example.IP_Session_001.security.service.AuthTokenFilter;
import com.example.IP_Session_001.security.service.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
// import org.springframework.security.authentication.ProviderManager;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
// import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.cors.CorsConfiguration;


import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableMethodSecurity
@Slf4j
public class WebSecurityConfig {

    @Autowired
    UserDetailsServiceImpl userDetailsServiceImpl;


    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    // @Autowired
    // private UrlValidationFilter urlValidationFilter;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider userAuthenticationProvider() {
        DaoAuthenticationProvider authProviderUser = new DaoAuthenticationProvider();
        authProviderUser.setUserDetailsService(userDetailsServiceImpl);
        authProviderUser.setPasswordEncoder(passwordEncoder());
        return authProviderUser;
    }

    @SuppressWarnings("null")
    @Bean
    public AuthenticationManager authenticationManager(
            DaoAuthenticationProvider userAuthenticationProvider,
            DaoAuthenticationProvider clientAuthenticationProvider) {
        return authentication -> {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                    .getRequest();
            String authType = request.getHeader("Auth-Type");
            log.info("WebSecurityConfig - Auth-Type header: {}", authType);
            if ("user".equalsIgnoreCase(authType)) {
                log.info("WebSecurityConfig - Using userAuthenticationProvider");
                return userAuthenticationProvider.authenticate(authentication);
            } else {
                log.warn("WebSecurityConfig - Invalid Auth-Type: {}", authType);
                throw new AuthenticationServiceException("Invalid authentication request");
            }
        };

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/user-login/**").permitAll()
                        .requestMatchers("/roles/**").permitAll()
                        .anyRequest().authenticated());

        http.cors(cors -> cors.configurationSource(request -> {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.addAllowedOriginPattern("*"); // Allow all origins
            configuration.addAllowedMethod("*"); // Allow all HTTP methods
            configuration.addAllowedHeader("*"); // Allow all headers
            configuration.setAllowCredentials(true); // Allow credentials
            return configuration;
        }))
        ;
        // http.addFilterBefore(urlValidationFilter,
        // UsernamePasswordAuthenticationFilter.class);

        http.authenticationProvider(userAuthenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
