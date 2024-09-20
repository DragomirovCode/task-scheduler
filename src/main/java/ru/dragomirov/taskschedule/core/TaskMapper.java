package ru.dragomirov.taskschedule.core;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    TaskDto toEntity(Task task);
    List<TaskDto> toEntity(List<Task> tasks);
    Task toDto(TaskDto taskDto);
}
