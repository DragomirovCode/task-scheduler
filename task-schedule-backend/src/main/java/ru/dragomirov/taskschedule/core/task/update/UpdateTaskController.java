package ru.dragomirov.taskschedule.core.task.update;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.dragomirov.taskschedule.auth.User;
import ru.dragomirov.taskschedule.auth.UserService;
import ru.dragomirov.taskschedule.core.task.Task;
import ru.dragomirov.taskschedule.core.task.TaskService;
import ru.dragomirov.taskschedule.core.task.get.StatusValidationService;

@RestController
@RequestMapping("/api/tasks/update")
@RequiredArgsConstructor
public class UpdateTaskController {
    private final TaskService taskService;
    private final UpdateTaskMapper updateTaskMapper;
    private final UserService userService;
    private final StatusValidationService statusValidationService;

    @PatchMapping("/{id}")
    public ResponseEntity patch(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTaskDto taskDto,
            Authentication authentication
    ) {
        statusValidationService.validateStatus(taskDto.status);

        String userName = authentication.getName();
        User user = userService.getByUsername(userName);

        if (!taskService.getById(id).getAuthor().getUsername().equals(userName)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Task updateTask = updateTaskMapper.toEntity(taskDto);
        taskService.update(id, updateTask, user);

        return ResponseEntity.ok().build();
    }
}
