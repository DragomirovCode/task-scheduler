package ru.dragomirov.taskscheduleemailsender.commons;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.dragomirov.taskschedule.commons.kafka.MessageDto;
import ru.dragomirov.taskscheduleemailsender.core.MailService;


@Service
@RequiredArgsConstructor
public class EmailConsumer {

    private final MailService mailService;

    @KafkaListener(topics = "${spring.kafka.topic.email}", groupId = "${spring.kafka.consumer.group-id}")
    public void listenForEmailTasks(MessageDto messageDto) {
        mailService.sendEmail(messageDto.getTo(), messageDto.getSubject(), messageDto.getBody());
    }
}
