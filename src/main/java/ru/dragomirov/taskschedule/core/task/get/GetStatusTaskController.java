package ru.dragomirov.taskschedule.core.task.get;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.dragomirov.taskschedule.core.task.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class GetStatusTaskController {
    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @GetMapping("/{status}")
    public List<TaskDto> get(
            @PathVariable Status status
    ) {
        List<Task> taskList = taskService.getByStatus(status);

        return taskMapper.toDto(taskList);
    }
}
