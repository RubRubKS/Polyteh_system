package com.example.schedule.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
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

    @Column(name = "group_number")
    private String groupNumber;
    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    private List<Lesson> lessons;
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;
}
