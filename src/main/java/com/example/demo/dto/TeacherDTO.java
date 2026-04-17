package com.example.demo.dto;

import com.example.demo.models.Comment;
import com.example.demo.models.EmptyTimesForTeacher;
import com.example.demo.models.Lesson;
import com.example.demo.models.TeacherCourse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
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

    private String name;


    private Integer age;



    private String comment;

    private boolean approved;

    private List<TeacherCourseDTO> teacherCourses;

    private List<EmptyTimesForTeacherDTO> emptyTimesForTeachers;

}
