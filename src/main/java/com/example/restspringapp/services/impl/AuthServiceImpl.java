package com.example.restspringapp.services.impl;

import com.example.restspringapp.services.AuthService;
import com.example.restspringapp.web.dto.auth.JwtRequest;
import com.example.restspringapp.web.dto.auth.JwtResponse;
import com.example.restspringapp.web.security.JwtUserDetailsService;
import com.example.restspringapp.web.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;
    private final JwtUserDetailsService jwtUserDetailsService;



    @Override
    public JwtResponse login(final JwtRequest loginRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(loginRequest.getEmail());
        String token = jwtUtils.generateToken(userDetails);
        return new JwtResponse(token);
    }
}
