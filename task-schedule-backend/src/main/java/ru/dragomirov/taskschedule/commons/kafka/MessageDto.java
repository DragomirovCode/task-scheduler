package ru.dragomirov.taskschedule.commons.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {

    public String to;
    public String subject;
    public String body;

}
