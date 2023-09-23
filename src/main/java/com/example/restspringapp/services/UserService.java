package com.example.restspringapp.services;

import com.example.restspringapp.domain.user.User;

public interface UserService {
    User getById(Long id);
    User getByEmail(String email);
    User create(User user);
    User update(User user);
    boolean isTaskOwner(Long userId, Long taskId);
    void deleteUser(Long id);
}
