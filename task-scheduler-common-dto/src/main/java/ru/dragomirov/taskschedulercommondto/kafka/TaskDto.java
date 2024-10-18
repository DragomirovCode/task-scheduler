package ru.dragomirov.taskschedulercommondto.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {

    public Long id;
    public String title;
    public String status;
    public String expirationData;

}
