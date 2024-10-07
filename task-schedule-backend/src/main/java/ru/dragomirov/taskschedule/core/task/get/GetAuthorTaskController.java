package ru.dragomirov.taskschedule.core.task.get;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.dragomirov.taskschedule.core.task.Task;
import ru.dragomirov.taskschedule.core.task.TaskDto;
import ru.dragomirov.taskschedule.core.task.TaskMapper;
import ru.dragomirov.taskschedule.core.task.TaskService;

import java.util.List;

@RestController
@RequestMapping("/api/tasks/get-list-authors")
@RequiredArgsConstructor
public class GetAuthorTaskController {
    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @GetMapping
    public List<TaskDto> get(
            @RequestParam String author
    ) {
        List<Task> taskList = taskService.getByAuthorByUsername(author);

        return taskMapper.toDto(taskList);
    }
}
