package com.example.demo.dto.programe;

import com.example.demo.dto.entities.StudentDTO;
import com.example.demo.dto.entities.TeacherDTO;
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
    private int id;
    private StudentDTO student;
    private TeacherDTO teacher;
    private CourseDTO course;
    private LocalDateTime lessonTime;
    private float duration;
    private String status;

}