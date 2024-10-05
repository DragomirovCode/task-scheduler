package ru.dragomirov.taskschedule.auth;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserRegistrationDto toRegistrationDto(User user);
    UserLoginDto toLoginDto(User user);
    User toRegistrationEntity(UserRegistrationDto userRegistrationDto);
    User toLoginEntity(UserLoginDto userLoginDto);
}