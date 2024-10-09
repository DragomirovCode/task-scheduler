package ru.dragomirov.taskschedule.core.task.update;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.dragomirov.taskschedule.auth.User;
import ru.dragomirov.taskschedule.auth.UserService;
import ru.dragomirov.taskschedule.core.task.Status;
import ru.dragomirov.taskschedule.core.task.Task;
import ru.dragomirov.taskschedule.core.task.TaskService;

@RestController
@RequestMapping("/api/tasks/update-status")
@RequiredArgsConstructor
public class UpdateStatusTaskController {
    private final TaskService taskService;
    private final UpdateTaskMapper updateTaskMapper;
    private final UserService userService;

    @PatchMapping("/{id}")
    public ResponseEntity<UpdateTaskDto> patch(
            @PathVariable Long id,
            @RequestParam("direction") String direction,
            Authentication authentication
    ) {
        String currentUsername = authentication.getName();
        User user = userService.getByUsername(currentUsername);

        Task task = taskService.getById(id);

        if (!task.getAuthor().getUsername().equals(currentUsername)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if ("forward".equalsIgnoreCase(direction)) {
            if (task.getStatus() == Status.TODO) {
                task.setStatus(Status.IN_PROGRESS);
            } else if (task.getStatus() == Status.IN_PROGRESS) {
                task.setStatus(Status.DONE);
            }
        } else if ("backward".equalsIgnoreCase(direction)) {
            if (task.getStatus() == Status.DONE) {
                task.setStatus(Status.IN_PROGRESS);
            } else if (task.getStatus() == Status.IN_PROGRESS) {
                task.setStatus(Status.TODO);
            }
        }

        taskService.update(id, task, user);

        UpdateTaskDto updatedTaskDto = updateTaskMapper.toDto(task);
        return ResponseEntity.ok(updatedTaskDto);
    }
}
