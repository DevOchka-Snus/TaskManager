package com.example.restspringapp.repo.mappers;

import com.example.restspringapp.domain.task.Task;
import com.example.restspringapp.domain.user.Role;
import com.example.restspringapp.domain.user.User;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserRowMapper {
    @SneakyThrows
    public static User mapRow(ResultSet resultSet) {
        Set<Role> roles = new HashSet<>();
        while (resultSet.next()) {
            roles.add(Role.valueOf(resultSet.getString("user_role_role")));
        }
        resultSet.beforeFirst();
        List<Task> tasks = TaskRowMapper.mapRows(resultSet);
        resultSet.beforeFirst();
        if (resultSet.next()) {
            User user = new User();
            user.setId(resultSet.getLong("user_id"));
            user.setFirstname(resultSet.getString("user_firstname"));
            user.setLastname(resultSet.getString("user_lastname"));
            user.setEmail(resultSet.getString("user_email"));
            user.setPassword(resultSet.getString("user_password"));
            user.setRoles(roles);
            user.setTasks(tasks);
            return user;
        }
        return null;
    }
}
