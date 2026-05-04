package com.example.demo.dto.other;

import com.example.demo.dto.programe.LessonDTO;
import com.example.demo.dto.thirdTable.EmptyTimesForTeacherDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TimeOfTheWeekDTO {
    private int id;
    private Integer dayOfTheWeek;
    private Integer timeOfTheDay;
    private Integer minute;
}
