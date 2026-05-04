package com.example.demo.dto.adminLessons;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class LessonAdminLessonsDTO {
    private int id;

    private StudentAdminLessonsDTO student;


    private TeacherAdminLessonsDTO teacher;


    private CourseAdminLessonsDTO course;


    private LocalDateTime lessonTime;

    private float duration;

    private String status;

    private TimeOfTheWeekAdminLessonsDTO timeOfTheWeek;

}