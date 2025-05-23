package com.example.schedule.service;


import com.example.schedule.model.Schedule;

import java.io.IOException;
import java.time.LocalDate;

public interface ScheduleService {

    public Schedule getScheduleByGroupNumberAndDate(String groupNumber, LocalDate startDate) throws IOException;

}
