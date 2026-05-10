package com.example.demo.repositories.thirdTables;

import com.example.demo.models.thirdTables.TeacherCourse;
import org.springframework.data.jpa.repository.JpaRepository;

// Репозиторій TeacherCourseRepository дає доступ до таблиць модуля «курси викладача» через Spring Data JPA.
// Назви методів нижче перетворюються Spring-ом у SQL-запити без ручного написання query.
public interface TeacherCourseRepository extends JpaRepository<TeacherCourse, Integer> {
}
