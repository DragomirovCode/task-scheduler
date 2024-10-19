package ru.dragomirov.taskschedulercommondto.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {

    public UserDto userDto;
    public String type;
    public Map<String, Object> body = new HashMap<>();

}
