package com.example.IP_Session_001.controller;

import com.example.IP_Session_001.common.AppResponse;
import com.example.IP_Session_001.dto.request.AuthRequest;
import com.example.IP_Session_001.dto.request.KeyCloakUserRequest;
import com.example.IP_Session_001.dto.request.UserLoginRequest;
import com.example.IP_Session_001.entity.UserLogin;
import com.example.IP_Session_001.repository.UserLoginRepository;
import com.example.IP_Session_001.security.service.AuthService;
import com.example.IP_Session_001.security.user.UserDetailsImpl;
import com.example.IP_Session_001.service.KeycloakUserService;
import com.example.IP_Session_001.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;
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
    private final KeycloakUserService keycloakUserService;


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

    @PostMapping("/create-keycloak")
    public ResponseEntity<?> createUser(@RequestBody KeyCloakUserRequest request) {

        Object keycloakUserCreateResponse = keycloakUserService.createUser(
                request.getEmail(),
                request.getPassword(),
                request.getFirstName(),
                request.getLastName(),
               request.getRoles()
        );

        return ResponseEntity.ok(
                Map.of(
                        "message", "User created successfully",
                        "userId",keycloakUserCreateResponse
                )
        );
    }

    @PostMapping("/signin-keycloak")
    public ResponseEntity<?> signinKeycloak(@RequestBody AuthRequest request) {
        try {
            Map<String, Object> token = keycloakUserService.generateToken(
                    request.getEmail(),
                    request.getPassword()
            );

            return ResponseEntity.ok(
                    Map.of(
                            "message", "User signed in successfully",
                            "token", token
                    )
            );

        } catch (Exception e) {
            log.error("Failed to sign in user: {}", request.getEmail(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "message", "Sign-in failed",
                            "error", e.getMessage()
                    ));
        }
    }

    @GetMapping("/me-keycloak")
    public ResponseEntity<?> getUserDetails(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Missing or invalid Authorization header"));
        }

        try {
            String token = authHeader.substring(7);

            // Split JWT by dots (header.payload.signature) and decode payload
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                throw new RuntimeException("Invalid JWT token format");
            }

            String payloadJson = new String(java.util.Base64.getUrlDecoder().decode(parts[1]));
            // Convert JSON string to Map
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            Map<String, Object> claims = mapper.readValue(payloadJson, Map.class);

            return ResponseEntity.ok(Map.of(
                    "userId", claims.get("sub"),
                    "username", claims.get("preferred_username"),
                    "email", claims.get("email"),
                    "roles", ((Map<?, ?>) claims.getOrDefault("realm_access", Map.of())).get("roles")
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Cannot retrieve user details", "error", e.getMessage()));
        }
    }



    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody AuthRequest loginRequest) {
        Map<String, Object> jwtResponse = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(
                AppResponse.success("User logged in successfully", jwtResponse)
        );
    }

    @GetMapping("/me")
    public Map<String, Object> me(@AuthenticationPrincipal Jwt jwt) {
        if (jwt == null) {
            return Map.of("error", "Unauthorized");
        }

        String userId = jwt.getSubject() != null ? jwt.getSubject() : "";
        String email = jwt.getClaimAsString("email") != null ? jwt.getClaimAsString("email") : "";

        // Extract roles from realm_access
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        List<String> roles = List.of();
        if (realmAccess != null && realmAccess.get("roles") instanceof List) {
            roles = ((List<?>) realmAccess.get("roles"))
                    .stream()
                    .map(Object::toString)
                    .toList();
        }

        return Map.of(
                "userId", userId,
                "email", email,
                "roles", roles
        );
    }




    @GetMapping("/oauth2/success")
    public ResponseEntity<?> oauthSuccess() {
        return ResponseEntity.ok("OAuth2 login successful");
    }

    @GetMapping("/oauth2/failure")
    public ResponseEntity<?> oauthFailure() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("OAuth2 login failed");
    }


    @PreAuthorize("hasRole('ROLE_ORGADMIN') or hasRole('ROLE_Admin')")
    @GetMapping("/details")
    public ResponseEntity<?> getOrganizationDetails() {
log.info("getOrganizationDetails IN");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        UUID userId = userDetails.getId();
        log.info("uuid:{}",userId);
        Optional<UserLogin> user = userLoginRepository.findById(userId);
        return ResponseEntity.ok(user.get());
    }
}
