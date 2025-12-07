package com.example.IP_Session_001.dto.request;


import lombok.Data;
import java.util.Set;

@Data
public class RegisterRequest {
    private String email;
    private String password;
    private Set<String> roles; // e.g., ["USER", "ADMIN"]
}
