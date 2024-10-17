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
    private String getRegistrationEmailContent(MessageDto dto) {
        StringWriter writer = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("name", dto.userDto.username);

        configuration.getTemplate("register.ftlh")
                .process(model, writer);

        return writer.getBuffer().toString();
    }
}
