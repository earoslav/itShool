package com.example.demo.mapper.programe;

import com.example.demo.dto.programe.CourseDTO;
import com.example.demo.mapper.univMapper.UniversalMapper;
import com.example.demo.models.programe.Course;
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
