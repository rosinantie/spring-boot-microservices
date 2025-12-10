package com.example.IP_Session_001.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class JwtAuthFilter implements GlobalFilter, Ordered {

    private final JwtDecoder jwtDecoder;

    // Public URLs (no JWT required)
    private final List<String> publicEndpoints = List.of(
            "/user-login/signin-keycloak",
            "/user-login/signup",
            "/user-login/create-keycloak"
    );

    public JwtAuthFilter(@Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String issuerUri) {
        this.jwtDecoder = NimbusJwtDecoder.withIssuerLocation(issuerUri).build();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();
        log.info("Incoming Request Path: {}", path);

        // 1) Check public endpoints
        if (publicEndpoints.stream().anyMatch(path::contains)) {
            log.info("Public endpoint allowed: {}", path);
            return chain.filter(exchange);
        }

        // 2) Validate Authorization Header
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Missing or invalid Authorization header");
            return unauthorized(exchange);
        }

        String token = authHeader.substring(7);

        try {
            // 3) Validate JWT with Keycloak
            Jwt jwt = jwtDecoder.decode(token);

            String username = jwt.getClaim("preferred_username");
            Map<String, Object> realmAccess = jwt.getClaim("realm_access");

            String roles = "";
            if (realmAccess != null && realmAccess.get("roles") != null) {
                roles = realmAccess.get("roles").toString();
            }

            log.info("JWT Validated | User: {} | Roles: {}", username, roles);

            // 4) Add user info to downstream request
            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                    .header("X-USER", username)
                    .header("X-ROLES", roles)
                    .build();

            return chain.filter(exchange.mutate().request(modifiedRequest).build());

        } catch (Exception ex) {
            log.error("JWT validation failed: {}", ex.getMessage());
            return unauthorized(exchange);
        }
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
