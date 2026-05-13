package com.example.demo.models.programe;

import com.example.demo.models.thirdTables.StudentCourse;
import com.example.demo.models.thirdTables.TeacherCourse;
import com.example.demo.models.thirdTables.TeacherStudentTimeOfTheWeek;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "course")

// Модель Course описує сутність модуля «курси» у базі даних або службовий об’єкт проекту.
// Поля класу читають сервіси, репозиторії та mapper-и під час створення сторінок і збереження змін.
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "cost_per_lesson")
    private Integer costPerLesson;
    @Column(name = "teacher_share_per_lesson")
    private Integer teacherShare;

    @Column(name = "description", length = 1000)
    private String description;
    @Column(name = "program")
    private String program;

    @OneToMany(mappedBy = "course")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<TeacherCourse> teacherCourses;
    @OneToMany(mappedBy = "course")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<StudentCourse> studentCourses;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "course")
    private List<TeacherStudentTimeOfTheWeek> tswList;
    // Отримує через Spring залежності String, Integer, List<TeacherCourse>, List<StudentCourse>, List<TeacherStudentTimeOfTheWeek>.
    // Ці сервіси й mapper-и потрібні методам класу для роботи з модулем «курси» без ручного створення об’єктів.
    public Course(String name, Integer costPerLesson, Integer teacherShare, String description, List<TeacherCourse> teacherCourses, List<StudentCourse> studentCourses, List<TeacherStudentTimeOfTheWeek> tswList) {
        this.name = name;
        this.costPerLesson = costPerLesson;
        this.teacherShare = teacherShare;
        this.description = description;
        this.teacherCourses = teacherCourses;
        this.studentCourses = studentCourses;
        this.tswList = tswList;
    }
    // Отримує через Spring залежності String, Integer.
    // Ці сервіси й mapper-и потрібні методам класу для роботи з модулем «курси» без ручного створення об’єктів.
    public Course(String name, Integer costPerLesson, Integer teacherShare, String description) {
        this.name = name;
        this.costPerLesson = costPerLesson;
        this.teacherShare = teacherShare;
        this.description = description;
    }

    public Course(int id, String name, Integer costPerLesson, Integer teacherShare, String description, String program) {
        this.id = id;
        this.name = name;
        this.costPerLesson = costPerLesson;
        this.teacherShare = teacherShare;
        this.description = description;
        this.program = program;
    }
}
