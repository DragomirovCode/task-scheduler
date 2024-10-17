package ru.dragomirov.taskschedule.commons;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.dragomirov.taskschedule.auth.User;
import ru.dragomirov.taskschedulercommondto.kafka.UserDto;

import java.util.List;

@Mapper(componentModel = "spring", uses = {DualServiceTaskMapper.class})
public interface DualServiceUserMapper {

    @Mapping(source = "tasks", target = "taskDtos")
    List<UserDto> toDto(List<User> users);

    @Mapping(source = "tasks", target = "taskDtos")
    UserDto toDto(User user);

    @Mapping(source = "taskDtos", target = "tasks")
    User toEntity(UserDto dto);

    @Mapping(source = "taskDtos", target = "tasks")
    List<User> toEntity(List<UserDto> userDtos);

}
