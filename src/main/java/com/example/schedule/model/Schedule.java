package com.example.schedule.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import java.util.List;

@Data
@Builder
public class Schedule {
    private Long id;

    private String group;
    private List<Lesson> lessons;
}
