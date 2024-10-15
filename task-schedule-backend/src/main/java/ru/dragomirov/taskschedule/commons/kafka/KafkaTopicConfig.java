package ru.dragomirov.taskschedule.commons.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic emailTopic() {
        return TopicBuilder.name("EMAIL_SENDING_TASKS")
                .partitions(1)
                .replicas(1)
                .build();
    }
}
