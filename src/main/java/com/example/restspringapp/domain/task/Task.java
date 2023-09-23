package com.example.restspringapp.domain.task;

import com.example.restspringapp.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
public class Task {
    private Long id;
    private String title;
    private String description;
    private String tag;
    private Status status;
    private LocalDateTime expirationTime;
}