package com.example.restspringapp.web.controllers;

import com.example.restspringapp.domain.task.Task;
import com.example.restspringapp.services.TaskService;
import com.example.restspringapp.web.dto.task.TaskDto;
import com.example.restspringapp.web.dto.validation.OnUpdate;
import com.example.restspringapp.web.mappers.TaskMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Validated
@Tag(name = "Task controller", description = "task API")
public class TaskController {

    private final TaskService taskService;

    private final TaskMapper taskMapper;

    @PutMapping
    @Operation(summary = "Update task")
    public TaskDto update(@Validated(OnUpdate.class) @RequestBody TaskDto dto) {
        Task task = taskMapper.toEntity(dto);
        Task updatedTask = taskService.update(task);
        return taskMapper.toDto(updatedTask);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get task info by his id")
    public TaskDto getById(@PathVariable("id") Long id)  {
        Task task = taskService.getById(id);
        return taskMapper.toDto(task);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete task from database by his id")
    public void delete(@PathVariable("id") Long id) {
        taskService.delete(id);
    }


}
