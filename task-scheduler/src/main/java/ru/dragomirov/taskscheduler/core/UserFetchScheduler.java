package ru.dragomirov.taskscheduler.core;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.dragomirov.taskscheduler.commons.EmailProducer;
import ru.dragomirov.taskscheduler.commons.MessageDto;
import ru.dragomirov.taskscheduler.commons.TaskDto;
import ru.dragomirov.taskscheduler.commons.UserDto;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserFetchScheduler {
    private final UserServiceClient userServiceClient;
    private final EmailProducer emailProducer;

    @Scheduled(fixedRate = 30000)
    public void getPendingTasksForDay() {
        List<UserDto> users = userServiceClient.getAllUsers();
        String[] targetStatuses = {"TODO", "IN_PROGRESS"};
        LocalDate yesterday = LocalDate.now().minusDays(1);

        List<UserDto> usersWithIncompleteTasks = filterUsersAndTasksByStatusAndDate(users, targetStatuses,
                yesterday.atStartOfDay());

        usersWithIncompleteTasks.forEach(user -> {
            MessageDto message = new MessageDto(user, "OUTSTANDING_TASKS", new HashMap<>());
            emailProducer.sendEmailMessage(message);
        });
    }

    @Scheduled(fixedRate = 30000)
    public void getAllTaskForDay() {
        List<UserDto> users = userServiceClient.getAllUsers();
        String[] incompleteStatuses = {"TODO", "IN_PROGRESS"};
        String[] completeStatuses = {"DONE"};
        LocalDate yesterday = LocalDate.now().minusDays(1);

        users.forEach(user -> {
            List<TaskDto> incompleteTasks = user.getTaskDtos().stream()
                    .filter(task -> List.of(incompleteStatuses).contains(task.getStatus()) && task.getCreatedDate().toLocalDate().isEqual(yesterday))
                    .collect(Collectors.toList());

            List<TaskDto> completeTasks = user.getTaskDtos().stream()
                    .filter(task -> List.of(completeStatuses).contains(task.getStatus()) && task.getCreatedDate().toLocalDate().isEqual(yesterday))
                    .collect(Collectors.toList());

            if (!incompleteTasks.isEmpty() && !completeTasks.isEmpty()) {
                MessageDto message = new MessageDto(user, "ALL_TASK_STATISTICS", new HashMap<>());
                message.body.put("incompleteTasks", incompleteTasks);
                message.body.put("completeTasks", completeTasks);
                emailProducer.sendEmailMessage(message);
            }
        });
    }

    @Scheduled(fixedRate = 30000)
    public void getCompletedTasksForDay() {
        List<UserDto> users = userServiceClient.getAllUsers();
        String[] targetStatuses = {"DONE"};
        LocalDate yesterday = LocalDate.now().minusDays(1);

        List<UserDto> usersWithTasks = filterUsersAndTasksByStatusAndDate(users, targetStatuses,
                yesterday.atStartOfDay());

        usersWithTasks.forEach(user -> {
            MessageDto message = new MessageDto(user, "COMPLETED_TASKS", new HashMap<>());
            emailProducer.sendEmailMessage(message);
        });
    }

    private List<UserDto> filterUsersAndTasksByStatusAndDate(List<UserDto> users, String[] targetStatuses, LocalDateTime targetDate) {
        return users.stream()
                .map(user -> {
                    List<TaskDto> filteredTasks = user.getTaskDtos().stream()
                            .filter(task -> List.of(targetStatuses).contains(task.getStatus()) && task.getCreatedDate().toLocalDate().isEqual(targetDate.toLocalDate()))
                            .collect(Collectors.toList());
                    user.setTaskDtos(filteredTasks);
                    return user;
                })
                .filter(user -> !user.getTaskDtos().isEmpty())
                .collect(Collectors.toList());
    }
}
