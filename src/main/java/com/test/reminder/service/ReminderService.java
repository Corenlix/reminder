package com.test.reminder.service;

import com.test.reminder.dto.CreateReminderDto;
import com.test.reminder.dto.GetReminderDto;
import com.test.reminder.dto.RemindersFilter;
import com.test.reminder.dto.UpdateReminderDto;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReminderService {
    void createReminder(CreateReminderDto createReminderDto);
    Page<GetReminderDto> getReminders(Pageable pageable, @Nullable RemindersFilter filter);
    void updateReminder(UpdateReminderDto updateReminderDto);
    void deleteReminder(Long reminderId);
    UpdateReminderDto getReminderForUpdate(Long reminderId);
    void sendReminders();
}
