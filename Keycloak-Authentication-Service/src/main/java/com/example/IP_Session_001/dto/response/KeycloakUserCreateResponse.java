package com.example.IP_Session_001.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class KeycloakUserCreateResponse {
    private String userId;
    private List<String> roles;
    private Map<String, Object> token;
}
