package com.example.demo.dto.entities;

import com.example.demo.dto.programe.LessonSimpleDTO;
import com.example.demo.dto.thirdTable.EmptyTimesForTeacherDTO;
import com.example.demo.dto.thirdTable.TeacherCourseDTO;
import com.example.demo.dto.thirdTable.TeacherCourseSimpleDTO;
import com.example.demo.dto.thirdTable.TeacherStudentTimeOfTheWeekSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
// The TeacherDTO DTO transfers "teachers" module data between controllers, forms, and templates.
// The class contains no business logic, only fields required by the web layer for displaying or receiving data.
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TeacherDTO {
    private int id;
    private Integer age;
    private String comment;
    private int approved;
    private List<TeacherStudentTimeOfTheWeekSimpleDTO> tswList;
    private List<TeacherCourseSimpleDTO> teacherCourses;
    private List<LessonSimpleDTO> lessons;
    private UserDTO user;
    public float unpaidMoney;
    private String phoneNumber;
    private String tgUsername;
    private String freeTimeIds;
    // Receives Integer, String, int, List<TeacherCourseDTO>, List<EmptyTimesForTeacherDTO>, UserDTO, and float dependencies through Spring.
    // These services and mappers are required by the class methods to work with the "teachers" module without manual object creation.

    public TeacherDTO(Integer age, String comment, int approved, List<TeacherCourseSimpleDTO> teacherCourses, UserDTO user, float unpaidMoney, String phoneNumber, String tgUsername) {
        this.age = age;
        this.comment = comment;
        this.approved = approved;
        this.teacherCourses = teacherCourses;
//        this.emptyTimesForTeachers = emptyTimesForTeachers;
        this.user = user;
        this.unpaidMoney = unpaidMoney;
        this.phoneNumber = phoneNumber;
        this.tgUsername = tgUsername;
    }
    // Receives Integer, String, and int dependencies through Spring.
    // These services and mappers are required by the class methods to work with the "teachers" module without manual object creation.
    public TeacherDTO(Integer age, String comment, int approved, String phoneNumber, String tgUsername) {

        this.age = age;
        this.comment = comment;
        this.approved = approved;
        this.phoneNumber = phoneNumber;
        this.tgUsername = tgUsername;
    }

    public TeacherDTO( Integer age, String comment, int approved, List<TeacherCourseSimpleDTO> teacherCourses, UserDTO user, float unpaidMoney, String phoneNumber, String tgUsername, String freeTimeIds) {

        this.age = age;
        this.comment = comment;
        this.approved = approved;
        this.teacherCourses = teacherCourses;

        this.user = user;
        this.unpaidMoney = unpaidMoney;
        this.phoneNumber = phoneNumber;
        this.tgUsername = tgUsername;
        this.freeTimeIds = freeTimeIds;
    }
}
