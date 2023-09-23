package com.example.restspringapp.domain.user;

import com.example.restspringapp.domain.task.Task;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class User{
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String passwordConfirmation;
    private Set<Role> roles;
    private List<Task> tasks;
}
