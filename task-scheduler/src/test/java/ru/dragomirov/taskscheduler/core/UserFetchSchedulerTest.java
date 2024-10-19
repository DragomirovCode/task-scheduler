package ru.dragomirov.taskscheduler.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.dragomirov.taskscheduler.commons.EmailProducer;
import ru.dragomirov.taskschedulercommondto.kafka.MessageDto;
import ru.dragomirov.taskschedulercommondto.kafka.TaskDto;
import ru.dragomirov.taskschedulercommondto.kafka.UserDto;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class UserFetchSchedulerTest {

    @Mock
    UserServiceClient userServiceClient;

    @Mock
    EmailProducer emailProducer;

    @InjectMocks
    UserFetchScheduler userFetchScheduler;

    @Test
    void getPendingTasksForDay_returnsUsersWithOutstandingTasks() {
        List<UserDto> users = Arrays.asList(
                new UserDto(1L, "user1", "user1@example.com",
                        Arrays.asList(new TaskDto(1L, "Task1", "TODO", "2024-10-18"))),
                new UserDto(2L, "user2", "user2@example.com",
                        Arrays.asList(new TaskDto(2L, "Task2", "IN_PROGRESS", "2024-10-19"))),
                new UserDto(3L, "user3", "user3@example.com",
                        Arrays.asList(new TaskDto(3L, "Task3", "DONE", "2024-10-20"),
                                new TaskDto(3L, "Task4", "TODO", "2024-10-20")))
        );

        when(userServiceClient.getAllUsers()).thenReturn(users);

        userFetchScheduler.getPendingTasksForDay();

        ArgumentCaptor<MessageDto> captor = ArgumentCaptor.forClass(MessageDto.class);
        verify(emailProducer, times(3)).sendEmailMessage(captor.capture());

        List<MessageDto> capturedMessages = captor.getAllValues();
        assertEquals(3, capturedMessages.size());
        assertEquals("TODO", capturedMessages.get(0).getUserDto().getTaskDtos().get(0).getStatus());
        assertEquals("IN_PROGRESS", capturedMessages.get(1).getUserDto().getTaskDtos().get(0).getStatus());
        assertEquals("TODO", capturedMessages.get(2).getUserDto().getTaskDtos().get(0).getStatus());

        Assertions.assertNotEquals("DONE", capturedMessages.get(0).getUserDto().getTaskDtos().get(0).getStatus());
        Assertions.assertNotEquals("DONE", capturedMessages.get(1).getUserDto().getTaskDtos().get(0).getStatus());
    }

    @Test
    void getCompletedTasksForDay_returnsUsersWithCompletedTasks() {
        List<UserDto> users = Arrays.asList(
                new UserDto(1L, "user1", "user1@example.com",
                        Arrays.asList(new TaskDto(1L, "Task1", "TODO", "2024-10-18"))),
                new UserDto(2L, "user2", "user2@example.com",
                        Arrays.asList(new TaskDto(2L, "Task2", "IN_PROGRESS", "2024-10-19"))),
                new UserDto(3L, "user3", "user3@example.com",
                        Arrays.asList(new TaskDto(3L, "Task3", "DONE", "2024-10-20"),
                                new TaskDto(3L, "Task4", "TODO", "2024-10-20")))
        );

        when(userServiceClient.getAllUsers()).thenReturn(users);

        userFetchScheduler.getCompletedTasksForDay();

        ArgumentCaptor<MessageDto> captor = ArgumentCaptor.forClass(MessageDto.class);
        verify(emailProducer, times(1)).sendEmailMessage(captor.capture());

        List<MessageDto> capturedMessages = captor.getAllValues();
        assertEquals(1, capturedMessages.size());
        assertEquals("DONE", capturedMessages.get(0).getUserDto().getTaskDtos().get(0).getStatus());
    }

    @Test
    void getAllTaskForDay_returnsUsersWithAllTasks() {
        List<UserDto> users = Arrays.asList(
                new UserDto(1L, "user1", "user1@example.com",
                        Arrays.asList(new TaskDto(1L, "Task1", "TODO", "2024-10-18"))),
                new UserDto(2L, "user2", "user2@example.com",
                        Arrays.asList(new TaskDto(2L, "Task2", "IN_PROGRESS", "2024-10-19"))),
                new UserDto(3L, "user3", "user3@example.com",
                        Arrays.asList(new TaskDto(3L, "Task3", "DONE", "2024-10-20"),
                                new TaskDto(4L, "Task4", "TODO", "2024-10-20")))
        );

        when(userServiceClient.getAllUsers()).thenReturn(users);

        userFetchScheduler.getAllTaskForDay();

        ArgumentCaptor<MessageDto> captor = ArgumentCaptor.forClass(MessageDto.class);
        verify(emailProducer, times(1)).sendEmailMessage(captor.capture());

        List<MessageDto> capturedMessages = captor.getAllValues();

        assertEquals(1, capturedMessages.size());
        assertEquals("user3", capturedMessages.get(0).userDto.username);
    }
}
