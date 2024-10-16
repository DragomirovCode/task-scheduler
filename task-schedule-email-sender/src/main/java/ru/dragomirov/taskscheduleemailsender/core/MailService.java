package ru.dragomirov.taskscheduleemailsender.core;

import freemarker.template.TemplateException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import freemarker.template.Configuration;
import ru.dragomirov.taskschedule.commons.kafka.MessageDto;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.mail.javamail.MimeMessageHelper;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    private final Configuration configuration;

    @Value("${spring.my-email}")
    private String email;

    public void sendEmail(MessageDto dto) {
        switch (dto.type) {
            case "REGISTRATION" -> sendRegistrationEmail(dto, dto.body);
        }
    }

    private void sendRegistrationEmail(MessageDto dto, Properties body) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(email);
            helper.setTo(dto.getUserRegistrationDto().getEmail());
            helper.setSubject("Welcome to our service!");

            // Получение контента из шаблона
            String emailContent = getRegistrationEmailContent(dto);

            // Установка HTML содержимого письма
            helper.setText(emailContent, true);  // true - это чтобы письмо было в формате HTML

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
            // Логирование ошибки
        }
    }

    private String getRegistrationEmailContent(MessageDto dto)  {
        StringWriter writer = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("name", dto.userRegistrationDto.username);

        try {
            configuration.getTemplate("register.ftlh")
                    .process(model, writer);
        } catch (TemplateException | IOException e) {
            throw new RuntimeException(e);
        }

        return writer.getBuffer().toString();
    }
}
