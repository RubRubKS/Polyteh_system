package com.example.schedule.service.impl;

import com.example.schedule.model.*;
import com.example.schedule.repository.GroupRepository;
import com.example.schedule.repository.ScheduleRepository;
import com.example.schedule.service.ScheduleService;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Optional;
import java.time.LocalDateTime;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final GroupRepository groupRepository;

    @Value("${external.url}")
    private String externalUrl;

    @Override
    public Schedule getScheduleByGroupNumber(String groupNumber) throws IOException {

        // Проверяем наличие расписания в БД
        Optional<Schedule> existingSchedule = scheduleRepository.getScheduleByGroupNumber(groupNumber);
        Optional<LocalDateTime> lastUpdated = scheduleRepository.findLastUpdatedByGroupNumber(groupNumber);

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

    private Schedule getOrUpdateSchedule(String groupNumber) throws IOException {

        // Получение нового расписания с сайта ruz
        Schedule schedule = getScheduleFromWebsite(groupNumber);

        // Обновление БД
        // Удаление старого расписания (если есть)
        scheduleRepository.getScheduleByGroupNumber(groupNumber)
                .ifPresent(s -> scheduleRepository.deleteScheduleByGroupNumber(s.getGroupNumber()));
        // Добавление нового расписания в БД
        scheduleRepository.save(schedule);

        return schedule;
    }

    // Функция для парсинга сайта расписания
    private Schedule getScheduleFromWebsite(String groupNumber) throws IOException {

        // Получаем параметры соответствующей группы (id и faculty) для получения их расписания
        Group group = groupRepository.getGroupByGroupNumber(groupNumber).orElseThrow(() ->
                new ErrorResponse("Group not found for group " + groupNumber));

        // Получаем HTML-код страницы расписания указанной группы
        String link = externalUrl + "faculty/" + group.getFaculty() + "/groups/" + group.getGroupId()
                + "?date=" + LocalDateTime.now();

        Document doc = Jsoup.connect(link)
                .userAgent("Mozilla/5.0")
                .timeout(10000)
                .ignoreHttpErrors(true)
                .get();

        Schedule schedule = new Schedule();

        // Получаем блоки учебных дней для извлечения данных
        Elements elementsScheduleDays = doc.getElementsByClass("schedule__day");

        // Извлекаем данные о каждом учебном дне
        for (Element scheduleDay : elementsScheduleDays)
        {
            Day day = new Day();
            day.setDayOfWeek(scheduleDay.select(".schedule__date").text());

            Elements elementsLessons = scheduleDay.select(".lesson");
            for (Element elementLesson : elementsLessons)
            {

                Lesson lesson = new Lesson();

                Element div = elementLesson.select(".lesson__subject").first();
                lesson.setSubject(div.children().get(2).text());

                div = elementLesson.select(".lesson__time").first();
                lesson.setStartTime(div.children().get(0).text());
                lesson.setEndTime(div.children().get(2).text());

                lesson.setType(elementLesson.select(".lesson__type").text());

                div = elementLesson.select(".lesson__teachers").first()
                        .select(".lesson__link").first();
                lesson.setTeacher(div.children().get(2).text());

                div = elementLesson.select(".lesson__places").first()
                        .select(".lesson__link").first();
                String room = "";
                room += div.select("span").first().select("span").first().text();
                div = div.children().last();
                room = room + div.children().get(0).text() + div.children().get(1).text();
                lesson.setRoom(room);

                // TODO: Добавить парсинг адреса СДО
                /*div = elementLesson.select(".lesson__resource_links").first();
                System.out.println("Получили блок с ссылкой: " + div.toString());
                lesson.setSdoAddress(div.select("a").first().attr("href"));
                System.out.println("Получили адрес СДО: " + lesson.getSdoAddress());*/

                day.addLesson(lesson);
            }

            schedule.addDay(day);

        }

        schedule.setGroupNumber(groupNumber);
        schedule.setLastUpdated(LocalDateTime.now());

        return schedule;
    }

}
