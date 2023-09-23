package com.example.restspringapp.repo;

import com.example.restspringapp.domain.task.Task;
import com.example.restspringapp.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepo {
    Optional<Task> findById(Long id);
    List<Task> findAllByUserId(Long userId);
    void assignToUserById(Long taskId, Long userId);
    void update(Task task);
    void create(Task task);
    void delete(Long id);
}
