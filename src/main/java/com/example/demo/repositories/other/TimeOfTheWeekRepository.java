package com.example.demo.repositories.other;

import com.example.demo.models.other.TimeOfTheWeek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface TimeOfTheWeekRepository extends JpaRepository<TimeOfTheWeek, Integer> {
    public TimeOfTheWeek findById(int id);
    public TimeOfTheWeek findFirstByDayOfTheWeekAndTimeOfTheDay(int day, int hour);
    public TimeOfTheWeek findByDayOfTheWeekAndTimeOfTheDayAndMinute(int day, int hour, int minute);
}
