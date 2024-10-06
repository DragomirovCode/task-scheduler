package ru.dragomirov.taskschedule.auth.registration;

import org.mapstruct.Mapper;
import ru.dragomirov.taskschedule.auth.User;
import ru.dragomirov.taskschedule.auth.registration.UserRegistrationDto;

@Mapper(componentModel = "spring")
public interface UserRegistrationMapper {
    UserRegistrationDto toDto(User user);
    User toEntity(UserRegistrationDto userRegistrationDto);
}