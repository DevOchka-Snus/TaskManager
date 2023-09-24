package com.example.restspringapp.web.controllers;

import com.example.restspringapp.domain.user.User;
import com.example.restspringapp.services.AuthService;
import com.example.restspringapp.services.UserService;
import com.example.restspringapp.web.dto.auth.JwtRequest;
import com.example.restspringapp.web.dto.auth.JwtResponse;
import com.example.restspringapp.web.dto.user.UserDto;
import com.example.restspringapp.web.dto.validation.OnCreate;
import com.example.restspringapp.web.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    private final UserMapper userMapper;

    @PostMapping("/login")
    public JwtResponse login(@Validated @RequestBody JwtRequest request) {
        return authService.login(request);
    }

    @PostMapping("/register")
    public UserDto register(@Validated(OnCreate.class) @RequestBody UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        User createdUser = userService.create(user);
        return userMapper.toDto(createdUser);
    }

    @PostMapping("/refresh")
    public JwtResponse refresh(@RequestBody String refreshToken) {
        return authService.refresh(refreshToken);
    }
}
