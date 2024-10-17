package ru.dragomirov.taskschedulercommondto.kafka;

import lombok.Data;

@Data
public class TaskDto {

    public Long id;
    public String status;
    public String expirationData;

}
