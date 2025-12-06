package com.example.IP_Session_001.security.jwtUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.List;

@Slf4j
@Component
public class JwtUtil {

    @Value("${project.app.jwtSecret}")
    private String secret;

    private Key getSigningKey() {
        log.debug("Decoding Base64 secret and generating signing key");
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Extract all claims
    public Claims extractAllClaims(String token) {
        log.info("Extracting claims from token");
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Extract user ID (subject)
    public String getUserId(String token) {
        return extractAllClaims(token).getSubject();
    }

    // Extract email
    public String getEmail(String token) {
        return extractAllClaims(token).get("email", String.class);
    }

    // Extract roles
    public List<String> getRoles(String token) {
        return extractAllClaims(token).get("roles", List.class);
    }

    // Validate token
    public boolean validateToken(String token) {
        try {
            extractAllClaims(token);
            log.info("JWT Token is valid");
            return true;
        } catch (Exception e) {
            log.error("JWT Token validation error: {}", e.getMessage());
            return false;
        }
    }
}
