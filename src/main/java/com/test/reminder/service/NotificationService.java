package com.test.reminder.service;

import com.test.reminder.domain.ReminderEntity;

public interface NotificationService {
    void sendNotification(ReminderEntity reminder);
}
