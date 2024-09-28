package ru.dragomirov.taskschedule.core.task;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dragomirov.taskschedule.auth.User;
import ru.dragomirov.taskschedule.auth.UserService;
import ru.dragomirov.taskschedule.commons.DuplicateException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;

    @Transactional(readOnly = true)
    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Task> getAllByAuthorId(Long userId) {
        return taskRepository.findAllByAuthorId(userId);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "TaskService::getById", key = "#id")
    public Optional<Task> getById(Long id) {
        return taskRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "TaskService::getByStatus", key = "#status")
    public List<Task> getByStatus(Status status, Long userId) {
        return taskRepository.findByStatusAndAuthorIdOrderByAuthorId(status, userId);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "TaskService::getByAuthor", key = "#author")
    public List<Task> getByAuthorByUsername(String author) {
        return taskRepository.findByAuthorUsername(author);
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
    public void save(Task task, Long userId) {
        try {
            User user = userService.getById(userId);
            task.setStatus(Status.TODO);
            task.setAuthor(user);
            taskRepository.save(task);
            user.getTasks().add(task);
            userService.update(userId, user);
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
