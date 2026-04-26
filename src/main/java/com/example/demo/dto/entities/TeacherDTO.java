package com.example.demo.dto.entities;

import com.example.demo.dto.thirdTable.EmptyTimesForTeacherDTO;
import com.example.demo.dto.thirdTable.TeacherCourseDTO;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
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

    public TeacherDTO(Integer age, String comment, int approved, String phoneNumber, String tgUsername) {

        this.age = age;
        this.comment = comment;
        this.approved = approved;
        this.phoneNumber = phoneNumber;
        this.tgUsername = tgUsername;
    }
}
