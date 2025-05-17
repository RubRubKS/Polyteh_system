package com.example.schedule.service;

import com.example.schedule.model.Deadline;

import java.util.List;

public interface DeadlineService {

    public List<Deadline> findAllDeadlines(Long userId);
    public String saveDeadline(Long userId, Deadline deadline);
    public String updateDeadline(Long userId, Deadline deadline);
    public String deleteDeadline(Long userId, Long deadlineId);

}
