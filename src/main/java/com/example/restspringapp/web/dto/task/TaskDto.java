package com.example.restspringapp.web.dto.task;

import com.example.restspringapp.domain.task.Status;
import com.example.restspringapp.web.dto.validation.OnCreate;
import com.example.restspringapp.web.dto.validation.OnUpdate;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class TaskDto {
    @NotNull(message = "Id must be not null", groups = OnUpdate.class)
    private Long id;

    @NotNull(message = "Title must be not null", groups = {OnCreate.class, OnUpdate.class})
    @Length(max = 255, message = "Title length must be smaller than 255", groups = {OnCreate.class, OnUpdate.class})
    private String title;

    @Length(max = 255, message = "Description length must be smaller than 255", groups = {OnCreate.class, OnUpdate.class})
    private String description;

    @Length(max = 255, message = "Tag length must be smaller than 255", groups = {OnCreate.class, OnUpdate.class})
    private String tag;
    private Status status;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime expirationTime;
}
