package ru.dragomirov.taskschedule.auth;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserRegistrationMapper {
    UserRegistrationDto toDto(User user);
    User toEntity(UserRegistrationDto userRegistrationDto);
}