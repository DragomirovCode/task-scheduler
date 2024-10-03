package ru.dragomirov.taskschedule.core.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByAuthorId(Long userId);
    Optional<Task> findByTitle(String title);
    List<Task> findByStatusAndAuthorIdOrderByAuthorId(Status status, Long userId);
    List<Task> findByAuthorUsername(String author);
}
