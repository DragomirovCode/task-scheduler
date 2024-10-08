package ru.dragomirov.taskschedule.core.task.update;

import org.mapstruct.Mapper;
import ru.dragomirov.taskschedule.core.task.Task;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UpdateTaskMapper {
    Task toEntity(UpdateTaskDto taskDto);
    List<Task> toEntity(List<UpdateTaskDto> taskDtoList);
    UpdateTaskDto toDto(Task task);
    List<UpdateTaskDto> toDto(List<Task> taskList);
}
