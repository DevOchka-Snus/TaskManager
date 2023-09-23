package com.example.restspringapp.web.dto.user;

import com.example.restspringapp.web.dto.validation.OnCreate;
import com.example.restspringapp.web.dto.validation.OnUpdate;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UserDto {
    @NotNull(message = "Id must be not null", groups = OnUpdate.class)
    private Long id;

    @NotNull(message = "First name must be not null", groups = {OnCreate.class, OnUpdate.class})
    @Length(max = 255, message = "First name length must be smaller than 255", groups = {OnCreate.class, OnUpdate.class})
    private String firstname;

    @NotNull(message = "Last name must be not null", groups = {OnCreate.class, OnUpdate.class})
    @Length(max = 255, message = "Last name length must be smaller than 255", groups = {OnCreate.class, OnUpdate.class})
    private String lastname;

    @Length(max = 255, message = "Email length must be smaller than 255", groups = {OnCreate.class, OnUpdate.class})
    @Email(message = "Email must be correct", groups = {OnCreate.class, OnUpdate.class})
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Password must be not null", groups = {OnCreate.class, OnUpdate.class})
    private String password;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Password confirmation must be not null", groups = OnCreate.class)
    private String passwordConfirmation;
}
