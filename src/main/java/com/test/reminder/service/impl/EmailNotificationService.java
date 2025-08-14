package com.test.reminder.service.impl;

import com.test.reminder.domain.ReminderEntity;
import com.test.reminder.service.NotificationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificationService implements NotificationService {
    private final JavaMailSender mailSender;
    private final String username;

    public EmailNotificationService(JavaMailSender mailSender,
                                    @Value("${spring.mail.username}") String username) {
        this.mailSender = mailSender;
        this.username = username;
    }

    public void sendNotification(ReminderEntity reminder) {
        if (reminder.getUser().getEmail() == null || reminder.getUser().getEmail().isBlank()) return;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(username);
        message.setTo(reminder.getUser().getEmail());
        message.setSubject("Напоминание " + reminder.getTitle());
        message.setText(reminder.getDescription());
        mailSender.send(message);
    }
}
