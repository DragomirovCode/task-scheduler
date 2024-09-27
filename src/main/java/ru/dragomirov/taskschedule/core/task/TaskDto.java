package ru.dragomirov.taskschedule.core.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class TaskDto {

    @NotBlank(message = "Title cannot be blank")
    @Size(min = 1, max = 50, message = "Title must be between 3 and 50 characters")
    public String title;

    @NotBlank(message = "Description cannot be blank")
    @Size(min = 1, max = 255, message = "Description must be between 3 and 50 characters")
    public String description;

    @NotBlank(message = "Author cannot be blank")
    @Size(min = 1, max = 255, message = "Author must be between 3 and 50 characters")
    public String author;

}
