package com.example.restspringapp.service.impl;

import com.example.restspringapp.config.TestConfig;
import com.example.restspringapp.domain.exception.ResourceNotFoundException;
import com.example.restspringapp.domain.user.Role;
import com.example.restspringapp.domain.user.User;
import com.example.restspringapp.repo.UserRepo;
import com.example.restspringapp.services.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.Properties;
import java.util.Set;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Import(TestConfig.class)
@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @MockBean
    private UserRepo userRepository;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserServiceImpl userService;

    @Test
    void getById() {
        Long id = 1L;
        User user = new User();
        user.setId(id);
        Mockito.when(userRepository.findById(id))
                .thenReturn(Optional.of(user));
        User testUser = userService.getById(id);
        Mockito.verify(userRepository).findById(id);
        Assertions.assertEquals(user, testUser);
    }

    @Test
    void getByNotExistingId() {
        Long id = 1L;
        Mockito.when(userRepository.findById(id))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> userService.getById(id));
        Mockito.verify(userRepository).findById(id);
    }

    @Test
    void getByUsername() {
        String email = "username@gmail.com";
        User user = new User();
        user.setEmail(email);
        Mockito.when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));
        User testUser = userService.getByEmail(email);
        Mockito.verify(userRepository).findByEmail(email);
        Assertions.assertEquals(user, testUser);
    }

    @Test
    void getByNotExistingUsername() {
        String email = "username@gmail.com";
        Mockito.when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> userService.getByEmail(email));
        Mockito.verify(userRepository).findByEmail(email);
    }

    @Test
    void update() {
        Long id = 1L;
        String password = "password";
        User user = new User();
        user.setId(id);
        user.setPassword(password);
        Mockito.when(passwordEncoder.encode(password))
                .thenReturn("encodedPassword");
        Mockito.when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));
        User updated = userService.update(user);
        Mockito.verify(passwordEncoder).encode(password);
        Mockito.verify(userRepository).save(user);
        Assertions.assertEquals(user.getEmail(), updated.getEmail());
        Assertions.assertEquals(user.getEmail(), updated.getEmail());
    }

    @Test
    void isTaskOwner() {
        Long userId = 1L;
        Long taskId = 1L;
        Mockito.when(userRepository.isTaskOwner(userId, taskId))
                .thenReturn(true);
        boolean isOwner = userService.isTaskOwner(userId, taskId);
        Mockito.verify(userRepository).isTaskOwner(userId, taskId);
        Assertions.assertTrue(isOwner);
    }

    @Test
    void create() {
        String email = "username@gmail.com";
        String password = "password";
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setPasswordConfirmation(password);
        Mockito.when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.encode(password))
                .thenReturn("encodedPassword");
        User testUser = userService.create(user);
        Mockito.verify(userRepository).save(user);
        Assertions.assertEquals(Set.of(Role.ROLE_USER), testUser.getRoles());
        Assertions.assertEquals("encodedPassword",
                testUser.getPassword());
    }

    @Test
    void createWithExistingUsername() {
        String email = "username@gmail.com";
        String password = "password";
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setPasswordConfirmation(password);
        Mockito.when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(new User()));
        Mockito.when(passwordEncoder.encode(password))
                .thenReturn("encodedPassword");
        Assertions.assertThrows(IllegalStateException.class,
                () -> userService.create(user));
        Mockito.verify(userRepository, Mockito.never()).save(user);
    }

    @Test
    void createWithDifferentPasswords() {
        String email = "username@gmail.com";
        String password = "password1";
        String passwordConfirmation = "password2";
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setPasswordConfirmation(passwordConfirmation);
        Mockito.when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(IllegalStateException.class,
                () -> userService.create(user));
        Mockito.verify(userRepository, Mockito.never()).save(user);
    }

    @Test
    void isTaskOwnerWithFalse() {
        Long userId = 1L;
        Long taskId = 1L;
        Mockito.when(userRepository.isTaskOwner(userId, taskId))
                .thenReturn(false);
        boolean isOwner = userService.isTaskOwner(userId, taskId);
        Mockito.verify(userRepository).isTaskOwner(userId, taskId);
        Assertions.assertFalse(isOwner);
    }

    @Test
    void getTaskAuthor() {
        Long taskId = 1L;
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        Mockito.when(userRepository.findTaskAuthor(taskId))
                .thenReturn(Optional.of(user));
        User author = userService.getTaskAuthor(taskId);
        Mockito.verify(userRepository).findTaskAuthor(taskId);
        Assertions.assertEquals(user, author);
    }

    @Test
    void getNotExistingTaskAuthor() {
        Long taskId = 1L;
        Mockito.when(userRepository.findTaskAuthor(taskId))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () ->
                userService.getTaskAuthor(taskId));
        Mockito.verify(userRepository).findTaskAuthor(taskId);
    }

    @Test
    void delete() {
        Long id = 1L;
        userService.deleteUser(id);
        Mockito.verify(userRepository).deleteById(id);
    }

}
