package ru.dragomirov.taskschedule.core.task.get;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dragomirov.taskschedule.core.task.Task;
import ru.dragomirov.taskschedule.core.task.TaskService;
import ru.dragomirov.taskschedule.core.task.update.UpdateTaskDto;
import ru.dragomirov.taskschedule.core.task.update.UpdateTaskMapper;

@RestController
@RequestMapping("/api/tasks/")
@RequiredArgsConstructor
public class GetTaskByIdController {
    private final TaskService taskService;
    private final UpdateTaskMapper updateTaskMapper;

    @GetMapping("/{id}")
    public ResponseEntity<UpdateTaskDto> get(
            @PathVariable Long id,
            Authentication authentication
    ) {
        String currentUsername = authentication.getName();
        Task task = taskService.getById(id);

        if (!task.getAuthor().getUsername().equals(currentUsername)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        UpdateTaskDto taskDto = updateTaskMapper.toDto(task);
        return ResponseEntity.ok(taskDto);
    }
}
