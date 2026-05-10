package com.example.demo.models.thirdTables;

import com.example.demo.models.programe.Course;
import com.example.demo.models.entities.Teacher;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Модель TeacherCourse описує сутність модуля «курси викладача» у базі даних або службовий об’єкт проекту.
// Поля класу читають сервіси, репозиторії та mapper-и під час створення сторінок і збереження змін.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "teacher_course")
public class TeacherCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
    // Отримує через Spring залежності Course, Teacher.
    // Ці сервіси й mapper-и потрібні методам класу для роботи з модулем «курси викладача» без ручного створення об’єктів.
    public TeacherCourse(Course course, Teacher teacher) {
        this.course = course;
        this.teacher = teacher;
    }
}
