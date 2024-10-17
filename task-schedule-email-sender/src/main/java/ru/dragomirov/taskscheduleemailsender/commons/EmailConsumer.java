package ru.dragomirov.taskscheduleemailsender.commons;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.dragomirov.taskscheduleemailsender.core.MailService;
import ru.dragomirov.taskschedulercommondto.kafka.MessageDto;


@Service
@RequiredArgsConstructor
public class EmailConsumer {

    private final MailService mailService;

    @KafkaListener(topics = "${spring.kafka.topic.email}", groupId = "${spring.kafka.consumer.group-id}")
    public void listenForEmailTasks(MessageDto messageDto) {
        mailService.sendEmail(messageDto);
    }
}
