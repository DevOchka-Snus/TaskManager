package com.example.restspringapp.services.impl;

import com.example.restspringapp.domain.exception.ResourceNotFoundException;
import com.example.restspringapp.domain.user.Role;
import com.example.restspringapp.domain.user.User;
import com.example.restspringapp.repo.UserRepo;
import com.example.restspringapp.services.UserService;
import com.example.restspringapp.web.dto.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepo userRepo;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "UserService::getById", key = "#id")
    public User getById(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "UserService::getByEmail", key = "#email")
    public User getByEmail(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    @Transactional
    @Caching(cacheable = {
            @Cacheable(value = "UserService::getById", key = "#user.id"),
            @Cacheable(value = "UserService::getByEmail", key = "#user.email")
    })
    public User create(final UserDto dto) {
        if (userRepo.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalStateException("User already exists");
        }
        if (!dto.getPassword().equals(dto.getPasswordConfirmation())) {
            throw new IllegalStateException("Password and password confirmation don`t match");
        }
        var user = new User();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Role> roles = Set.of(Role.ROLE_USER);
        user.setRoles(roles);
        user.setTasks(new ArrayList<>());
        return userRepo.save(user);
    }

    @Override
    @Transactional
    @Caching(put = {
            @CachePut(value = "UserService::getById", key = "#user.id"),
            @CachePut(value = "UserService::getByEmail", key = "#user.email")
    })
    public User update(final UserDto dto) {
        var existing = getById(dto.getId());
        existing.setEmail(dto.getEmail());
        existing.setFirstname(dto.getFirstname());
        existing.setLastname(dto.getLastname());
        existing.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepo.save(existing);
        return existing;
    }

    @Override
    public User getTaskAuthor(Long taskId) {
        return userRepo.findTaskAuthor(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "UserService::isTaskOwner", key = "#userId + '.' + #taskId")
    public boolean isTaskOwner(Long userId, Long taskId) {
        return userRepo.isTaskOwner(userId, taskId);
    }

    @Override
    @Transactional
    @CacheEvict(value = "UserService::getById", key = "#id")
    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }
}
