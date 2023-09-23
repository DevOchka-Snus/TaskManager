package com.example.restspringapp.services;

import com.example.restspringapp.web.dto.auth.JwtRequest;
import com.example.restspringapp.web.dto.auth.JwtResponse;

public interface AuthService {
    JwtResponse login(JwtRequest loginRequest);
    JwtResponse refresh(String refreshToken);
}
