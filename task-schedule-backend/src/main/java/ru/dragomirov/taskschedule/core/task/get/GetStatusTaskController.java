package ru.dragomirov.taskschedule.core.task.get;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.dragomirov.taskschedule.auth.User;
import ru.dragomirov.taskschedule.auth.UserService;
import ru.dragomirov.taskschedule.core.task.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks/get-list-status")
@RequiredArgsConstructor
public class GetStatusTaskController {
    private final TaskService taskService;
    private final TaskMapper taskMapper;
    private final UserService userService;
    private final StatusValidationService statusValidationService;

    @GetMapping
    public List<TaskDto> get(
            @RequestParam String status,
            Authentication authentication
    ) {
        Status statusEnum = statusValidationService.validateStatus(status);
        String userName = authentication.getName();
        User user = userService.getByUsername(userName);

        List<Task> taskList = taskService.getByStatus(statusEnum, user.getId());
        return taskMapper.toDto(taskList);
    }
}
