package com.test.reminder.repository;

import com.test.reminder.domain.ReminderEntity;
import com.test.reminder.domain.UserEntity;
import com.test.reminder.dto.RemindersFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReminderCustomRepository {
    Page<ReminderEntity> findByFilter(UserEntity user, Pageable pageable, RemindersFilter filter);
}
