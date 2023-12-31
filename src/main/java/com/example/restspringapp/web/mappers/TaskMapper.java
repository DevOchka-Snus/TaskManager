package com.example.restspringapp.web.mappers;

import com.example.restspringapp.domain.task.Task;
import com.example.restspringapp.web.dto.task.TaskDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper extends Mappable<Task, TaskDto> {
}
