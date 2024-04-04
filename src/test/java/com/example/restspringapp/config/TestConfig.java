/*
package com.example.restspringapp.config;

import com.example.restspringapp.repo.TaskRepo;
import com.example.restspringapp.repo.UserRepo;
import com.example.restspringapp.services.ImageService;
import com.example.restspringapp.services.impl.AuthServiceImpl;
import com.example.restspringapp.services.impl.ImageServiceImpl;
import com.example.restspringapp.services.impl.TaskServiceImpl;
import com.example.restspringapp.services.impl.UserServiceImpl;
import com.example.restspringapp.services.props.JwtProperties;
import com.example.restspringapp.services.props.MinioProperties;
import com.example.restspringapp.web.security.JwtUserDetailsService;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@TestConfiguration
@RequiredArgsConstructor
public class TestConfig {

    @Bean
    @Primary
    public BCryptPasswordEncoder testPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtProperties jwtProperties() {
        JwtProperties jwtProperties = new JwtProperties();
        jwtProperties.setSecret(
                "ZnNkYXZhc2R2YXdyZWd2YWR2ZHdyYWQ"
        );
        return jwtProperties;
    }

    @Bean
    public UserDetailsService userDetailsService(
            final UserRepo userRepository
    ) {
        return new JwtUserDetailsService(userService(userRepository));
    }

    @Bean
    public MinioClient minioClient() {
        return Mockito.mock(MinioClient.class);
    }

    @Bean
    public MinioProperties minioProperties() {
        MinioProperties properties = new MinioProperties();
        properties.setBucket("images");
        return properties;
    }

    @Bean
    public AppConfig configuration() {
        return Mockito.mock(AppConfig.class);
    }

    @Bean
    @Primary
    public ImageService imageService() {
        return new ImageServiceImpl(minioClient(), minioProperties());
    }

    @Bean
    public JwtTokenProvider tokenProvider(
            final UserRepo userRepository
    ) {
        return new JwtTokenProvider(jwtProperties(),
                userDetailsService(userRepository),
                userService(userRepository));
    }

    @Bean
    @Primary
    public UserServiceImpl userService(
            final UserRepo userRepository
    ) {
        return new UserServiceImpl(
                userRepository,
                testPasswordEncoder()
        );
    }

    @Bean
    @Primary
    public TaskServiceImpl taskService(
            final TaskRepo taskRepository
    ) {
        return new TaskServiceImpl(taskRepository, imageService());
    }

    @Bean
    @Primary
    public AuthServiceImpl authService(
            final UserRepo userRepository,
            final AuthenticationManager authenticationManager
    ) {
        return new AuthServiceImpl(
                authenticationManager,
                userService(userRepository),
                tokenProvider(userRepository)
        );
    }

    @Bean
    public UserRepo userRepository() {
        return Mockito.mock(UserRepo.class);
    }

    @Bean
    public TaskRepo taskRepository() {
        return Mockito.mock(TaskRepo.class);
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return Mockito.mock(AuthenticationManager.class);
    }

}
*/
