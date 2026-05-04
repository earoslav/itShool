package com.example.demo.repositories.program;

import com.example.demo.models.programe.Lesson;
import com.example.demo.models.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    public void deleteAllByStudent(Student student);
    public List<Lesson> findAllByTeacherId(int id);
    public Lesson findById(int id);
    public Lesson findByLessonTimeAndStudentId(LocalDateTime time, int id);
    public List<Lesson> findAllByTimeOfTheWeekIdAndTeacherId(int timeOfTheWeekId, int teacherId);
    public List<Lesson> findAllByTeacherIdAndStudentId(int teacherId, int studentId);
    public void removeAllByStudentIdAndTeacherIdAndTimeOfTheWeekIdAndLessonTimeAfter(int stId,int teachId, int tswId, LocalDateTime now);
    public void removeAllByTimeOfTheWeekId(int id);
    public List<Lesson> findAllByTeacherIdAndLessonTime(int id, LocalDateTime time);
    public List<Lesson> findByStudentId(int stId);
    public Lesson searchByLessonTimeAndTeacherId(LocalDateTime time, int id);
    public Lesson findByLessonTime(LocalDateTime time);
    public List<Lesson> findAllByTeacherIdAndStudentIdAndTimeOfTheWeekId(int teachId, int stId, int weekId);
}
