package com.test.reminder.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class CreateReminderDto {
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotNull
    private LocalDateTime remind;
}
