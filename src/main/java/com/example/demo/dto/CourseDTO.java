package com.example.demo.dto;

import com.example.demo.models.StudentCourse;
import com.example.demo.models.TeacherCourse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CourseDTO {
    private String name;

    private Integer costPerLesson;

    private String description;


    private List<TeacherCourseDTO> teacherCourses;

}
