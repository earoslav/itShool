package com.example.demo.repositories.program;

import com.example.demo.models.programe.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Репозиторій CourseRepository дає доступ до таблиць модуля «курси» через Spring Data JPA.
// Назви методів нижче перетворюються Spring-ом у SQL-запити без ручного написання query.
public interface CourseRepository extends JpaRepository<Course, Integer> {
    // Повертає всі записи «курси» з таблиці.
    // Сервіс використовує цей метод для списків і довідників у формах.
    public List<Course> findAll();
}
