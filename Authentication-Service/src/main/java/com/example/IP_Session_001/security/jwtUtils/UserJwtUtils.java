package com.example.IP_Session_001.security.jwtUtils;

import java.security.Key;
import java.util.Date;
import java.util.List;

import com.example.IP_Session_001.security.user.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserJwtUtils {

    @Value("${project.app.jwtSecret}")
    private String jwtSecret;

    @Value("${project.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userPrincipal.getAuthorities()
                .stream()
                .map(a -> a.getAuthority())
                .toList();

        String token = Jwts.builder()
                .setSubject(String.valueOf(userPrincipal.getId()))
                .claim("email", userPrincipal.getEmail())
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

        log.info("UserJwtUtils - Generated JWT for userId {}: {}", userPrincipal.getId(), token);
        return token;
    }

    public Claims extractAllClaims(String token) {
        log.info("UserJwtUtils - Extracting claims from token: {}", token);
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateJwtToken(String token) {
        try {
            extractAllClaims(token);
            log.info("UserJwtUtils - JWT token is valid");
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("UserJwtUtils - Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }


    // Get signing key from Base64 secret
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Extract user ID
    public String getUserIdFromJwtToken(String token) {
        return extractAllClaims(token).getSubject();
    }

    // Extract email
    public String getEmailFromJwtToken(String token) {
        return extractAllClaims(token).get("email", String.class);
    }

    // Extract roles
    public List<String> getRolesFromJwtToken(String token) {
        return extractAllClaims(token).get("roles", List.class);
    }

}
