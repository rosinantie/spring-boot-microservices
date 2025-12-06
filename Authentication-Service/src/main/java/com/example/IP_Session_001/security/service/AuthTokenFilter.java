package com.example.IP_Session_001.security.service;

import java.io.IOException;

import com.example.IP_Session_001.security.jwtUtils.UserJwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private UserJwtUtils userJwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

   private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @SuppressWarnings("null")
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            log.info("AuthTokenFilter - Parsed JWT: {}", jwt);

            if (jwt != null) {
                log.info("AuthTokenFilter - Validating JWT");
                if (userJwtUtils.validateJwtToken(jwt)) {
                    String userId = userJwtUtils.getUserIdFromJwtToken(jwt);
                    log.info("AuthTokenFilter - JWT valid. User ID from token: {}", userId);

                    UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(userId);
                    log.info("AuthTokenFilter - Loaded UserDetails: {}", userDetails);

                    if (userDetails != null) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        log.info("AuthTokenFilter - Authentication set in SecurityContext for userId: {}", userId);
                    }
                } else {
                    log.warn("AuthTokenFilter - JWT validation failed for token: {}", jwt);
                }
            } else {
                log.warn("AuthTokenFilter - No JWT found in request");
            }
        } catch (Exception e) {
            log.error("AuthTokenFilter - Cannot set user authentication: {}", e.getMessage(), e);
        }

        filterChain.doFilter(request, response);
    }


    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}
