package com.test.reminder.service.impl;

import com.test.reminder.domain.ReminderEntity;
import com.test.reminder.domain.UserEntity;
import com.test.reminder.dto.CreateReminderDto;
import com.test.reminder.dto.GetReminderDto;
import com.test.reminder.dto.RemindersFilter;
import com.test.reminder.dto.UpdateReminderDto;
import com.test.reminder.mapper.ReminderMapper;
import com.test.reminder.repository.ReminderRepository;
import com.test.reminder.repository.UserRepository;
import com.test.reminder.service.NotificationsSenderService;
import com.test.reminder.service.ReminderService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class ReminderServiceImpl implements ReminderService {
    private final ReminderRepository reminderRepository;
    private final UserRepository userRepository;
    private final ReminderMapper reminderMapper;
    private final AuthService authService;
    private final NotificationsSenderService notificationsSenderService;
    private final Integer batchSize;

    public ReminderServiceImpl(ReminderRepository reminderRepository,
                               UserRepository userRepository,
                               ReminderMapper reminderMapper,
                               AuthService authService,
                               NotificationsSenderService notificationsSenderService,
                               @Value("${reminder.job.batch-size}") Integer batchSize) {
        this.reminderRepository = reminderRepository;
        this.userRepository = userRepository;
        this.reminderMapper = reminderMapper;
        this.authService = authService;
        this.notificationsSenderService = notificationsSenderService;
        this.batchSize = batchSize;
    }

    @Transactional
    public void createReminder(CreateReminderDto createReminderDto) {
        Long userId = authService.getCurrentUserId();
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        ReminderEntity reminderEntity = reminderMapper.toEntity(createReminderDto, user);
        reminderRepository.save(reminderEntity);
    }

    @Transactional(readOnly = true)
    public Page<GetReminderDto> getReminders(Pageable pageable, RemindersFilter filter) {
        UserEntity user = userRepository.findById(authService.getCurrentUserId()).orElseThrow(() -> new EntityNotFoundException("User not found"));

        return reminderRepository.findByFilter(user, pageable, filter)
                .map(reminderMapper::toGetDto);
    }

    @Transactional
    public void updateReminder(UpdateReminderDto updateReminderDto) {
        ReminderEntity reminderEntity = reminderRepository.findById(updateReminderDto.getId()).orElseThrow(() -> new EntityNotFoundException("Reminder  not found"));
        authService.checkOwnership(reminderEntity.getUser().getId());

        reminderMapper.updateEntity(updateReminderDto, reminderEntity);
        reminderRepository.save(reminderEntity);
    }

    @Transactional
    public void deleteReminder(Long reminderId) {
        ReminderEntity reminderEntity = reminderRepository.findById(reminderId).orElseThrow();
        authService.checkOwnership(reminderEntity.getUser().getId());
        reminderRepository.delete(reminderEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public UpdateReminderDto getReminderForUpdate(Long reminderId) {
        ReminderEntity reminderEntity = reminderRepository.findById(reminderId).orElseThrow();
        authService.checkOwnership(reminderEntity.getUser().getId());

        return reminderMapper.toUpdateDto(reminderEntity);
    }

    @Override
    @Transactional
    public void sendReminders() {
        List<ReminderEntity> reminders = reminderRepository.findRemindsBeforeNowForUpdateSkipLocked(batchSize);
        for (ReminderEntity reminderEntity : reminders) {
            try {
                notificationsSenderService.send(reminderEntity);
                reminderRepository.delete(reminderEntity);
            } catch (Exception e) {
                log.error("Failed to send reminder {}", reminderEntity.getId(), e);
            }
        }
    }
}
