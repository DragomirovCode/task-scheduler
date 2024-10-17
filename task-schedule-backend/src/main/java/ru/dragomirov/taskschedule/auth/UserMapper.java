package ru.dragomirov.taskschedule.auth;

import org.mapstruct.Mapper;
import ru.dragomirov.taskschedulercommondto.kafka.UserDto;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(UserDto dto);
}
