package com.test.reminder.service.impl;

import com.test.reminder.domain.ReminderEntity;
import com.test.reminder.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TelegramNotificationService implements NotificationService {
    private final TelegramBot telegramBot;

    @Override
    public void sendNotification(ReminderEntity reminder) {
        if (reminder.getUser().getTelegramId() == null || reminder.getUser().getTelegramId().isBlank()) {
            return;
        }
        String message = reminder.getTitle() + "\n\n" + reminder.getDescription();
        telegramBot.sendMessage(reminder.getUser().getTelegramId(), message);
    }
}
