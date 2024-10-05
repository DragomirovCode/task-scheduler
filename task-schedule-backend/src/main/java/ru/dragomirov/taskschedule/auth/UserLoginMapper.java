package ru.dragomirov.taskschedule.auth;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserLoginMapper {
    UserLoginDto toDto(User user);
    User toEntity(UserLoginDto userLoginDto);
}
