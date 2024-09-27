package ru.dragomirov.taskschedule.core.task;

import org.mapstruct.Mapper;
import ru.dragomirov.taskschedule.core.task.Task;
import ru.dragomirov.taskschedule.core.task.TaskDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    TaskDto toEntity(Task task);
    List<TaskDto> toEntity(List<Task> tasks);
    Task toDto(TaskDto taskDto);
}
