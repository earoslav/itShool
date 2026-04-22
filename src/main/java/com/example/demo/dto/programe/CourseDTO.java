package com.example.demo.dto.programe;

import com.example.demo.dto.thirdTable.TeacherCourseDTO;
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
    private int id;
    private String name;
    private Integer costPerLesson;
    private Integer teacherShare;
    private String description;

}
