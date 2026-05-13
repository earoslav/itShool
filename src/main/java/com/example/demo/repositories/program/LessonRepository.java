package com.example.demo.repositories.program;

import com.example.demo.models.programe.Lesson;
import com.example.demo.models.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

// Репозиторій LessonRepository дає доступ до таблиць модуля «уроки» через Spring Data JPA.
// Назви методів нижче перетворюються Spring-ом у SQL-запити без ручного написання query.
public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    // Видаляє записи модуля «уроки» за умовами: студентом.
    // Це використовується для очищення зв’язків після видалення студента, викладача, курсу або часу.
    public void deleteAllByStudent(Student student);
    // Шукає записи модуля «уроки» за умовами: викладачем.
    // Назва методу описує критерії, які Spring Data перетворює у запит до бази.
    public List<Lesson> findAllByTeacherId(int id);
    // Шукає записи модуля «уроки» за умовами: id.
    // Назва методу описує критерії, які Spring Data перетворює у запит до бази.
    public Lesson findById(int id);
    // Шукає записи модуля «уроки» за умовами: датою уроку, студентом.
    // Назва методу описує критерії, які Spring Data перетворює у запит до бази.
    public Lesson findByLessonTimeAndStudentId(LocalDateTime time, int id);
    // Шукає записи модуля «уроки» за умовами: time of the time of the week id, викладачем.
    // Назва методу описує критерії, які Spring Data перетворює у запит до бази.
    public List<Lesson> findAllByTimeOfTheWeekAndTeacherId(int timeOfTheWeekId, int teacherId);
    // Шукає записи модуля «уроки» за умовами: викладачем, студентом.
    // Назва методу описує критерії, які Spring Data перетворює у запит до бази.
    public List<Lesson> findAllByTeacherIdAndStudentId(int teacherId, int studentId);
    // Видаляє записи модуля «уроки» за умовами: студентом, викладачем, time of the time of the week id, уроками після заданого часу.
    // Це використовується для очищення зв’язків після видалення студента, викладача, курсу або часу.
    public void removeAllByStudentIdAndTeacherIdAndTimeOfTheWeekAndLessonTimeAfter(int stId,int teachId, int tswId, LocalDateTime now);
    // Видаляє записи модуля «уроки» за умовами: time of the time of the week id.
    // Це використовується для очищення зв’язків після видалення студента, викладача, курсу або часу.
    public void removeAllByTimeOfTheWeek(int id);
    // Шукає записи модуля «уроки» за умовами: викладачем, датою уроку.
    // Назва методу описує критерії, які Spring Data перетворює у запит до бази.
    public List<Lesson> findAllByTeacherIdAndLessonTime(int id, LocalDateTime time);
    // Шукає записи модуля «уроки» за умовами: студентом.
    // Назва методу описує критерії, які Spring Data перетворює у запит до бази.
    public List<Lesson> findByStudentId(int stId);
    // Шукає записи модуля «уроки» за умовами: датою уроку, викладачем.
    // Назва методу описує критерії, які Spring Data перетворює у запит до бази.
    public Lesson searchByLessonTimeAndTeacherId(LocalDateTime time, int id);
    // Шукає записи модуля «уроки» за умовами: датою уроку.
    // Назва методу описує критерії, які Spring Data перетворює у запит до бази.
    public Lesson findByLessonTime(LocalDateTime time);
    // Шукає записи модуля «уроки» за умовами: викладачем, студентом, time of the time of the week id.
    // Назва методу описує критерії, які Spring Data перетворює у запит до бази.
    public List<Lesson> findAllByTeacherIdAndStudentIdAndTimeOfTheWeek(int teachId, int stId, int weekId);

    public List<Lesson> findAllByTeacherIdAndLessonTimeAfterAndLessonTimeBefore(int teachId, LocalDateTime after, LocalDateTime before);
    public List<Lesson> findAllByStudentIdAndLessonTimeAfterAndLessonTimeBefore(int studentId, LocalDateTime after, LocalDateTime before);
}
