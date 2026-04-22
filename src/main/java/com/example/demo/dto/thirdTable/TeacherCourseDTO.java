package com.example.demo.dto.thirdTable;

import com.example.demo.dto.entities.TeacherDTO;
import com.example.demo.dto.programe.CourseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeacherCourseDTO {
    private int id;
    private CourseDTO course;
}
