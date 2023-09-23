package com.example.restspringapp.services.impl;

import com.example.restspringapp.services.AuthService;
import com.example.restspringapp.web.dto.auth.JwtRequest;
import com.example.restspringapp.web.dto.auth.JwtResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Override
    public JwtResponse login(JwtRequest loginRequest) {
        return null;
    }

    @Override
    public JwtResponse refresh(String refreshToken) {
        return null;
    }
}
