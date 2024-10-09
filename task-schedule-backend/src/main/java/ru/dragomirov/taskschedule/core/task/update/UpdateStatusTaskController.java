package ru.dragomirov.taskschedule.core.task.update;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public UpdateTaskDto patch(
            @PathVariable Long id,
            Authentication authentication
    ) {
        String currentUsername = authentication.getName();
        User user = userService.getByUsername(currentUsername);

        Task task = taskService.getById(id);

        if (task.getStatus() == Status.TODO) {
            task.setStatus(Status.IN_PROGRESS);
        } else if (task.getStatus() == Status.IN_PROGRESS) {
            task.setStatus(Status.DONE);
        }

        taskService.update(id, task, user);

        return updateTaskMapper.toDto(task);
    }
}
