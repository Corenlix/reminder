package com.test.reminder.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "reminder")
public class ReminderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String title;

    private String description;

    private LocalDateTime remind;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private UserEntity user;
}