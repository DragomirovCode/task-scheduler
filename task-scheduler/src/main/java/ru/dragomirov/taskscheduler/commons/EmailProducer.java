package ru.dragomirov.taskscheduler.commons;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.dragomirov.taskscheduler.commons.message.MessageDto;

@Service
public class EmailProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${spring.kafka.topic.email}")
    private String emailTopic;

    public EmailProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendEmailMessage(MessageDto messageDto) {
        try {
            String jsonString = objectMapper.writeValueAsString(messageDto);
            kafkaTemplate.send(emailTopic, jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
