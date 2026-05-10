package com.example.demo.repositories.thirdTables;

import com.example.demo.models.entities.Student;
import com.example.demo.models.thirdTables.StudentCourse;
import org.springframework.data.jpa.repository.JpaRepository;

// Репозиторій StudentCourseRepository дає доступ до таблиць модуля «курси студента» через Spring Data JPA.
// Назви методів нижче перетворюються Spring-ом у SQL-запити без ручного написання query.
public interface StudentCourseRepository extends JpaRepository<StudentCourse, Integer> {
    // Видаляє записи модуля «курси студента» за умовами: студентом.
    // Це використовується для очищення зв’язків після видалення студента, викладача, курсу або часу.
    public void deleteAllByStudent(Student student);
}
