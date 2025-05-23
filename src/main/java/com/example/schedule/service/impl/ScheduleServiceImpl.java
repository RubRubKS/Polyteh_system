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

    @Value("${schedule.update.threshold.hours}")
    private int updateThresholdHours;

    @Override
    public Schedule getScheduleByGroupNumberAndDate(String groupNumber, LocalDate startDate) throws IOException {

        // Проверяем наличие расписания в БД
        Optional<Schedule> existingSchedule = scheduleRepository.getScheduleByGroupNumberAndStartDate(groupNumber, startDate);
        Optional<LocalDateTime> lastUpdated = scheduleRepository.findLastUpdatedByGroupNumberAndStartDate(groupNumber, startDate);

        // Проверяем наличие расписания и необходимость его проверки
       if (lastUpdated.isEmpty() || lastUpdated.get().isBefore(LocalDateTime.now().minusHours(updateThresholdHours)))
       {
           return getOrUpdateSchedule(groupNumber, startDate);
       }

       // Если расписание было изначально или оно было обновлено
        return existingSchedule.orElseThrow(() ->
                new ErrorResponse("Schedule not found for group " + groupNumber));
    }

    private Schedule getOrUpdateSchedule(String groupNumber, LocalDate startDate) throws IOException {
        // Получение нового расписания с сайта ruz
        Schedule schedule = getScheduleFromWebsite(groupNumber, startDate);
        // Обновление БД
        // Удаление старого расписания (если есть)
        scheduleRepository.getScheduleByGroupNumberAndStartDate(groupNumber, startDate)
                .ifPresent(s -> scheduleRepository.deleteScheduleByGroupNumber(s.getGroupNumber()));
        // Добавление нового расписания в БД
        scheduleRepository.save(schedule);
        return schedule;
    }

    // Функция для парсинга сайта расписания
    //TODO: Проследить, что не возвращает null
    private Schedule getScheduleFromWebsite(String groupNumber, LocalDate startDate) throws IOException {

        // Получаем параметры соответствующей группы (id и faculty) для получения их расписания
        Group group = groupRepository.getGroupByGroupNumber(groupNumber).orElseThrow(() ->
                new ErrorResponse("Group not found for group " + groupNumber));

        // Получаем HTML-код страницы расписания указанной группы
        String link = externalUrl + "faculty/" + group.getFaculty() + "/groups/" + group.getGroupId()
                + "?date=" + startDate.toString();

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

                // Получаем название предмета
                Element div = elementLesson.select(".lesson__subject").first();
                if (!(div == null || div.children().get(2).text().isEmpty()))
                {
                    lesson.setSubject(div.children().get(2).text());
                }

                // Получаем время проведения пары
                div = elementLesson.select(".lesson__time").first();
                if (!(div == null || div.children().get(0).text().isEmpty() || div.children().get(2).text().isEmpty()))
                {
                    lesson.setStartTime(div.children().get(0).text());
                    lesson.setEndTime(div.children().get(2).text());
                }
                else
                {
                    lesson.setStartTime("N/A");
                    lesson.setEndTime("N/A");
                }

                // Получаем тип пары
                if (!(elementLesson.select(".lesson__type").text().isEmpty()))
                {
                    lesson.setType(elementLesson.select(".lesson__type").text());
                }
                else
                {
                    lesson.setType("N/A");
                }

                // Получаем имя преподавателя
                div = elementLesson.select(".lesson__teachers").first();
                if (div != null)
                {
                    div = div.select(".lesson__link").first();
                }
                if (!(div == null || div.children().get(2).text().isEmpty()))
                {
                    lesson.setTeacher(div.children().get(2).text());
                }
                else
                {
                    lesson.setTeacher("N/A");
                }

                // Получаем аудиторию
                div = elementLesson.select(".lesson__places").first();
                if (div != null)
                {
                    div = div.select(".lesson__link").first();
                }
                if (!(div == null || div.select("span").first().select("span").first().text().isEmpty()))
                {
                    String room = "";
                    room += div.select("span").first().select("span").first().text();
                    div = div.children().last();
                    room = room + div.children().get(0).text() + div.children().get(1).text();
                    lesson.setRoom(room);
                }
                else
                {
                    lesson.setRoom("N/A");
                }
                
                div = elementLesson.select(".lesson__resource_links").first();
                if (!(div == null || div.select("a").first().attr("href").isEmpty()))
                {
                    lesson.setSdoAddress(div.select("a").first().attr("href"));
                }
                else
                {
                    lesson.setSdoAddress("N/A");
                }

                day.addLesson(lesson);
            }

            schedule.addDay(day);

        }

        schedule.setGroupNumber(groupNumber);
        schedule.setLastUpdated(LocalDateTime.now());
        schedule.setStartDate(startDate);

        return schedule;
    }

}
