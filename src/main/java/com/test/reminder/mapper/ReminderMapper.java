package com.test.reminder.mapper;

import com.test.reminder.domain.ReminderEntity;
import com.test.reminder.domain.UserEntity;
import com.test.reminder.dto.CreateReminderDto;
import com.test.reminder.dto.GetReminderDto;
import com.test.reminder.dto.UpdateReminderDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ReminderMapper {
    GetReminderDto toGetDto(ReminderEntity reminderEntity);

    ReminderEntity toEntity(CreateReminderDto createReminderDto, @Context UserEntity user);

    UpdateReminderDto toUpdateDto(ReminderEntity reminderEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateEntity(UpdateReminderDto dto, @MappingTarget ReminderEntity entity);

    @AfterMapping
    default void setUser(@MappingTarget ReminderEntity entity, @Context UserEntity user) {
        entity.setUser(user);
    }
}
