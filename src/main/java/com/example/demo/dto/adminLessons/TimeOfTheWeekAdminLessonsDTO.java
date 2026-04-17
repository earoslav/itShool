package com.example.demo.dto.adminLessons;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TimeOfTheWeekAdminLessonsDTO {
    private int id;
    private Integer dayOfTheWeek;


    private Integer timeOfTheDay;


}
