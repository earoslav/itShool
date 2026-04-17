package com.example.demo.dto;

import com.example.demo.models.EmptyTimesForTeacher;
import com.example.demo.models.Lesson;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
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
    private Integer dayOfTheWeek;


    private Integer timeOfTheDay;

    private List<LessonDTO> lessons;
    private List<EmptyTimesForTeacherDTO> emptyTimesForTeachers;
}
