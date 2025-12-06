package com.example.IP_Session_001.dto.request;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
}