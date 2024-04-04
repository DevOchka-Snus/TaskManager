package com.example.restspringapp.services;

import com.example.restspringapp.domain.user.User;
import com.example.restspringapp.web.dto.user.UserDto;

public interface UserService {
    User getById(Long id);
    User getByEmail(String email);
    User create(UserDto user);
    User update(UserDto user);
    User getTaskAuthor(Long taskId);
    boolean isTaskOwner(Long userId, Long taskId);
    void deleteUser(Long id);
}
