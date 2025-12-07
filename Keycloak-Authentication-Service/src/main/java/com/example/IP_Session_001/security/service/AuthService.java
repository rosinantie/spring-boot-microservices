package com.example.IP_Session_001.security.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.IP_Session_001.dto.request.AuthRequest;
import com.example.IP_Session_001.repository.UserLoginRepository;
import com.example.IP_Session_001.security.jwtUtils.UserJwtUtils;
import com.example.IP_Session_001.security.user.UserDetailsImpl;
import jakarta.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserLoginRepository userLoginRepository;

    @Autowired
    private UserJwtUtils jwtUtils;

    public Map<String, Object> authenticateUser(@Valid AuthRequest authRequest) {

        log.info("Authentication attempt started for email: {}", authRequest.getEmail());

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getEmail(),
                            authRequest.getPassword()
                    )
            );
            log.info("Authentication successful for email: {}", authRequest.getEmail());
        } catch (Exception e) {
            log.error("Authentication failed for email: {}", authRequest.getEmail(), e);
            throw e;
        }

        log.info("AuthService - Authenticating user: {}", authRequest.getEmail());
                    authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );
        log.info("AuthService - Authentication successful for user: {}", authRequest.getEmail());
        String jwt = jwtUtils.generateJwtToken(authentication);
        log.info("AuthService - JWT generated for user: {}", jwt);

        log.debug("JWT generated successfully for user: {}", authRequest.getEmail());

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(a -> a.getAuthority())
                .collect(Collectors.toList());

        log.debug("UserDetails loaded: id={}, username={}, roles={}",
                userDetails.getId(), userDetails.getUsername(), roles);

        Map<String, Object> response = new HashMap<>();
        response.put("accessToken", jwt);
        response.put("type", "Bearer");
        response.put("id", userDetails.getId());
        response.put("username", userDetails.getUsername());
        response.put("email", userDetails.getEmail());
        response.put("roles", roles);

        // Account status log
        userLoginRepository.findByEmailIgnoreCase(authRequest.getEmail()).ifPresent(user -> {
            log.debug("UserLogin found for email: {}", authRequest.getEmail());
            response.put("accounstatus", user.getAccountStatus());
        });


        log.info("Login response prepared successfully for user: {}", authRequest.getEmail());
        return response;
    }

}
