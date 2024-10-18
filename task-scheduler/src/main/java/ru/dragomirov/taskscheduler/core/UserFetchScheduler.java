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
    public void checkAndSendEmailReminders() {
        List<UserDto> users = userServiceClient.getAllUsers();
        String[] targetStatuses = {"TODO", "IN_PROGRESS"};

        List<UserDto> usersWithIncompleteTasks = users.stream()
                .filter(user -> user.getTaskDtos() != null)
                .filter(user -> user.getTaskDtos().stream()
                        .anyMatch(task -> List.of(targetStatuses).contains(task.getStatus())))
                .collect(Collectors.toList());

        usersWithIncompleteTasks.forEach(user -> {
            MessageDto message = new MessageDto(user, "OUTSTANDING_TASKS", new Properties());
            emailProducer.sendEmailMessage(message);
        });
    }
}
