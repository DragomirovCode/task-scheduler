package ru.dragomirov.taskschedule.core.task.create;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dragomirov.taskschedule.auth.User;
import ru.dragomirov.taskschedule.auth.UserService;
import ru.dragomirov.taskschedule.core.task.Task;
import ru.dragomirov.taskschedule.core.task.TaskDto;
import ru.dragomirov.taskschedule.core.task.TaskMapper;
import ru.dragomirov.taskschedule.core.task.TaskService;

import java.util.Optional;

@RestController
@RequestMapping("/api/tasks/create-task")
@RequiredArgsConstructor
public class CreateTaskController {
    private final TaskService taskService;
    private final TaskMapper taskMapper;
    private final UserService userService;

    @PostMapping
    public TaskDto post(
            @Valid @RequestBody TaskDto taskDto,
            Authentication authentication
    ) {
        String userName = authentication.getName();
        Optional<User> user = userService.getByUsername(userName);
        Task task = taskMapper.toEntity(taskDto);
        taskService.save(task, user.get().getId());

        return taskMapper.toDto(task);
    }
}
