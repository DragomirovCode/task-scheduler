package ru.dragomirov.taskschedule.commons.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.dragomirov.taskschedule.auth.registration.UserRegistrationDto;

import java.util.Properties;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {

    public UserRegistrationDto userRegistrationDto;
    public String type;
    public Properties body;

}
