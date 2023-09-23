package com.example.restspringapp.web.mappers;

import com.example.restspringapp.domain.user.User;
import com.example.restspringapp.web.dto.user.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(UserDto userDto);
}
