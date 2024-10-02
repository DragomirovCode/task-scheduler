package ru.dragomirov.taskschedule.core.task.get;

import org.springframework.stereotype.Service;
import ru.dragomirov.taskschedule.commons.InvalidParameterException;
import ru.dragomirov.taskschedule.core.task.Status;

@Service
public class StatusValidationService {
    public Status validateStatus(String status) {
        try {
            return Status.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidParameterException("Invalid status");
        }
    }
}
