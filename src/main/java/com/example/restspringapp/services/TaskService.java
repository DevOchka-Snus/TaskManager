package com.example.restspringapp.services;

import com.example.restspringapp.domain.task.Task;

import java.util.List;

public interface TaskService {
    Task getById(Long id);
    List<Task> getByUserId(Long userId);
    Task update(Task task);
    Task create(Task task, Long id);
    void delete(Long id);
}
