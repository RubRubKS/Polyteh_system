package com.example.schedule.service.impl;

import com.example.schedule.model.ErrorResponse;
import com.example.schedule.model.Lesson;
import com.example.schedule.model.Schedule;
import com.example.schedule.repository.ScheduleRepository;
import com.example.schedule.service.ScheduleService;
// import com.fleshka4.spbstu.ruz.api.RuzSpbStu;
//import com.fleshka4.spbstu.ruz.api.models.Schedule;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.springframework.beans.factory.annotation.Value;

@Service
//@AllArgsConstructor
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository repository;

    @Value("${external.api.url}")
    private String externalApiUrl;

    // TODO: Проследить правильность написания аннотации Transaction именно для этого метода, а не любого другого
    @Override
    @Transactional
    public Schedule getScheduleByGroupNumber(String groupNumber) {
        // Проверяем наличие расписания в БД
        Optional<Schedule> existingSchedule = repository.getScheduleByGroupNumber(groupNumber);
        Optional<LocalDateTime> lastUpdated = repository.findLastUpdatedByGroupNumber(groupNumber);

        // TODO: Добавить автообновление расписания
        // Проверяем наличие расписания
        boolean needsUpdate = lastUpdated.isEmpty();

        // Если нужно обновить расписание или его вовсе нет, парсим сайт Политеха
        if (needsUpdate) {
            return getOrUpdateSchedule(groupNumber);
        }

        return existingSchedule.orElseThrow(() ->
                new ErrorResponse("Schedule not found for group " + groupNumber));
    }

    private Schedule getOrUpdateSchedule(String groupNumber) {
        // Получение нового расписания с сайта ruz
        Schedule schedule = getScheduleFromWebsite(groupNumber);

        // Обновление БД
        // Удаление старого расписания (если есть)
        repository.getScheduleByGroupNumber(groupNumber)
                .ifPresent(s -> repository.deleteScheduleByGroupNumber(s.getGroupNumber()));
        // Добавление нового расписания в БД
        repository.save(schedule);

        return schedule;
    }

    private Schedule getScheduleFromWebsite(String groupNumber) {
        try {
            JSONObject json = request(externalApiUrl + "scheduler/" + groupNumber);
            if(json.get("error") != null) {
                throw new ErrorResponse("Ошибка API: " + json.get("text"));
            }
            return parseJsonToSchedule(json, groupNumber);
        }
        catch(Exception e) {
            throw new ErrorResponse(e.getMessage());
        }
    }

    // Метод для получения JSON объекта расписания с ruz
    private static JSONObject request(String requestLink) {
        try {
            URL ruz = new URL(requestLink);
            URLConnection yc = ruz.openConnection();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            yc.getInputStream()));
            String inputLine;
            StringBuilder result = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                result.append(inputLine);
            }
            JSONParser ruzPars = new JSONParser();
            JSONObject jsonObject = (JSONObject) ruzPars.parse(result.toString());
            in.close();
            return jsonObject;
        } catch (ParseException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Метод для преобразования JSON объекта в объект класса Schedule
    private Schedule parseJsonToSchedule(JSONObject json, String groupNumber) {
        Schedule schedule = new Schedule();
        schedule.setGroupNumber(groupNumber);
        schedule.setLastUpdated(LocalDateTime.now());

        JSONArray lessonsJson = (JSONArray) json.get("lessons");
        List<Lesson> lessons = new ArrayList<>();

        for (Object lessonObj : lessonsJson)
        {
            // TODO: Часть кода для отладки. Удалить.
            for (Object key : json.keySet()) {
                System.out.println("Ключ: " + key + ", Значение: " + json.get(key));
            }
            // Удалить до сюда.

            JSONObject lessonJson = (JSONObject) lessonObj;
            Lesson lesson = new Lesson();

            lesson.setSchedule(schedule);
            lesson.setSubject(getStringSafe(lessonJson, "subject"));
            lesson.setType(getStringSafe(lessonJson, "type"));
            lesson.setTeacher(getStringSafe(lessonJson, "teacher"));
            lesson.setRoom(getStringSafe(lessonJson, "room"));
            lesson.setSdoAddress(getStringSafe(lessonJson, "sdoAddress"));
            lesson.setStartTime(getStringSafe(lessonJson, "startTime"));
            lesson.setEndTime(getStringSafe(lessonJson, "endTime"));
            lesson.setDayOfWeek(getStringSafe(lessonJson, "dayOfWeek"));

            lessons.add(lesson);
        }

        schedule.setLessons(lessons);
        return schedule;
    }

    // Вспомогательный метод для безопасного получения строки
    private String getStringSafe(JSONObject json, String key) {
        return json.containsKey(key) ? json.get(key).toString() : "N/A";
    }

}
