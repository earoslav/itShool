package com.example.demo.models.programe;

import com.example.demo.models.other.TimeOfTheWeek;
import com.example.demo.models.entities.Student;
import com.example.demo.models.entities.Teacher;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Модель Lesson описує сутність модуля «уроки» у базі даних або службовий об’єкт проекту.
// Поля класу читають сервіси, репозиторії та mapper-и під час створення сторінок і збереження змін.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "lesson")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "lesson_time")
    private LocalDateTime lessonTime;

    @Column(name = "duration")
    private float duration;

    @ManyToOne
    @JoinColumn(name = "planed_date_id")
    private TimeOfTheWeek timeOfTheWeek;

    @Column(name = "status", length = 30)
    private String status;
    // Отримує через Spring залежності Student, Teacher, Course, LocalDateTime, float, TimeOfTheWeek, String.
    // Ці сервіси й mapper-и потрібні методам класу для роботи з модулем «уроки» без ручного створення об’єктів.
    public Lesson(Student student, Teacher teacher, Course course, LocalDateTime lessonTime, float duration, TimeOfTheWeek timeOfTheWeek, String status) {
        this.student = student;
        this.teacher = teacher;
        this.course = course;
        this.lessonTime = lessonTime;
        this.duration = duration;
        this.timeOfTheWeek = timeOfTheWeek;
        this.status = status;

    }
}
