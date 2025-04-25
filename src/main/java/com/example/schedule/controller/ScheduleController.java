package com.example.schedule.controller;

import com.example.schedule.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.fleshka4.spbstu.ruz.api.models.Schedule;
import com.fleshka4.spbstu.ruz.api.models.Day;
import com.fleshka4.spbstu.ruz.api.models.Lesson;
import com.fleshka4.spbstu.ruz.api.models.Week;
import com.fleshka4.spbstu.ruz.api.RuzSpbStu;  // Импортируй этот класс, чтобы использовать getScheduleByGroupId

@Controller
public class ScheduleController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/schedule")
    public String getSchedule(@RequestParam int groupId, Model model) {
        model.addAttribute("groupId", groupId);
        try {
            // Получаем расписание с помощью метода getScheduleByGroupId
            Schedule schedule = RuzSpbStu.getScheduleByGroupId(groupId);
            if (schedule != null) {
                model.addAttribute("schedule", formatSchedule(schedule));  // Форматируем расписание
            } else {
                model.addAttribute("schedule", "Не удалось получить расписание.");
            }
        } catch (Exception e) {
            model.addAttribute("schedule", "Ошибка при получении расписания: " + e.getMessage());
        }
        return "index";
    }

    // Форматирование расписания для отображения на веб-странице
    private String formatSchedule(Schedule schedule) {
        StringBuilder sb = new StringBuilder();
        sb.append("Неделя: ").append(schedule.getWeek().toString()).append("<br>");
        for (Day day : schedule.getDays()) {
            sb.append("<br>").append("День: ").append(day.getDate()).append(" (").append(day.getWeekDay()).append(")<br>");
            for (Lesson lesson : day.getLessons()) {
                sb.append("  <b>Предмет:</b> ").append(lesson.getSubject()).append("<br>");
                sb.append("  <b>Время:</b> ").append(lesson.getTimeStart()).append(" - ").append(lesson.getTimeEnd()).append("<br>");
                sb.append("  <b>Преподаватель:</b> ").append(lesson.getTeachers()).append("<br>");
                sb.append("  <b>Аудитория:</b> ").append(lesson.getAuditories()).append("<br>");
                sb.append("  <b>Доп. информация:</b> ").append(lesson.getAdditionalInfo()).append("<br>");
                sb.append("  <b>Ссылка на вебинар:</b> <a href='").append(lesson.getWebinarUrl()).append("'>").append(lesson.getWebinarUrl()).append("</a><br>");
                sb.append("  <b>LMS ссылка:</b> <a href='").append(lesson.getLmsUrl()).append("'>").append(lesson.getLmsUrl()).append("</a><br><br>");
            }
        }
        return sb.toString();
    }
}
