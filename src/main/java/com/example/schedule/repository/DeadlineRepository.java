package com.example.schedule.repository;

import com.example.schedule.model.Deadline;
import com.example.schedule.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeadlineRepository extends JpaRepository<Deadline, Long> {

    public List<Deadline> findAllDeadlineByUserId(Long userId);
    public void deleteDeadlineById(Long id);

}