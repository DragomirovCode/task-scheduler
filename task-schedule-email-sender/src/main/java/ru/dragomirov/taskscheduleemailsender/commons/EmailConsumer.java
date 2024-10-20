package ru.dragomirov.taskscheduleemailsender.commons;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.dragomirov.taskscheduleemailsender.commons.message.MessageDto;
import ru.dragomirov.taskscheduleemailsender.core.MailService;

@Service
@RequiredArgsConstructor
public class EmailConsumer {

    private final MailService mailService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${spring.kafka.topic.email}", groupId = "${spring.kafka.consumer.group-id}")
    public void listenForEmailTasks(String message) {
        try {
            MessageDto messageDto = objectMapper.readValue(message, MessageDto.class);
            mailService.sendEmail(messageDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
