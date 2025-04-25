package com.example.schedule.service;

import com.fleshka4.spbstu.ruz.api.RuzSpbStu;
import com.fleshka4.spbstu.ruz.api.models.Schedule;
import org.springframework.stereotype.Service;

@Service
public class ScheduleService {

    public Schedule getScheduleByGroupId(int groupId) {
        return RuzSpbStu.getScheduleByGroupId(groupId);
    }
}
