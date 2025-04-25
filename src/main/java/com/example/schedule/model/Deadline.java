package com.example.schedule.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Deadline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private LocalDateTime dueDate;

    // Геттеры, сеттеры и конструкторы
}