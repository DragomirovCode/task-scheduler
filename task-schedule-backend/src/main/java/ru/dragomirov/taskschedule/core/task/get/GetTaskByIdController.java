package ru.dragomirov.taskschedule.core.task.get;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dragomirov.taskschedule.core.task.Task;
import ru.dragomirov.taskschedule.core.task.TaskService;
import ru.dragomirov.taskschedule.core.task.update.UpdateTaskDto;
import ru.dragomirov.taskschedule.core.task.update.UpdateTaskMapper;

@RestController
@RequestMapping("/api/tasks/")
@RequiredArgsConstructor
public class GetTaskByIdController {
    private final TaskService taskService;
    private final UpdateTaskMapper updateTaskMapper;

    @GetMapping("/{id}")
    public UpdateTaskDto get(
            @PathVariable Long id
    ) {
        Task task = taskService.getById(id);

        return updateTaskMapper.toDto(task);
    }
}
