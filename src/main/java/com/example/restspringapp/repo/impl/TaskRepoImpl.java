package com.example.restspringapp.repo.impl;

import com.example.restspringapp.domain.exception.ResourceMappingException;
import com.example.restspringapp.domain.task.Task;
import com.example.restspringapp.repo.DataSourceConfig;
import com.example.restspringapp.repo.TaskRepo;
import com.example.restspringapp.repo.mappers.TaskRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;
import java.util.Optional;

//@Repository
@RequiredArgsConstructor
public class TaskRepoImpl implements TaskRepo {

    private final DataSourceConfig dataSourceConfig;

    private final String FIND_BY_ID = """
            SELECT t.id as task_id,
                   t.title as task_title,
                   t.description as task_description,
                   t.tag as t_tag,
                   t.expirationdate as task_expiration_date,
                   t.status as task_status
            FROM tasks t
            WHERE t.id = ?""";

    private final String FIND_ALL_BY_USER_ID = """
            SELECT t.id as task_id,
                   t.title as task_title,
                   t.description as task_description,
                   t.tag as t_tag,
                   t.expirationdate as task_expiration_date,
                   t.status as task_status
            FROM tasks t
            JOIN user_tasks ut on t.id = ut.task_id
            WHERE ut.user_id = ?""";

    private final String ASSIGN = """
            INSERT INTO user_tasks (task_id, user_id)
            VALUES (?, ?)""";

    private final String UPDATE = """
            UPDATE tasks
            SET title = ?,
                description = ?,
                tag = ?,
                expirationdate = ?,
                status = ?
            WHERE id = ?
            """;

    private final String CREATE = """
            INSERT INTO tasks (title, description, tag, expirationdate, status)
            VALUES (?, ?, ?, ?, ?)
            """;

    private final String DELETE = """
            DELETE FROM tasks
            WHERE id = ?""";

    @Override
    public Optional<Task> findById(Long id) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(FIND_BY_ID);
            statement.setLong(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                return Optional.ofNullable(TaskRowMapper.mapRow(rs));
            }
        } catch (SQLException e) {
            throw new ResourceMappingException("Error while finding task by id");
        }
    }

    @Override
    public List<Task> findAllByUserId(Long userId) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(FIND_ALL_BY_USER_ID);
            statement.setLong(1, userId);
            try (ResultSet rs = statement.executeQuery()) {
                return TaskRowMapper.mapRows(rs);
            }
        } catch (SQLException e) {
            throw new ResourceMappingException("Error while finding all by user id");
        }
    }

    @Override
    public void assignToUserById(Long taskId, Long userId) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(ASSIGN);
            statement.setLong(1, taskId);
            statement.setLong(2, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ResourceMappingException("Error while assigning to user by id");
        }
    }

    @Override
    public void update(Task task) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE);
            statement.setString(1, task.getTitle());

            if (task.getDescription() == null) {
                statement.setNull(2, Types.VARCHAR);
            } else {

            }

            if (task.getTag() == null) {
                statement.setNull(3, Types.VARCHAR);
            } else {
                statement.setString(3, task.getTag());
            }

            if (task.getExpirationDate() == null) {
                statement.setNull(4, Types.TIMESTAMP);
            } else {
                statement.setTimestamp(4, Timestamp.valueOf(task.getExpirationDate()));
            }

            statement.setString(5, task.getStatus().name());

            statement.setLong(6, task.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ResourceMappingException("Error while updating task");
        }
    }

    @Override
    public void create(Task task) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, task.getTitle());

            if (task.getDescription() == null) {
                statement.setNull(2, Types.VARCHAR);
            } else {
                statement.setString(2, task.getDescription());
            }

            if (task.getTag() == null) {
                statement.setNull(3, Types.VARCHAR);
            } else {
                statement.setString(3, task.getTag());
            }

            if (task.getExpirationDate() == null) {
                statement.setNull(4, Types.TIMESTAMP);
            } else {
                statement.setTimestamp(4, Timestamp.valueOf(task.getExpirationDate()));
            }

            statement.setString(5, task.getStatus().name());

            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                resultSet.next();
                task.setId(resultSet.getLong(1));
            }
        } catch (SQLException e) {
            throw new ResourceMappingException("Error while creating task");
        }
    }

    @Override
    public void delete(Long id) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE);
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ResourceMappingException("Error while deleting task");
        }
    }
}
