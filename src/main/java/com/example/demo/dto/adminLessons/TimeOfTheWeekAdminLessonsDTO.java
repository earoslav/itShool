package com.example.demo.dto.adminLessons;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
// This class separates the external representation of information from JPA entities.
// It contains only the fields required for display or form processing.

// The TimeOfTheWeekAdminLessonsDTO DTO transfers "weekly time slots" module data between controllers, forms, and templates.
// The class contains no business logic, only fields required by the web layer for displaying or receiving data.
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TimeOfTheWeekAdminLessonsDTO {
    private int id;
    private Integer dayOfTheWeek;
    private Integer timeOfTheDay;
    private Integer minute;



}
