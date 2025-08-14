package com.test.reminder.service.impl;

import com.test.reminder.domain.ReminderEntity;
import com.test.reminder.service.NotificationService;
import com.test.reminder.service.NotificationsSenderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class NotificationsSenderServiceImpl implements NotificationsSenderService {
    private final List<NotificationService> notificationServiceList;

    @Override
    public void send(ReminderEntity entity) {
        notificationServiceList.forEach(notificationService -> notificationService.sendNotification(entity));
    }
}
