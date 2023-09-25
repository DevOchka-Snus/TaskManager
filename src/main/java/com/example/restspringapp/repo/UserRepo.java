package com.example.restspringapp.repo;

import com.example.restspringapp.domain.user.Role;
import com.example.restspringapp.domain.user.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface UserRepo {
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    void update(User user);
    void create(User user);
    void insertUserRole(@Param("userId") Long userId, @Param("role") Role role);
    boolean isTaskOwner(@Param("userId") Long userId, @Param("taskId") Long taskId);
    void delete(Long id);
}
