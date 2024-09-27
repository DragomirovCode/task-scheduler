package ru.dragomirov.taskschedule.core.task;

import org.mapstruct.Mapper;
import ru.dragomirov.taskschedule.core.task.Task;
import ru.dragomirov.taskschedule.core.task.TaskDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    Task toEntity(TaskDto taskDto);
    List<Task> toEntity(List<TaskDto> taskDtoList);
    TaskDto toDto(Task task);
    List<TaskDto> toDto(List<Task> taskList);
}
