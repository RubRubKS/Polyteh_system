package com.example.schedule.model;

import java.util.List;

public class Schedule {
    private String group;
    private List<Lesson> lessons;

    // Геттеры и сеттеры
    public String getGroup() {
        return group;
    }
    public void setGroup(String group) {
        this.group = group;
    }
    public List<Lesson> getLessons() {
        return lessons;
    }
    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }
}
