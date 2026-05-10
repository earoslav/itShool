package com.example.demo.models.thirdTables;

import com.example.demo.models.other.TimeOfTheWeek;
import com.example.demo.models.entities.Student;
import com.example.demo.models.entities.Teacher;
import com.example.demo.models.programe.Course;
import jakarta.persistence.*;
import lombok.*;

// Модель TeacherStudentTimeOfTheWeek описує сутність модуля «постійний розклад викладача зі студентом» у базі даних або службовий об’єкт проекту.
// Поля класу читають сервіси, репозиторії та mapper-и під час створення сторінок і збереження змін.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@ToString
@Table(name = "teacherstudenttimeoftheweek")
public class TeacherStudentTimeOfTheWeek {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
    @ManyToOne
    @JoinColumn(name = "time_of_the_week_id")
    private TimeOfTheWeek timeOfTheWeek;
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
    // Отримує через Spring залежності Teacher, Student, TimeOfTheWeek, Course.
    // Ці сервіси й mapper-и потрібні методам класу для роботи з модулем «постійний розклад викладача зі студентом» без ручного створення об’єктів.
    public TeacherStudentTimeOfTheWeek(Teacher teacher, Student student, TimeOfTheWeek timeOfTheWeek, Course course) {
        this.teacher = teacher;
        this.student = student;
        this.timeOfTheWeek = timeOfTheWeek;
        this.course = course;
    }
}
