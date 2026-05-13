package com.example.demo.dto.entities;

import com.example.demo.dto.thirdTable.EmptyTimesForTeacherDTO;
import com.example.demo.dto.thirdTable.TeacherCourseDTO;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
// DTO TeacherDTO переносить дані модуля «викладачі» між контролерами, формами та шаблонами.
// Клас не містить бізнес-логіки, а лише поля, які потрібні веб-рівню для показу або прийому даних.
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TeacherDTO {
    private int id;
    private Integer age;
    private String comment;
    private int approved;
    private List<TeacherCourseDTO> teacherCourses;
    private List<EmptyTimesForTeacherDTO> emptyTimesForTeachers;
    private UserDTO user;
    public float unpaidMoney;
    private String phoneNumber;
    private String tgUsername;
    private String freeTimeIds;
    // Отримує через Spring залежності Integer, String, int, List<TeacherCourseDTO>, List<EmptyTimesForTeacherDTO>, UserDTO, float.
    // Ці сервіси й mapper-и потрібні методам класу для роботи з модулем «викладачі» без ручного створення об’єктів.

    public TeacherDTO(Integer age, String comment, int approved, List<TeacherCourseDTO> teacherCourses, List<EmptyTimesForTeacherDTO> emptyTimesForTeachers, UserDTO user, float unpaidMoney, String phoneNumber, String tgUsername) {
        this.age = age;
        this.comment = comment;
        this.approved = approved;
        this.teacherCourses = teacherCourses;
        this.emptyTimesForTeachers = emptyTimesForTeachers;
        this.user = user;
        this.unpaidMoney = unpaidMoney;
        this.phoneNumber = phoneNumber;
        this.tgUsername = tgUsername;
    }
    // Отримує через Spring залежності Integer, String, int.
    // Ці сервіси й mapper-и потрібні методам класу для роботи з модулем «викладачі» без ручного створення об’єктів.
    public TeacherDTO(Integer age, String comment, int approved, String phoneNumber, String tgUsername) {

        this.age = age;
        this.comment = comment;
        this.approved = approved;
        this.phoneNumber = phoneNumber;
        this.tgUsername = tgUsername;
    }

    public TeacherDTO( Integer age, String comment, int approved, List<TeacherCourseDTO> teacherCourses, List<EmptyTimesForTeacherDTO> emptyTimesForTeachers, UserDTO user, float unpaidMoney, String phoneNumber, String tgUsername, String freeTimeIds) {

        this.age = age;
        this.comment = comment;
        this.approved = approved;
        this.teacherCourses = teacherCourses;
        this.emptyTimesForTeachers = emptyTimesForTeachers;
        this.user = user;
        this.unpaidMoney = unpaidMoney;
        this.phoneNumber = phoneNumber;
        this.tgUsername = tgUsername;
        this.freeTimeIds = freeTimeIds;
    }
}
