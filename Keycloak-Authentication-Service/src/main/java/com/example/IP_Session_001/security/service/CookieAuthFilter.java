package com.example.IP_Session_001.security.service;

import com.example.IP_Session_001.security.filter.AuthorizationHeaderRequestWrapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class CookieAuthFilter extends OncePerRequestFilter {
    private static final String COOKIE_NAME = "AUTH-TOKEN";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        if (request.getHeader("Authorization") == null) {
            Cookie[] cookies = request.getCookies();

            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (COOKIE_NAME.equals(cookie.getName())) {

                        String token = cookie.getValue();
                        if (token != null && !token.isBlank()) {

                            // ✅ Ensure only ONE Bearer prefix
                            if (!token.startsWith("Bearer ")) {
                                token = "Bearer " + token;
                            }

                            HttpServletRequest wrappedRequest =
                                    new AuthorizationHeaderRequestWrapper(request, token);

                            filterChain.doFilter(wrappedRequest, response);
                            return;
                        }
                    }
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
