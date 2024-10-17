package ru.dragomirov.taskschedule.commons;

import org.mapstruct.Mapper;
import ru.dragomirov.taskschedule.core.task.Task;
import ru.dragomirov.taskschedulercommondto.kafka.TaskDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DualServiceTaskMapper {

    Task toEntity(TaskDto taskDto);
    List<Task> toEntity(List<TaskDto> taskDtoList);
    TaskDto toDto(Task task);
    List<TaskDto> toDto(List<Task> taskList);

}
