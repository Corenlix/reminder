package com.test.reminder.service.impl;

import com.test.reminder.domain.UserEntity;
import com.test.reminder.dto.UserDto;
import com.test.reminder.mapper.UserMapper;
import com.test.reminder.repository.UserRepository;
import com.test.reminder.service.UserService;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AuthService authService;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public void createUserIfNotExists(String subId, @Nullable String email) {
        if (!userRepository.existsBySubId(subId)) {
            try {
                UserEntity newUser = new UserEntity();
                newUser.setSubId(subId);
                newUser.setEmail(email);
                userRepository.save(newUser);
            } catch (DataIntegrityViolationException ignored) {
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getCurrentUser() {
        UserEntity user = userRepository.findById(authService.getCurrentUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public void updateUser(UserDto userDto) {
        UserEntity user = userRepository.findById(authService.getCurrentUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setEmail(userDto.getEmail());
        user.setTelegramId(userDto.getTelegramId());
        userRepository.save(user);
    }
}
