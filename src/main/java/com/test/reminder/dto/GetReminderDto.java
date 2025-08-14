package com.test.reminder.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class GetReminderDto {
    @NotBlank
    private Long id;
    @NotBlank
    private final String title;
    @NotBlank
    private final String description;
    @NotNull
    private LocalDateTime remind;
}
