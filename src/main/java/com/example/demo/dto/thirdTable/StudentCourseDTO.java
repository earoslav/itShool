package com.example.demo.dto.thirdTable;

import com.example.demo.dto.entities.StudentDTO;
import com.example.demo.dto.programe.CourseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StudentCourseDTO {
    private int id;
    private CourseDTO course;
}
