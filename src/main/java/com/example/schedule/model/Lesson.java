package com.example.schedule.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "lessons")
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule; // К какому расписанию пара принадлежит

    @Column(nullable = false)
    private String subject;
    @Column(nullable = false)
    private String type; // Тип пары (лекция, практика и т.д.)
    @Column(nullable = false)
    private String teacher;
    @Column(nullable = false)
    private String room;
    @Column
    private String sdoAddress; // Ссылка на СДО (если есть)
    @Column(nullable = false)
    private LocalDateTime startTime; // Когда начинается
    @Column(nullable = false)
    private LocalDateTime endTime;   // Когда заканчивается

    @Column(nullable = false)
    private String dayOfWeek;       // День недели (Пн, Вт и т.д.)
}
