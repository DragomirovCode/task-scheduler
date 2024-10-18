package ru.dragomirov.taskscheduler.core;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.dragomirov.taskscheduler.commons.EmailProducer;
import ru.dragomirov.taskschedulercommondto.kafka.MessageDto;
import ru.dragomirov.taskschedulercommondto.kafka.UserDto;

import java.util.List;
import java.util.Properties;
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

        List<UserDto> usersWithIncompleteTasks = sortUsersByStatus(users, targetStatuses);

        usersWithIncompleteTasks.forEach(user -> {
            MessageDto message = new MessageDto(user, "OUTSTANDING_TASKS", new Properties());
            emailProducer.sendEmailMessage(message);
        });
    }

    @Scheduled(fixedRate = 30000)
    public void getCompletedTasksForDay() {
        List<UserDto> users = userServiceClient.getAllUsers();
        String[] targetStatuses = {"DONE"};

        List<UserDto> usersWithTasks = sortUsersByStatus(users, targetStatuses);

        usersWithTasks.forEach(user -> {
            MessageDto message = new MessageDto(user, "COMPLETED_TASKS", new Properties());
            emailProducer.sendEmailMessage(message);
        });
    }

    @Scheduled(fixedRate = 30000)
    public void getAllTaskForDay() {
        List<UserDto> users = userServiceClient.getAllUsers();
        String[] targetStatuses = {"TODO", "IN_PROGRESS", "DONE"};

        List<UserDto> usersWithTasks = sortUsersByStatus(users, targetStatuses);

        usersWithTasks.forEach(user -> {
            MessageDto message = new MessageDto(user, "ALL_TASKS", new Properties());
            emailProducer.sendEmailMessage(message);
        });
    }

    private List<UserDto> sortUsersByStatus(List<UserDto> users, String[] targetStatuses) {
        return users.stream()
                .filter(user -> user.getTaskDtos() != null)
                .filter(user -> user.getTaskDtos().stream()
                        .anyMatch(task -> List.of(targetStatuses).contains(task.getStatus())))
                .collect(Collectors.toList());
    }
}
