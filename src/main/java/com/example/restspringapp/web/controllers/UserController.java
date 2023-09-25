package com.example.restspringapp.web.controllers;

import com.example.restspringapp.domain.task.Task;
import com.example.restspringapp.domain.user.User;
import com.example.restspringapp.services.TaskService;
import com.example.restspringapp.services.UserService;
import com.example.restspringapp.web.dto.task.TaskDto;
import com.example.restspringapp.web.dto.user.UserDto;
import com.example.restspringapp.web.dto.validation.OnCreate;
import com.example.restspringapp.web.dto.validation.OnUpdate;
import com.example.restspringapp.web.mappers.TaskMapper;
import com.example.restspringapp.web.mappers.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
@Tag(name = "User controller", description = "user API")
public class UserController {

    private final UserService userService;
    private final TaskService taskService;

    private final UserMapper userMapper;
    private final TaskMapper taskMapper;

    @PutMapping
    @Operation(summary = "Update user")
    public UserDto update(@Validated(OnUpdate.class) @RequestBody UserDto dto) {
        User user = userMapper.toEntity(dto);
        User updatedUser = userService.update(user);
        return userMapper.toDto(updatedUser);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user info by his id")
    public UserDto getById(@PathVariable("id") Long id) {
        User user = userService.getById(id);
        return userMapper.toDto(user);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user from database by his id")
    public void delete(@PathVariable("id") Long id) {
        userService.deleteUser(id);
    }

    @GetMapping("/{id}/tasks")
    @Operation(summary = "Get all user tasks")
    public List<TaskDto> getTasksByUserId(@PathVariable("id") Long id) {
        List<Task> tasks = taskService.getByUserId(id);
        return taskMapper.toDto(tasks);
    }

    @PostMapping("/{id}/tasks")
    @Operation(summary = "Create new user account")
    public TaskDto create(@PathVariable("id") Long id, @Validated(OnCreate.class) @RequestBody TaskDto dto) {
        Task task = taskMapper.toEntity(dto);
        Task createdTask = taskService.create(task, id);
        return taskMapper.toDto(createdTask);
    }
}
