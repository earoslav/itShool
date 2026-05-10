package com.example.demo.repositories.thirdTables;

import com.example.demo.models.thirdTables.EmptyTimesForTeacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Репозиторій EmptyTimesForTeacherRepository дає доступ до таблиць модуля «вільний час викладача» через Spring Data JPA.
// Назви методів нижче перетворюються Spring-ом у SQL-запити без ручного написання query.
public interface EmptyTimesForTeacherRepository extends JpaRepository<EmptyTimesForTeacher, Integer> {
    // Видаляє записи модуля «вільний час викладача» за умовами: викладачем.
    // Це використовується для очищення зв’язків після видалення студента, викладача, курсу або часу.
    public void removeAllByTeacherId(int id);
    // Шукає записи модуля «вільний час викладача» за умовами: викладачем.
    // Назва методу описує критерії, які Spring Data перетворює у запит до бази.
    public List<EmptyTimesForTeacher> findAllByTeacherId(int id);
}
