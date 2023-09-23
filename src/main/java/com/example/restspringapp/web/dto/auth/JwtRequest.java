package com.example.restspringapp.web.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class JwtRequest {
    @Email(message = "Email must be correct")
    private String email;
    @NotNull(message = "Password must be not null")
    private String password;
}
