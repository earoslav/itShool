package com.example.demo.models.thirdTables;

import com.example.demo.models.programe.Course;
import com.example.demo.models.entities.Student;
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

// Модель StudentCourse описує сутність модуля «курси студента» у базі даних або службовий об’єкт проекту.
// Поля класу читають сервіси, репозиторії та mapper-и під час створення сторінок і збереження змін.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "student_course")
public class StudentCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
    // Отримує через Spring залежності Course, Student.
    // Ці сервіси й mapper-и потрібні методам класу для роботи з модулем «курси студента» без ручного створення об’єктів.
    public StudentCourse(Course course, Student student) {
        this.course = course;
        this.student = student;
    }
}
