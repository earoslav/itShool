package com.example.demo.dto;

import com.example.demo.models.Course;
import com.example.demo.models.Student;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StudentCourseDTO {
    private CourseDTO course;


    private StudentDTO student;
}
