package com.example.demo.repositories.other;

import com.example.demo.models.other.Comment;
import com.example.demo.models.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

// Репозиторій commentRepository дає доступ до таблиць модуля «коментарі студентів» через Spring Data JPA.
// Назви методів нижче перетворюються Spring-ом у SQL-запити без ручного написання query.
public interface commentRepository extends JpaRepository<Comment, Integer> {
    // Видаляє записи модуля «коментарі студентів» за умовами: студентом.
    // Це використовується для очищення зв’язків після видалення студента, викладача, курсу або часу.
    public void deleteAllByStudent(Student student);
}
