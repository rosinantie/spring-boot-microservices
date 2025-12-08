package com.example.IP_Session_001.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class KeycloakUserCreateResponse {
    private String userId;
    private Map<String, Object> token;
}
