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
    private String name;
    private Integer age;
    private String comment;
    private boolean approved;
    private List<TeacherCourseDTO> teacherCourses;
    private List<EmptyTimesForTeacherDTO> emptyTimesForTeachers;
    private String email;
    public float unpaidMoney;
    private String phoneNumber;
    private String tgUsername;

}
