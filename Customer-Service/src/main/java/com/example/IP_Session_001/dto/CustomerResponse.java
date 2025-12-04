package com.example.IP_Session_001.dto;

public record CustomerResponse(
                Long id,
                String firstName,
                String lastName,
                String email,
                String mobile) {
}
