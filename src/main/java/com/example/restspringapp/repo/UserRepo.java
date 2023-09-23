package com.example.restspringapp.repo;

import com.example.restspringapp.domain.user.Role;
import com.example.restspringapp.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo {
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    void update(User user);
    void create(User user);
    void insertUserRole(Long userId, Role role);
    boolean isTaskOwner(Long userId, Long taskId);
    void delete(Long id);
}
