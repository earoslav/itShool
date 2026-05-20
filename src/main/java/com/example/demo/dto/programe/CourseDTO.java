package com.example.demo.dto.programe;

import com.example.demo.dto.thirdTable.TeacherCourseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
// The CourseDTO DTO transfers "courses" module data between controllers, forms, and templates.
// The class contains no business logic, only fields required by the web layer for displaying or receiving data.
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CourseDTO {
    private int id;
    private String name;
    private Integer costPerLesson;
    private Integer teacherShare;
    private String description;
    private String program;
    private List<LessonSimpleDTO> lessons;
}
