package com.example.demo.repositories.program;

import com.example.demo.models.programe.Lesson;
import com.example.demo.models.entities.Student;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

// Репозиторій LessonRepository дає доступ до таблиць модуля «уроки» через Spring Data JPA.
// Назви методів нижче перетворюються Spring-ом у SQL-запити без ручного написання query.
public interface LessonRepository extends JpaRepository<Lesson, Integer> {

    @EntityGraph(attributePaths = {"student", "teacher", "course"})
    public Lesson findById(int id);

    @EntityGraph(attributePaths = {"student", "teacher", "course"})
    public List<Lesson> findAllByTeacherId(int id);

    @EntityGraph(attributePaths = {"student", "teacher", "course"})
    public List<Lesson> findByStudentId(int stId);

    public void deleteAllBy(Student student);

    public Lesson findByLessonTimeAndStudentId(LocalDateTime time, int id);

    public List<Lesson> findAllByTimeOfTheWeekAndTeacherId(int timeOfTheWeekId, int teacherId);

    public List<Lesson> findAllByTeacherIdAndStudentId(int teacherId, int studentId);

    public void removeAllByStudentIdAndTeacherIdAndTimeOfTheWeekAndLessonTimeAfter(int stId, int teachId, int tswId, LocalDateTime now);

    public void removeAllByTimeOfTheWeek(int id);

    public List<Lesson> findAllByTeacherIdAndLessonTime(int id, LocalDateTime time);

    public Lesson searchByLessonTimeAndTeacherId(LocalDateTime time, int id);

    public Lesson findByLessonTime(LocalDateTime time);

    public List<Lesson> findAllByTeacherIdAndStudentIdAndTimeOfTheWeek(int teachId, int stId, int weekId);

    public List<Lesson> findAllByTeacherIdAndLessonTimeAfterAndLessonTimeBefore(int teachId, LocalDateTime after, LocalDateTime before);

    public List<Lesson> findAllByStudentIdAndLessonTimeAfterAndLessonTimeBefore(int studentId, LocalDateTime after, LocalDateTime before);
}
