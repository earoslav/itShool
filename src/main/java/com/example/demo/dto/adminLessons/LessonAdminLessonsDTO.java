package com.example.demo.dto.adminLessons;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
// Такий клас відокремлює зовнішнє представлення інформації від JPA-сутностей.
// У ньому залишаються тільки поля, які потрібні для відображення або обробки форми.

// DTO LessonAdminLessonsDTO переносить дані модуля «уроки» між контролерами, формами та шаблонами.
// Клас не містить бізнес-логіки, а лише поля, які потрібні веб-рівню для показу або прийому даних.
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
