package ru.dragomirov.taskschedule.core.task.get;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dragomirov.taskschedule.core.task.Task;
import ru.dragomirov.taskschedule.core.task.TaskDto;
import ru.dragomirov.taskschedule.core.task.TaskMapper;
import ru.dragomirov.taskschedule.core.task.TaskService;

import java.util.List;

@RestController
@RequestMapping("/api/tasks/get-all")
@RequiredArgsConstructor
public class GetAllTaskController {
    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @GetMapping
    public List<TaskDto> get() {
        List<Task> taskList = taskService.getAll();

        return taskMapper.toDto(taskList);
    }
}
