package com.example.IP_Session_001.controller;

import com.example.IP_Session_001.dto.request.AuthRequest;
import com.example.IP_Session_001.dto.request.UserLoginRequest;
import com.example.IP_Session_001.entity.UserLogin;
import com.example.IP_Session_001.repository.UserLoginRepository;
import com.example.IP_Session_001.security.service.AuthService;
import com.example.IP_Session_001.security.user.UserDetailsImpl;
import com.example.IP_Session_001.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/user-login")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final AuthService authService;
    private final UserLoginRepository userLoginRepository;


    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserLoginRequest userRequest) {
        try {
            userService.saveUser(userRequest);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("User registered successfully with email: " + userRequest.getEmail());
        } catch (Exception e) {
            log.error("Error registering user: {}", userRequest, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to register user: " + e.getMessage());
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody AuthRequest loginRequest) {
        Map<String, Object> jwtResponse = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok("user looged successfully"+jwtResponse);
    }

    @PreAuthorize("hasRole('ROLE_ORGADMIN') or hasRole('ROLE_Admin')")
    @GetMapping("/details")
    public ResponseEntity<?> getOrganizationDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        UUID userId = userDetails.getId();
        Optional<UserLogin> user = userLoginRepository.findById(userId);
        return ResponseEntity.ok(user.get());
    }
    }
