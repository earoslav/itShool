package com.example.demo.mapper.thirdTable;

import com.example.demo.dto.thirdTable.StudentCourseDTO;
import com.example.demo.mapper.univMapper.UniversalMapper;
import com.example.demo.models.thirdTables.StudentCourse;
import org.springframework.stereotype.Service;

@Service
public class StudentCourseMapper {
    public StudentCourseMapper() {
    }
    public StudentCourse mapStudentCourseDTOToStudentCourse(StudentCourseDTO studentCourseDTO){
        return UniversalMapper.generalMapper(studentCourseDTO, StudentCourse.class);
    }
    public StudentCourseDTO mapStudentCourseToStudentCourseDTO(StudentCourse studentCourse){
        return UniversalMapper.generalMapper(studentCourse, StudentCourseDTO.class);
    }
}
