package com.example.schedule.repository;


import com.example.schedule.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    public Optional<Schedule> getScheduleByGroupNumberAndStartDate(String groupNumber, LocalDate startDate);

    // Метод получения последнего обновления расписания (нужно для проверки наличия расписания и регулярных обновлений)
    @Query("SELECT s.lastUpdated FROM Schedule s " +
            "WHERE s.groupNumber = :groupNumber AND s.startDate = :startDate")
    Optional<LocalDateTime> findLastUpdatedByGroupNumberAndStartDate(String groupNumber, LocalDate startDate);

    public void deleteScheduleByGroupNumber(String groupNumber);

}
