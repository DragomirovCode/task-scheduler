package ru.dragomirov.taskschedule.core.task.delete;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dragomirov.taskschedule.core.task.TaskService;

@RestController
@RequestMapping("/api/tasks/delete")
@RequiredArgsConstructor
public class DeleteTaskController {
    private final TaskService taskService;

    @DeleteMapping("/{id}")
    public ResponseEntity delete(
            @PathVariable Long id
    ) {
        taskService.delete(id);
        return ResponseEntity.ok().build();
    }
}
