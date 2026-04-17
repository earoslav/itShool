package com.example.demo.mapper;

import com.example.demo.dto.CourseDTO;
import com.example.demo.mapper.univMapper.UniversalMapper;
import com.example.demo.models.Course;
import org.springframework.stereotype.Service;

@Service
public class CourseMapper {

    public CourseMapper() {

    }

    public Course mapCourseDTOToCourse(CourseDTO courseDTO) {
        return UniversalMapper.generalMapper(courseDTO, Course.class);
    }

    public CourseDTO mapCourseToCourseDTO(Course course) {
        return UniversalMapper.generalMapper(course, CourseDTO.class);
    }
}
