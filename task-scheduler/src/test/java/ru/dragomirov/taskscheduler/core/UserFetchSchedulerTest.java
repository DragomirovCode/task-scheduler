package ru.dragomirov.taskscheduler.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.dragomirov.taskscheduler.commons.EmailProducer;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.extension.ExtendWith;
import ru.dragomirov.taskscheduler.commons.message.MessageDto;
import ru.dragomirov.taskscheduler.commons.message.TaskDto;
import ru.dragomirov.taskscheduler.commons.message.UserDto;

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
        LocalDate today = LocalDate.now();
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate weekAgo = LocalDate.now().minusDays(7);

        List<UserDto> users = Arrays.asList(
                new UserDto(1L, "user1", "user1@example.com",
                        Arrays.asList(new TaskDto(1L, "Task1", "TODO", "2024-10-18", today.atStartOfDay()))),
                new UserDto(2L, "user2", "user2@example.com",
                        Arrays.asList(new TaskDto(2L, "Task2", "IN_PROGRESS", "2024-10-19", yesterday.atStartOfDay()))),
                new UserDto(3L, "user3", "user3@example.com",
                        Arrays.asList(new TaskDto(3L, "Task3", "DONE", "2024-10-20", yesterday.atStartOfDay()),
                                new TaskDto(4L, "Task4", "TODO", "2024-10-20", weekAgo.atStartOfDay())))
        );

        when(userServiceClient.getAllUsers()).thenReturn(users);

        userFetchScheduler.getPendingTasksForDay();

        ArgumentCaptor<MessageDto> captor = ArgumentCaptor.forClass(MessageDto.class);
        verify(emailProducer, times(1)).sendEmailMessage(captor.capture());

        List<MessageDto> capturedMessages = captor.getAllValues();
        assertEquals(1, capturedMessages.size());
        assertEquals("IN_PROGRESS", capturedMessages.get(0).getUserDto().getTaskDtos().get(0).getStatus());

        Assertions.assertNotEquals("DONE", capturedMessages.get(0).getUserDto().getTaskDtos().get(0).getStatus());
    }

    @Test
    void getCompletedTasksForDay_returnsUsersWithCompletedTasks() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate weekAgo = LocalDate.now().minusDays(7);

        List<UserDto> users = Arrays.asList(
                new UserDto(1L, "user1", "user1@example.com",
                        Arrays.asList(new TaskDto(1L, "Task1", "DONE", "2024-10-18", today.atStartOfDay()))),
                new UserDto(2L, "user2", "user2@example.com",
                        Arrays.asList(new TaskDto(2L, "Task2", "DONE", "2024-10-19", yesterday.atStartOfDay()))),
                new UserDto(3L, "user3", "user3@example.com",
                        Arrays.asList(new TaskDto(3L, "Task3", "DONE", "2024-10-20", weekAgo.atStartOfDay()),
                                new TaskDto(4L, "Task4", "DONE", "2024-10-20", yesterday.atStartOfDay())))
        );

        when(userServiceClient.getAllUsers()).thenReturn(users);

        userFetchScheduler.getCompletedTasksForDay();

        ArgumentCaptor<MessageDto> captor = ArgumentCaptor.forClass(MessageDto.class);
        verify(emailProducer, times(2)).sendEmailMessage(captor.capture());

        List<MessageDto> capturedMessages = captor.getAllValues();
        assertEquals(2, capturedMessages.size());
        assertEquals("DONE", capturedMessages.get(0).getUserDto().getTaskDtos().get(0).getStatus());
        assertEquals("DONE", capturedMessages.get(1).getUserDto().getTaskDtos().get(0).getStatus());
    }

    @Test
    void getAllTaskForDay_returnsUsersWithAllTasks() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = LocalDate.now().minusDays(1);

        List<UserDto> users = Arrays.asList(
                new UserDto(1L, "user1", "user1@example.com",
                        Arrays.asList(new TaskDto(1L, "Task1", "TODO", "2024-10-18", today.atStartOfDay()))),
                new UserDto(2L, "user2", "user2@example.com",
                        Arrays.asList(new TaskDto(2L, "Task2", "IN_PROGRESS", "2024-10-19", yesterday.atStartOfDay()))),
                new UserDto(3L, "user3", "user3@example.com",
                        Arrays.asList(new TaskDto(3L, "Task3", "DONE", "2024-10-20", yesterday.atStartOfDay()),
                                new TaskDto(4L, "Task4", "TODO", "2024-10-20", yesterday.atStartOfDay())))
        );

        when(userServiceClient.getAllUsers()).thenReturn(users);

        userFetchScheduler.getAllTaskForDay();

        ArgumentCaptor<MessageDto> captor = ArgumentCaptor.forClass(MessageDto.class);
        verify(emailProducer, times(1)).sendEmailMessage(captor.capture());

        List<MessageDto> capturedMessages = captor.getAllValues();

        assertEquals(1, capturedMessages.size());
        assertEquals("user3", capturedMessages.get(0).getUserDto().getUsername());
    }
}
