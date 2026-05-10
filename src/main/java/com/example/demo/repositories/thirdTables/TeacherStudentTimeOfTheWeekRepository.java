package com.example.demo.repositories.thirdTables;

import com.example.demo.models.thirdTables.TeacherStudentTimeOfTheWeek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

// Репозиторій TeacherStudentTimeOfTheWeekRepository дає доступ до таблиць модуля «постійний розклад викладача зі студентом» через Spring Data JPA.
// Назви методів нижче перетворюються Spring-ом у SQL-запити без ручного написання query.
@Repository
@EnableJpaRepositories
public interface TeacherStudentTimeOfTheWeekRepository extends JpaRepository<TeacherStudentTimeOfTheWeek, Integer> {
    // Видаляє записи модуля «постійний розклад викладача зі студентом» за умовами: викладачем, time of the time of the week id.
    // Це використовується для очищення зв’язків після видалення студента, викладача, курсу або часу.
    public void removeAllByTeacherIdAndTimeOfTheWeekId(int tId, int wId);
    // Шукає записи модуля «постійний розклад викладача зі студентом» за умовами: викладачем.
    // Назва методу описує критерії, які Spring Data перетворює у запит до бази.
    public List<TeacherStudentTimeOfTheWeek> findAllByTeacherId(int id);
    // Видаляє записи модуля «постійний розклад викладача зі студентом» за умовами: викладачем, студентом.
    // Це використовується для очищення зв’язків після видалення студента, викладача, курсу або часу.
    public void removeAllByTeacherIdAndStudentId(int idT, int idSt);
    // Видаляє записи модуля «постійний розклад викладача зі студентом» за умовами: студентом, time of the time of the week id.
    // Це використовується для очищення зв’язків після видалення студента, викладача, курсу або часу.
    public void removeAllByStudentIdAndTimeOfTheWeekId(int idSt, int idTsw);
    // Видаляє записи модуля «постійний розклад викладача зі студентом» за умовами: студентом, викладачем, time of the time of the week id.
    // Це використовується для очищення зв’язків після видалення студента, викладача, курсу або часу.
    public void removeAllByStudentIdAndTeacherIdAndTimeOfTheWeekId(int stId, int teachId, int tswId);
    // Видаляє записи модуля «постійний розклад викладача зі студентом» за умовами: студентом.
    // Це використовується для очищення зв’язків після видалення студента, викладача, курсу або часу.
    public void removeAllByStudentId(int id);
}
