package com.example.restspringapp.services.impl;

import com.example.restspringapp.domain.exception.ResourceNotFoundException;
import com.example.restspringapp.domain.user.Role;
import com.example.restspringapp.domain.user.User;
import com.example.restspringapp.repo.UserRepo;
import com.example.restspringapp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepo userRepo;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public User getById(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public User getByEmail(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    @Transactional
    public User create(User user) {
        if (userRepo.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalStateException("User already exists");
        }
        if (!user.getPassword().equals(user.getPasswordConfirmation())) {
            throw new IllegalStateException("Password and password confirmation don`t match");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.create(user);
        Set<Role> roles = Set.of(Role.ROLE_USER);
        userRepo.insertUserRole(user.getId(), Role.ROLE_USER);
        user.setRoles(roles);
        return user;
    }

    @Override
    @Transactional
    public User update(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.create(user);
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isTaskOwner(Long userId, Long taskId) {
        return userRepo.isTaskOwner(userId, taskId);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRepo.delete(id);
    }
}
