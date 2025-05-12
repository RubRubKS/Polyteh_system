package com.example.schedule.service;

//import com.fleshka4.spbstu.ruz.api.RuzSpbStu;
//import com.fleshka4.spbstu.ruz.api.models.Schedule;

import com.example.schedule.model.Schedule;

import java.util.Optional;

public interface ScheduleService {

    public Schedule getScheduleByGroupNumber(String groupNumber);

}
