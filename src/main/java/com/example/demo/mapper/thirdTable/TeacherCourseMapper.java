package com.example.demo.mapper.thirdTable;

import com.example.demo.dto.thirdTable.TeacherCourseDTO;
import com.example.demo.mapper.univMapper.UniversalMapper;
import com.example.demo.models.thirdTables.TeacherCourse;
import org.springframework.stereotype.Service;

// TeacherCourseMapper converts "teacher courses" module objects between Entity and DTO.
// This way, controllers and HTML templates receive simple objects without extra work with JPA relationships.
@Service
public class TeacherCourseMapper {
    // Creates an empty TeacherCourseMapper for Spring, JPA, or UniversalMapper.
    // Such a constructor is needed so the framework can create the object and then fill its fields.
    public TeacherCourseMapper() {
    }
    // Converts TeacherCourseDTO to TeacherCourse using UniversalMapper.
    // This allows controllers to pass a DTO to the template or assemble an Entity from form data without manual field copying.
    public TeacherCourse mapTeacherCourseDTOToTeacherCourse(TeacherCourseDTO teacherCourseDTO){
        return UniversalMapper.generalMapper(teacherCourseDTO, TeacherCourse.class);
    }
    // Converts TeacherCourse to TeacherCourseDTO using UniversalMapper.
    // This allows controllers to pass a DTO to the template or assemble an Entity from form data without manual field copying.
    public TeacherCourseDTO mapTeacherCourseToTeacherCourseDTO(TeacherCourse teacherCourse){
        return UniversalMapper.generalMapper(teacherCourse, TeacherCourseDTO.class);
    }
}
