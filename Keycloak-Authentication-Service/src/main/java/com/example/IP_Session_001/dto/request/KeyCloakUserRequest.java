package com.example.IP_Session_001.dto.request;

import lombok.Data;

@Data
public class KeyCloakUserRequest {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}
