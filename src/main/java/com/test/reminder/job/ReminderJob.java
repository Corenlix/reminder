package com.test.reminder.job;

import com.test.reminder.service.ReminderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class ReminderJob {
    private final ReminderService reminderService;

    @Scheduled(cron = "${reminder.job.cron}")
    public void reminderJobSchedule() {
        log.info("Start reminderJob");
        reminderService.sendReminders();
    }
}
