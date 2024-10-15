package ru.dragomirov.taskscheduleemailsender.commons;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.dragomirov.taskschedule.commons.kafka.MessageDto;


@Service
@RequiredArgsConstructor
public class EmailConsumer {

    private final MailService mailService;

    @KafkaListener(topics = "EMAIL_SENDING_TASKS", groupId = "email_group")
    public void listenForEmailTasks(MessageDto messageDto) {
        mailService.sendEmail(messageDto.getTo(), messageDto.getSubject(), messageDto.getBody());
    }
}
