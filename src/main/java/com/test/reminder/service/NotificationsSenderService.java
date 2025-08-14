package com.test.reminder.service;

import com.test.reminder.domain.ReminderEntity;

public interface NotificationsSenderService {
    void send(ReminderEntity entity);
}
