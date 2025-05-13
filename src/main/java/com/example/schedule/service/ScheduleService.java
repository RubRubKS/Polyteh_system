package com.example.schedule.service;


import com.example.schedule.model.Schedule;

import java.io.IOException;

public interface ScheduleService {

    public Schedule getScheduleByGroupNumber(String groupNumber) throws IOException;

}
