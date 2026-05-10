package com.example.demo.repositories.other;

import com.example.demo.models.other.TimeOfTheWeek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

// Репозиторій TimeOfTheWeekRepository дає доступ до таблиць модуля «часові слоти тижня» через Spring Data JPA.
// Назви методів нижче перетворюються Spring-ом у SQL-запити без ручного написання query.
@Repository
@EnableJpaRepositories
public interface TimeOfTheWeekRepository extends JpaRepository<TimeOfTheWeek, Integer> {
    // Шукає записи модуля «часові слоти тижня» за умовами: id.
    // Назва методу описує критерії, які Spring Data перетворює у запит до бази.
    public TimeOfTheWeek findById(int id);
    // Spring Data JPA створює запит на пошук записів «часові слоти тижня» за днем тижня, годиною.
    // Цей метод викликає сервісний шар, коли сторінці потрібен конкретний профіль, урок або слот.
    public TimeOfTheWeek findFirstByDayOfTheWeekAndTimeOfTheDay(int day, int hour);
    // Шукає записи модуля «часові слоти тижня» за умовами: днем тижня, годиною, хвилиною.
    // Назва методу описує критерії, які Spring Data перетворює у запит до бази.
    public TimeOfTheWeek findByDayOfTheWeekAndTimeOfTheDayAndMinute(int day, int hour, int minute);
}
