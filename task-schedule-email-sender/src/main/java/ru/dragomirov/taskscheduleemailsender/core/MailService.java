package ru.dragomirov.taskscheduleemailsender.core;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import freemarker.template.Configuration;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.mail.javamail.MimeMessageHelper;
import ru.dragomirov.taskschedulercommondto.kafka.MessageDto;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    private final Configuration configuration;

    @Value("${spring.my-email}")
    private String email;

    public void sendEmail(MessageDto dto) {
        switch (dto.type) {
            case "REGISTRATION" -> sendRegistrationEmail(dto);
            case "OUTSTANDING_TASKS" -> sendPendingTasksReminder(dto);
            case "COMPLETED_TASKS" -> sendCompletedTasksReminder(dto);
            case "ALL_TASK_STATISTICS" -> sendAllTasksReminder(dto);
        }
    }

    @SneakyThrows
    private void sendRegistrationEmail(MessageDto dto) {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setFrom(email);
        helper.setTo(dto.userDto.email);
        helper.setSubject("Welcome to our service!");
        String emailContent = getRegistrationEmailContent(dto);
        helper.setText(emailContent, true);

        mailSender.send(mimeMessage);

    }

    @SneakyThrows
    private void sendPendingTasksReminder(MessageDto dto) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setFrom(email);
        helper.setTo(dto.userDto.email);
        helper.setSubject("Notification!");
        String emailContent = getPendingTasksEmailContent(dto);
        helper.setText(emailContent, true);

        mailSender.send(mimeMessage);
    }

    @SneakyThrows
    private void sendCompletedTasksReminder(MessageDto dto) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setFrom(email);
        helper.setTo(dto.userDto.email);
        helper.setSubject("Notification!");
        String emailContent = getCompletedTasksEmailContent(dto);
        helper.setText(emailContent, true);

        mailSender.send(mimeMessage);
    }

    @SneakyThrows
    private void sendAllTasksReminder(MessageDto dto) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setFrom(email);
        helper.setTo(dto.getUserDto().getEmail());
        helper.setSubject("Your Daily Task Statistics");

        Map<String, Object> model = new HashMap<>();
        model.put("name", dto.getUserDto().getUsername());
        model.put("incompleteTasks", dto.body.get("incompleteTasks"));
        model.put("completeTasks", dto.body.get("completeTasks"));

        String emailContent = getTaskStatisticsEmailContent(model);
        helper.setText(emailContent, true);

        mailSender.send(mimeMessage);
    }

    @SneakyThrows
    private String getRegistrationEmailContent(MessageDto dto) {
        StringWriter writer = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("name", dto.userDto.username);

        configuration.getTemplate("register.ftlh")
                .process(model, writer);

        return writer.getBuffer().toString();
    }

    @SneakyThrows
    private String getPendingTasksEmailContent(MessageDto dto) {
        StringWriter writer = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("name", dto.userDto.username);
        model.put("totalTasks", dto.userDto.taskDtos.size());
        model.put("tasks", dto.userDto.taskDtos.stream().limit(5).collect(Collectors.toList()));

        configuration.getTemplate("pending-tasks.ftlh")
                .process(model, writer);

        return writer.getBuffer().toString();
    }

    @SneakyThrows
    private String getCompletedTasksEmailContent(MessageDto dto) {
        StringWriter writer = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("name", dto.userDto.username);
        model.put("totalTasks", dto.userDto.taskDtos.size());
        model.put("tasks", dto.userDto.taskDtos.stream().limit(5).collect(Collectors.toList()));

        configuration.getTemplate("completed-tasks.ftlh")
                .process(model, writer);

        return writer.getBuffer().toString();
    }

    @SneakyThrows
    private String getTaskStatisticsEmailContent(Map<String, Object> model) {
        StringWriter writer = new StringWriter();
        configuration.getTemplate("all-tasks.ftlh").process(model, writer);
        return writer.getBuffer().toString();
    }
}
