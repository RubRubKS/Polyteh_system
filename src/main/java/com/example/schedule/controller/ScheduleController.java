package com.example.schedule.controller;

import com.example.schedule.model.ErrorResponse;
import com.example.schedule.model.Schedule;
import com.example.schedule.repository.ScheduleRepository;
import com.example.schedule.service.ScheduleService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/schedule")
@AllArgsConstructor
public class ScheduleController {

    private final ScheduleRepository scheduleRepository;
    ScheduleService scheduleService;

    @GetMapping ("/{group_number}/{date}")
    public ResponseEntity<?> getScheduleByGroupNumber(
            @PathVariable String group_number,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date)
    {

        try {
            Schedule schedule = scheduleService.getScheduleByGroupNumberAndDate(group_number, date); // Обращаемся в сервис для получения расписания

            // Расписание не найдено
            if (schedule == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Расписание для группы с номером " + group_number + " не найдено"));
            }

            // Возвращаем успешный ответ с расписанием
            return ResponseEntity.ok(schedule);
        }
        catch (Exception e) {
            //TODO: добавить журнализацию ошибок (опционально)

            // Возвращаем ошибку сервера
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Произошла ошибка при получении расписания"));
        }
    }

}
