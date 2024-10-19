package ru.dragomirov.taskscheduler.core;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.dragomirov.taskscheduler.commons.EmailProducer;
import ru.dragomirov.taskschedulercommondto.kafka.MessageDto;
import ru.dragomirov.taskschedulercommondto.kafka.TaskDto;
import ru.dragomirov.taskschedulercommondto.kafka.UserDto;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserFetchScheduler {
    private final UserServiceClient userServiceClient;
    private final EmailProducer emailProducer;

    @Scheduled(cron = "0 0 0 * * ?")
    public void getPendingTasksForDay() {
        List<UserDto> users = userServiceClient.getAllUsers();
        String[] targetStatuses = {"TODO", "IN_PROGRESS"};

        List<UserDto> usersWithIncompleteTasks = filterUsersAndTasksByStatus(users, targetStatuses);

        usersWithIncompleteTasks.forEach(user -> {
            MessageDto message = new MessageDto(user, "OUTSTANDING_TASKS", new Properties());
            emailProducer.sendEmailMessage(message);
        });
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void getAllTaskForDay() {
        List<UserDto> users = userServiceClient.getAllUsers();
        String[] incompleteStatuses = {"TODO", "IN_PROGRESS"};
        String[] completeStatuses = {"DONE"};

        users.forEach(user -> {
            List<TaskDto> incompleteTasks = user.getTaskDtos().stream()
                    .filter(task -> List.of(incompleteStatuses).contains(task.getStatus()))
                    .collect(Collectors.toList());

            List<TaskDto> completeTasks = user.getTaskDtos().stream()
                    .filter(task -> List.of(completeStatuses).contains(task.getStatus()))
                    .collect(Collectors.toList());

            if (!incompleteTasks.isEmpty() && !completeTasks.isEmpty()) {
                MessageDto message = new MessageDto(user, "ALL_TASK_STATISTICS", new Properties());
                message.body.put("incompleteTasks", incompleteTasks);
                message.body.put("completeTasks", completeTasks);
                emailProducer.sendEmailMessage(message);
            }
        });
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void getCompletedTasksForDay() {
        List<UserDto> users = userServiceClient.getAllUsers();
        String[] targetStatuses = {"DONE"};

        List<UserDto> usersWithTasks = filterUsersAndTasksByStatus(users, targetStatuses);

        usersWithTasks.forEach(user -> {
            MessageDto message = new MessageDto(user, "COMPLETED_TASKS", new Properties());
            emailProducer.sendEmailMessage(message);
        });
    }

    private List<UserDto> filterUsersAndTasksByStatus(List<UserDto> users, String[] targetStatuses) {
        return users.stream()
                .map(user -> {
                    List<TaskDto> filteredTasks = user.getTaskDtos().stream()
                            .filter(task -> List.of(targetStatuses).contains(task.getStatus()))
                            .collect(Collectors.toList());
                    user.setTaskDtos(filteredTasks);
                    return user;
                })
                .filter(user -> !user.getTaskDtos().isEmpty())
                .collect(Collectors.toList());
    }
}
