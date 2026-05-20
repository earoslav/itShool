package com.example.demo.mapper.programe;

import com.example.demo.dto.programe.CourseDTO;
import com.example.demo.mapper.univMapper.UniversalMapper;
import com.example.demo.models.programe.Course;
import org.springframework.stereotype.Service;

// CourseMapper converts "courses" module objects between Entity and DTO.
// This way, controllers and HTML templates receive simple objects without extra work with JPA relationships.
@Service
public class CourseMapper {

    // Creates an empty CourseMapper for Spring, JPA, or UniversalMapper.
    // Such a constructor is needed so the framework can create the object and then fill its fields.
    public CourseMapper() {

    }
    // Converts CourseDTO to Course using UniversalMapper.
    // This allows controllers to pass a DTO to the template or assemble an Entity from form data without manual field copying.
    public Course mapCourseDTOToCourse(CourseDTO courseDTO) {
        return UniversalMapper.generalMapper(courseDTO, Course.class);
    }
    // Converts Course to CourseDTO using UniversalMapper.
    // This allows controllers to pass a DTO to the template or assemble an Entity from form data without manual field copying.
    public CourseDTO mapCourseToCourseDTO(Course course) {
        return UniversalMapper.generalMapper(course, CourseDTO.class);
    }
}
