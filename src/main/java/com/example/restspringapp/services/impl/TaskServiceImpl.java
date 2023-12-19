package com.example.restspringapp.services.impl;

import com.example.restspringapp.domain.exception.ResourceNotFoundException;
import com.example.restspringapp.domain.task.Status;
import com.example.restspringapp.domain.task.Task;
import com.example.restspringapp.domain.task.TaskImage;
import com.example.restspringapp.domain.user.User;
import com.example.restspringapp.repo.TaskRepo;
import com.example.restspringapp.services.ImageService;
import com.example.restspringapp.services.TaskService;
import com.example.restspringapp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepo taskRepo;
    private final ImageService imageService;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "TaskService::getById", key = "#id")
    public Task getById(final Long id) {
        return taskRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> getAllByUserId(final Long userId) {
        return taskRepo.findAllByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> getAllSoonTasks(final Duration duration) {
        LocalDateTime now = LocalDateTime.now();
        return taskRepo.findAllSoonTasks(Timestamp.valueOf(now), Timestamp.valueOf(now.plus(duration)));
    }

    @Override
    @Transactional
    @CachePut(value = "TaskService::getById", key = "#task.id")
    public Task update(final Task task) {
        Task existing = getById(task.getId());
        existing.setStatus(task.getStatus() == null ? Status.TODO : task.getStatus());
        existing.setTitle(task.getTitle());
        existing.setDescription(task.getDescription());
        existing.setExpirationDate(task.getExpirationDate());
        taskRepo.save(existing);
        return existing;
    }

    @Override
    @Transactional
    @Cacheable(value = "TaskService::getById",
            condition = "#task.id!=null",
            key = "#task.id")
    public Task create(final Task task, final Long userId) {
        task.setStatus(Status.TODO);
        taskRepo.save(task);
        taskRepo.assignTask(userId, task.getId());
        return task;
    }

    @Override
    @Transactional
    @CacheEvict(value = "TaskService::getById", key = "#id")
    public void delete(final Long id) {
        taskRepo.deleteById(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = "TaskService::getById", key = "#id")
    public void uploadImage(final Long id, final TaskImage image) {
        String filename = imageService.upload(image);
        taskRepo.addImage(id, filename);
    }
}
