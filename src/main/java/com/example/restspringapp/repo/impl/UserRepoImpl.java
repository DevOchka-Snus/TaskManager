package com.example.restspringapp.repo.impl;

import com.example.restspringapp.domain.exception.ResourceMappingException;
import com.example.restspringapp.domain.user.Role;
import com.example.restspringapp.domain.user.User;
import com.example.restspringapp.repo.DataSourceConfig;
import com.example.restspringapp.repo.UserRepo;
import com.example.restspringapp.repo.mappers.UserRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

//@Repository
@RequiredArgsConstructor
public class UserRepoImpl implements UserRepo {

    private final DataSourceConfig dataSourceConfig;

    private final String FIND_BY_ID = """
            SELECT u.id as user_id,
                   u.firstname as user_firstname,
                   u.lastname as user_lastname,
                   u.email as user_email,
                   u.password as user_password,
                   ur.role as user_role_role,
                   t.id as task_id,
                   t.title as task_title,
                   t.description as task_description,
                   t.tag as task_tag,
                   t.expirationdate as task_expiration_date,
                   t.status as task_status
            FROM users u
                LEFT JOIN users_roles ur on u.id = ur.user_id
                LEFT JOIN user_tasks ut on u.id = ut.user_id
                LEFT JOIN tasks t on ut.task_id = t.id
            WHERE u.id = ?
            """;

    private final String FIND_BY_EMAIL = """
            SELECT u.id as user_id,
                   u.firstname as user_firstname,
                   u.lastname as user_lastname,
                   u.email as user_email,
                   u.password as user_password,
                   ur.role as user_role_role,
                   t.id as task_id,
                   t.title as task_title,
                   t.description as task_description,
                   t.tag as task_tag,
                   t.expirationdate as task_expiration_date,
                   t.status as task_status
            FROM users u
                LEFT JOIN users_roles ur on u.id = ur.user_id
                LEFT JOIN user_tasks ut on u.id = ut.user_id
                LEFT JOIN tasks t on ut.task_id = t.id
            WHERE u.email = ?
            """;

    private final String UPDATE = """
            UPDATE users
            SET firstname = ?,
                lastname = ?,
                email = ?,
                password = ?
            WHERE id = ?
            """;

    private final String CREATE = """
            INSERT INTO users (firstname, lastname, email, password)
            VALUES (?, ?, ?, ?)
            """;

    private final String INSERT_USER_ROLE = """
            INSERT INTO users_roles (user_id, role)
            VALUES (?, ?)
            """;

    private final String IS_TASK_OWNER = """
            SELECT exists(
                SELECT 1
                FROM user_tasks
                WHERE user_id = ?
                AND task_id = ?
            )""";

    private final String DELETE = """
            DELETE FROM users
            WHERE id = ?""";
    @Override
    public Optional<User> findById(Long id) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(FIND_BY_ID,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                return Optional.ofNullable(UserRowMapper.mapRow(resultSet));
            }
        } catch (SQLException e) {
            throw new ResourceMappingException("Error while finding user by id");
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(FIND_BY_EMAIL,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                return Optional.ofNullable(UserRowMapper.mapRow(resultSet));
            }
        } catch (SQLException e) {
            throw new ResourceMappingException("Error while finding user by email");
        }
    }

    @Override
    public void update(User user) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE);
            statement.setString(1, user.getFirstname());
            statement.setString(2, user.getLastname());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPassword());
            statement.setLong(5, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ResourceMappingException("Error while updating user");
        }
    }

    @Override
    public void create(User user) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getFirstname());
            statement.setString(2, user.getLastname());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPassword());
            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                resultSet.next();
                user.setId(resultSet.getLong(1));
            }
        } catch (SQLException e) {
            throw new ResourceMappingException("Error while creating user");
        }
    }

    @Override
    public void insertUserRole(Long userId, Role role) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(INSERT_USER_ROLE);
            statement.setLong(1, userId);
            statement.setString(2, role.name());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ResourceMappingException("Error while inserting user role");
        }
    }

    @Override
    public boolean isTaskOwner(Long userId, Long taskId) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(IS_TASK_OWNER);
            statement.setLong(1, userId);
            statement.setLong(2, taskId);
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return resultSet.getBoolean(1);
            }
        } catch (SQLException e) {
            throw new ResourceMappingException("Error while checking if user is task owner");
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
            throw new ResourceMappingException("Error while deleting user");
        }
    }
}
