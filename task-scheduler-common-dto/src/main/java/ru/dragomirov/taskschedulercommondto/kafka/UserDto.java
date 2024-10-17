package ru.dragomirov.taskschedulercommondto.kafka;

import lombok.Data;

@Data
public class UserDto {

    public Long id;
    public String username;
    public String email;

}
