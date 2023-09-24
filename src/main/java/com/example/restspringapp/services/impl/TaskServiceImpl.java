package com.example.restspringapp.services.impl;

import com.example.restspringapp.domain.exception.ResourceNotFoundException;
import com.example.restspringapp.domain.task.Status;
import com.example.restspringapp.domain.task.Task;
import com.example.restspringapp.repo.TaskRepo;
import com.example.restspringapp.repo.UserRepo;
import com.example.restspringapp.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepo taskRepo;
    @Override
    @Transactional(readOnly = true)
    public Task getById(Long id) {
        return taskRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> getByUserId(Long userId) {
        return taskRepo.findAllByUserId(userId);
    }

    @Override
    @Transactional
    public Task update(Task task) {
        if (task.getStatus() == null) {
            task.setStatus(Status.TODO);
        }
        taskRepo.update(task);
        return task;
    }

    @Override
    @Transactional
    public Task create(Task task, Long userId) {
        task.setStatus(Status.TODO);
        taskRepo.create(task);
        taskRepo.assignToUserById(task.getId(), userId);
        return task;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        taskRepo.delete(id);
    }
}
