//                  This is for the KeyCloak Authentication and Authorization


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

    private static final String COOKIE_NAME = "AUTH-TOKEN";

    private final JwtDecoder jwtDecoder;

    private final List<String> publicEndpoints = List.of(
            "/user-login/signin-keycloak",
            "/user-login/signup",
            "/user-login/create-keycloak"
    );

    public JwtAuthFilter(
            @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
            String issuerUri
    ) {
        this.jwtDecoder = NimbusJwtDecoder.withIssuerLocation(issuerUri).build();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();
        log.info("Incoming Request Path: {}", path);

        if (publicEndpoints.stream().anyMatch(path::contains)) {
            return chain.filter(exchange);
        }

        String token = extractToken(exchange);

        if (token == null) {
            log.warn("JWT not found in header or cookie");
            return unauthorized(exchange);
        }

        try {
            Jwt jwt = jwtDecoder.decode(token);

            String username = jwt.getClaimAsString("preferred_username");
            Map<String, Object> realmAccess = jwt.getClaim("realm_access");

            String roles = realmAccess != null
                    ? realmAccess.getOrDefault("roles", List.of()).toString()
                    : "";

            log.info("JWT Valid | user={} roles={}", username, roles);

            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .header("X-USER", username)
                    .header("X-ROLES", roles)
                    .build();

            return chain.filter(exchange.mutate().request(modifiedRequest).build());

        } catch (Exception ex) {
            log.error("JWT validation failed", ex);
            return unauthorized(exchange);
        }
    }

    private String extractToken(ServerWebExchange exchange) {

        // 1) Authorization header
        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        // 2) Cookie fallback
        return exchange.getRequest()
                .getCookies()
                .getFirst(COOKIE_NAME) != null
                ? exchange.getRequest().getCookies().getFirst(COOKIE_NAME).getValue()
                : null;
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return -1; // Run early
    }
}

