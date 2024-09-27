package ru.dragomirov.taskschedule.core.task;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dragomirov.taskschedule.commons.DuplicateException;
import ru.dragomirov.taskschedule.core.task.Task;
import ru.dragomirov.taskschedule.core.task.TaskRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {
    private TaskRepository taskRepository;

    @Transactional(readOnly = true)
    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "TaskService::getById", key = "#id")
    public Optional<Task> getById(Long id) {
        return taskRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "TaskService::getByTitle", key = "#title")
    public Optional<Task> getByTitle(String title) {
        return taskRepository.findByTitle(title);
    }

    @Transactional
    @Caching(put = {
            @CachePut(value = "TaskService::getById", key = "#task.id"),
            @CachePut(value = "TaskService::getByTitle", key = "#task.title")}
    )
    public void save(Task task) {
        try {
            taskRepository.save(task);
        } catch (Exception e) {
            throw new DuplicateException("");
        }
    }

    @Transactional
    @Caching(put = {
            @CachePut(value = "TaskService::getById", key = "#updateTask.id"),
            @CachePut(value = "TaskService::getByTitle", key = "#updateTask.title")}
    )
    public void update(Long id, Task updateTask) {
        updateTask.setId(id);
        taskRepository.save(updateTask);
    }

    @Transactional
    @CacheEvict(value = "TaskService::delete", key = "#id")
    public void delete(Long id) {
        taskRepository.deleteById(id);
    }
}
