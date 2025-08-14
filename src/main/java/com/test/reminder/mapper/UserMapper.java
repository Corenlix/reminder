package com.test.reminder.mapper;

import com.test.reminder.domain.UserEntity;
import com.test.reminder.dto.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(UserEntity userEntity);
}
