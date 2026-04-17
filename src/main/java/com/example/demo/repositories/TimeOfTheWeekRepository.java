package com.example.demo.repositories;

import com.example.demo.models.TimeOfTheWeek;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeOfTheWeekRepository extends JpaRepository<TimeOfTheWeek, Integer> {
    public TimeOfTheWeek findById(int id);
    public TimeOfTheWeek findByDayOfTheWeekAndTimeOfTheDay(int day, int hour);
}
