package com.example.restspringapp.web.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Request for login")
public class JwtRequest {
    @Schema(description = "email", example = "vladfedotov@gmail.ru")
    @Email(message = "Email must be not null")
    private String email;
    @Schema(description = "password", example = "12345")
    @NotNull(message = "Password must be not null")
    private String password;
}
