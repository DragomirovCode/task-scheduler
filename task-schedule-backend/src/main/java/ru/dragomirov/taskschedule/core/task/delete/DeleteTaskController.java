package ru.dragomirov.taskschedule.core.task.delete;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.dragomirov.taskschedule.core.task.Task;
import ru.dragomirov.taskschedule.core.task.TaskService;

@RestController
@RequestMapping("/api/tasks/delete")
@RequiredArgsConstructor
public class DeleteTaskController {
    private final TaskService taskService;

    @DeleteMapping("/{id}")
    public ResponseEntity delete(
            @PathVariable Long id,
            Authentication authentication
    ) {
        String currentUsername = authentication.getName();
        Task task = taskService.getById(id);

        if (!task.getAuthor().getUsername().equals(currentUsername)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        taskService.delete(id);

        return ResponseEntity.ok().build();
    }
}
