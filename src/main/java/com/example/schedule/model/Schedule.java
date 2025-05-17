package com.example.schedule.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "schedules")
@NoArgsConstructor
@AllArgsConstructor
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_number", nullable = false)
    private String groupNumber;
    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Day> days = new ArrayList<>();
    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;

    // Метод для корректного добавления объекта класса Day
    public void addDay(Day day) {
        day.setSchedule(this);
        this.days.add(day);
    }

}
