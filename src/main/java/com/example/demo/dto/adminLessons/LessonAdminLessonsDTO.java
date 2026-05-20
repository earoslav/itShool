package com.example.demo.dto.adminLessons;

import lombok.*;

import java.time.LocalDateTime;
// This class separates the external representation of information from JPA entities.
// It contains only the fields required for display or form processing.

// The LessonAdminLessonsDTO DTO transfers "lessons" module data between controllers, forms, and templates.
// The class contains no business logic, only fields required by the web layer for displaying or receiving data.
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class LessonAdminLessonsDTO {
    private int id;

    private StudentAdminLessonsDTO student;


    private TeacherAdminLessonsDTO teacher;


    private CourseAdminLessonsDTO course;


    private LocalDateTime lessonTime;

    private float duration;

    private String status;



}
