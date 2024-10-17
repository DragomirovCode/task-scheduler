package ru.dragomirov.taskscheduler.commons;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.dragomirov.taskschedulercommondto.kafka.MessageDto;

@Service
public class EmailProducer {
    private final KafkaTemplate<String, MessageDto> kafkaTemplate;

    @Value("${spring.kafka.topic.email}")
    private String emailTopic;

    public EmailProducer(KafkaTemplate<String, MessageDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEmailMessage(MessageDto messageDto) {
        kafkaTemplate.send(emailTopic, messageDto);
    }
}
