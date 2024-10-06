package ru.dragomirov.taskschedule.auth.login;

import org.mapstruct.Mapper;
import ru.dragomirov.taskschedule.auth.User;

@Mapper(componentModel = "spring")
public interface UserLoginMapper {
    UserLoginDto toDto(User user);
    User toEntity(UserLoginDto userLoginDto);
}
