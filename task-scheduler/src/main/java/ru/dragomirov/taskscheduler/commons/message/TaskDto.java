package ru.dragomirov.taskscheduler.commons.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {

    public Long id;
    public String title;
    public String status;
    public String expirationData;
    public LocalDateTime createdDate;

}
