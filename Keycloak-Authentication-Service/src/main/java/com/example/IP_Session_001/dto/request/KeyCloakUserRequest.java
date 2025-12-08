package com.example.IP_Session_001.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class KeyCloakUserRequest {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private List<String> roles;
}
