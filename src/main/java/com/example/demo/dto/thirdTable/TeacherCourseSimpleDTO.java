package com.example.demo.dto.thirdTable;

import com.example.demo.dto.programe.CourseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// This class separates the external representation of information from JPA entities.
// It contains only the fields required for display or form processing.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeacherCourseSimpleDTO {
    private int id;
    private CourseDTO course;
}
