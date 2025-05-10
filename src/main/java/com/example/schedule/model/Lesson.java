package com.example.schedule.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Lesson {
    private Long id;

    private String subject;
    private String type; // Тип пары (лекция, практика и т.д.)
    private String teacher;
    private String room;
    private String sdoAddress; // Ссылка на СДО (если есть)
    private LocalDateTime startTime; // Когда начинается
    private LocalDateTime endTime;   // Когда заканчивается

    private String dayOfWeek;       // День недели (Пн, Вт и т.д.)
}
