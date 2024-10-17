package ru.dragomirov.taskschedulercommondto.kafka;

import lombok.Data;

import java.util.List;

@Data
public class UserDto {

    public Long id;
    public String username;
    public String email;
    public List<TaskDto> taskDtos;

}
