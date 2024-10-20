package ru.dragomirov.taskscheduler.commons.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.dragomirov.taskscheduler.commons.message.TaskDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    public Long id;
    public String username;
    public String email;
    public List<TaskDto> taskDtos;

}
