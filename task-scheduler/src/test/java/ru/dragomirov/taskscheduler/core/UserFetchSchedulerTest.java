package ru.dragomirov.taskscheduler.core;

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
    public void testGetPendingTasksForDay() {
        List<UserDto> users = Arrays.asList(
                new UserDto(1L, "user1", "user1@example.com", Arrays.asList(new TaskDto(1L, "Task1", "TODO", "2024-10-18"))),
                new UserDto(2L, "user2", "user2@example.com", Arrays.asList(new TaskDto(2L, "Task2", "IN_PROGRESS", "2024-10-19"))),
                new UserDto(3L, "user3", "user3@example.com", Arrays.asList(new TaskDto(3L, "Task3", "DONE", "2024-10-20")))
        );

        when(userServiceClient.getAllUsers()).thenReturn(users);

        userFetchScheduler.getPendingTasksForDay();

        ArgumentCaptor<MessageDto> captor = ArgumentCaptor.forClass(MessageDto.class);
        verify(emailProducer, times(2)).sendEmailMessage(captor.capture());

        List<MessageDto> capturedMessages = captor.getAllValues();
        assertEquals("user1", capturedMessages.get(0).getUserDto().getUsername());
        assertEquals("user2", capturedMessages.get(1).getUserDto().getUsername());
    }
}
