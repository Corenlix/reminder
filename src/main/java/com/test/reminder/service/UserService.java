package com.test.reminder.service;

import com.test.reminder.dto.UserDto;
import jakarta.annotation.Nullable;

public interface UserService {
    void createUserIfNotExists(String subId, @Nullable String email);
    UserDto getCurrentUser();
    void updateUser(UserDto userDto);
}
