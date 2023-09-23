package com.example.restspringapp.web.controllers;

import com.example.restspringapp.domain.task.Task;
import com.example.restspringapp.services.TaskService;
import com.example.restspringapp.web.dto.task.TaskDto;
import com.example.restspringapp.web.dto.validation.OnUpdate;
import com.example.restspringapp.web.mappers.TaskMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Validated
public class TaskController {

    private final TaskService taskService;

    private final TaskMapper taskMapper;

    @PutMapping
    public TaskDto update(@Validated(OnUpdate.class) @RequestBody TaskDto dto) {
        Task task = taskMapper.toEntity(dto);
        Task updatedTask = taskService.update(task);
        return taskMapper.toDto(updatedTask);
    }

    @GetMapping("/{id}")
    public TaskDto getById(@PathVariable("id") Long id)  {
        Task task = taskService.getById(id);
        return taskMapper.toDto(task);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        taskService.delete(id);
    }


}
