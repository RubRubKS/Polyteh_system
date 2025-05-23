package com.example.schedule.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Data
@Entity
@Table(name = "lessons")
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "day_id", nullable = false)
    @JsonBackReference
    private Day day; // К какому расписанию пара принадлежит

    @Column(nullable = false)
    private String subject;
    @Column(nullable = false)
    private String type; // Тип пары (лекция, практика и т.д.)
    @Column(nullable = false)
    private String teacher;
    @Column(nullable = false)
    private String room;
    @Column(nullable = false)
    private String sdoAddress; // Ссылка на СДО (если есть)
    @Column(nullable = false)
    private String startTime; // Когда начинается
    @Column(nullable = false)
    private String endTime;   // Когда заканчивается
}
