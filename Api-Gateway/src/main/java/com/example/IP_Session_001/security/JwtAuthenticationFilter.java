package com.example.IP_Session_001.security;

import com.example.IP_Session_001.security.jwtUtils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        log.info("Incoming Request Path: {}", path);

        // allow login/register APIs without JWT
        if (path.contains("/user-login/signin") || path.contains("/user-login/signup")) {
            log.info("Public API accessed: {}", path);
            return chain.filter(exchange);
        }

        // validate Authorization header
        if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
            log.warn("Authorization header missing!");
            return unauthorized(exchange);
        }

        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        log.info("Authorization Header Received: {}", authHeader);

        if (!authHeader.startsWith("Bearer ")) {
            log.warn("Invalid Authorization header format!");
            return unauthorized(exchange);
        }

        String token = authHeader.substring(7);
        log.info("Extracted JWT Token: {}", token);

        if (!jwtUtil.validateToken(token)) {
            log.warn("JWT validation failed!");
            return unauthorized(exchange);
        }

        Claims claims = jwtUtil.extractAllClaims(token);
        log.info("JWT Validated. User: {}, Roles: {}", claims.getSubject(), claims.get("roles"));

        // Forward user data
        String roles = claims.get("roles") != null ? claims.get("roles").toString() : "";
        ServerHttpRequest modifiedRequest = request.mutate()
                .header("X-USER", claims.getSubject())
                .header("X-ROLES", roles)
                .build();


        return chain.filter(exchange.mutate().request(modifiedRequest).build());
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        log.error("Unauthorized request blocked");
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return -1; // run before other filters
    }
}
