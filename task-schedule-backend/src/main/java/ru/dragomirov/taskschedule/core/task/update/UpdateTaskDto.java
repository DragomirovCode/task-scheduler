package ru.dragomirov.taskschedule.core.task.update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateTaskDto {

    public Long id;

    @NotBlank(message = "Title cannot be blank")
    @Size(min = 1, max = 50, message = "Title must be between 3 and 50 characters")
    public String title;

    @NotBlank(message = "Description cannot be blank")
    @Size(min = 1, max = 255, message = "Description must be between 3 and 50 characters")
    public String description;

    @NotBlank(message = "Status cannot be blank")
    public String status;
}
