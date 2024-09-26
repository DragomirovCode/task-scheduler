package ru.dragomirov.taskschedule.core;

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
    @Pattern(regexp = "^[^\\s][\\S]*$", message = "Title cannot start with a space")
    public String title;

    @NotBlank(message = "Description cannot be blank")
    @Size(min = 1, max = 255, message = "Description must be between 3 and 50 characters")
    @Pattern(regexp = "^[^\\s][\\S]*$", message = "Description cannot start with a space")
    public String description;
    public Status status;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    public LocalDateTime expirationData;

}
