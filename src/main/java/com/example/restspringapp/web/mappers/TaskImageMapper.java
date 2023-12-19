package com.example.restspringapp.web.mappers;

import com.example.restspringapp.domain.task.Task;
import com.example.restspringapp.domain.task.TaskImage;
import com.example.restspringapp.web.dto.task.TaskDto;
import com.example.restspringapp.web.dto.task.TaskImageDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskImageMapper extends Mappable<TaskImage, TaskImageDto> {
}
