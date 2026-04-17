package com.example.demo.dto;

import com.example.demo.models.Course;
import com.example.demo.models.Student;
import com.example.demo.models.Teacher;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class LessonDTO {

private StudentDTO student;


private TeacherDTO teacher;


private CourseDTO course;


private LocalDateTime lessonTime;

private int duration;

private String status;

}