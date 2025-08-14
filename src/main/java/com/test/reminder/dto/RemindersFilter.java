package com.test.reminder.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class RemindersFilter {
    private LocalDateTime from;
    private LocalDateTime to;
    private String title;
    private String description;
}
