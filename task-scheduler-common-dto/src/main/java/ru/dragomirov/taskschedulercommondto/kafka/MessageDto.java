package ru.dragomirov.taskschedulercommondto.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Properties;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {

    public UserDto userDto;
    public String type;
    public Properties body;

}
