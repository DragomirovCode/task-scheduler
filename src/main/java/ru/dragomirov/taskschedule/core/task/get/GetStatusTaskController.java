package ru.dragomirov.taskschedule.core.task.get;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.dragomirov.taskschedule.auth.User;
import ru.dragomirov.taskschedule.auth.UserService;
import ru.dragomirov.taskschedule.core.task.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks/get-list-status")
@RequiredArgsConstructor
public class GetStatusTaskController {
    private final TaskService taskService;
    private final TaskMapper taskMapper;
    private final UserService userService;

    @GetMapping("/{status}")
    public List<TaskDto> get(
            @PathVariable Status status,
            Authentication authentication
    ) {
        String userName = authentication.getName();
        Optional<User> user = userService.getByUsername(userName);

        List<Task> taskList = taskService.getByStatus(status, user.get().getId());

        return taskMapper.toDto(taskList);
    }
}
