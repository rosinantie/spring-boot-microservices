package com.example.IP_Session_001.dto;

import java.util.UUID;

public record CustomerResponse(
                UUID id,
                String firstName,
                String lastName,
                String email,
                String mobile) {
}
