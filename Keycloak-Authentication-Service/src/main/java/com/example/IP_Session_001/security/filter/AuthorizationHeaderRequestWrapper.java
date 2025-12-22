package com.example.IP_Session_001.security.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

public class AuthorizationHeaderRequestWrapper extends HttpServletRequestWrapper {

    private final String authorizationHeader;

    public AuthorizationHeaderRequestWrapper(
            HttpServletRequest request,
            String authorizationHeader
    ) {
        super(request);
        this.authorizationHeader = authorizationHeader;
    }

    @Override
    public String getHeader(String name) {
        if ("Authorization".equalsIgnoreCase(name)) {
            return authorizationHeader;
        }
        return super.getHeader(name);
    }
}
