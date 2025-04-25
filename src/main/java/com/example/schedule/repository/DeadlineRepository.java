package com.example.schedule.repository;

import com.example.schedule.model.Deadline;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeadlineRepository extends JpaRepository<Deadline, Long> {
}