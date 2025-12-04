package com.example.IP_Session_001.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CustomerRequest(
                @NotBlank(message = "First name is required") String firstName,

                String lastName,

                @Email @NotBlank(message = "Email is required") String email,

                String mobile) {
}
