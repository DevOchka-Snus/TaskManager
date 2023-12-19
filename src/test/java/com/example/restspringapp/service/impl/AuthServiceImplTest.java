package com.example.restspringapp.service.impl;

import com.example.restspringapp.config.TestConfig;
import com.example.restspringapp.domain.exception.ResourceNotFoundException;
import com.example.restspringapp.domain.user.Role;
import com.example.restspringapp.domain.user.User;
import com.example.restspringapp.repo.TaskRepo;
import com.example.restspringapp.repo.UserRepo;
import com.example.restspringapp.services.impl.AuthServiceImpl;
import com.example.restspringapp.services.impl.UserServiceImpl;
import com.example.restspringapp.web.dto.auth.JwtRequest;
import com.example.restspringapp.web.dto.auth.JwtResponse;
import com.example.restspringapp.web.security.JwtTokenProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Set;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Import(TestConfig.class)
@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    private UserRepo userRepository;

    @MockBean
    private TaskRepo taskRepository;

    @MockBean
    private JwtTokenProvider tokenProvider;

    @Autowired
    private AuthServiceImpl authService;

    @Test
    void login() {
        Long userId = 1L;
        String email = "email@we.ru";
        String password = "password";
        Set<Role> roles = Collections.emptySet();
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";
        JwtRequest request = new JwtRequest();
        request.setEmail(email);
        request.setPassword(password);
        User user = new User();
        user.setId(userId);
        user.setEmail(email);
        user.setRoles(roles);
        Mockito.when(userService.getByEmail(email))
                .thenReturn(user);
        Mockito.when(tokenProvider.createAccessToken(userId, email, roles))
                .thenReturn(accessToken);
        Mockito.when(tokenProvider.createRefreshToken(userId, email))
                .thenReturn(refreshToken);
        JwtResponse response = authService.login(request);
        Mockito.verify(authenticationManager)
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword())
                );
        Assertions.assertEquals(response.getEmail(), email);
        Assertions.assertEquals(response.getId(), userId);
        Assertions.assertNotNull(response.getAccessToken());
        Assertions.assertNotNull(response.getRefreshToken());
    }

    @Test
    void loginWithIncorrectUsername() {
        String email = "email@we.ru";
        String password = "password";
        JwtRequest request = new JwtRequest();
        request.setEmail(email);
        request.setPassword(password);
        User user = new User();
        user.setEmail(email);
        Mockito.when(userService.getByEmail(email))
                .thenThrow(ResourceNotFoundException.class);
        Mockito.verifyNoInteractions(tokenProvider);
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> authService.login(request));
    }

    @Test
    void refresh() {
        String refreshToken = "refreshToken";
        String accessToken = "accessToken";
        String newRefreshToken = "newRefreshToken";
        JwtResponse response = new JwtResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(newRefreshToken);
        Mockito.when(tokenProvider.refreshUserToken(refreshToken))
                .thenReturn(response);
        JwtResponse testResponse = authService.refresh(refreshToken);
        Mockito.verify(tokenProvider).refreshUserToken(refreshToken);
        Assertions.assertEquals(testResponse, response);
    }

}
