package com.example.demo.mapper.thirdTable;

import com.example.demo.dto.thirdTable.StudentCourseDTO;
import com.example.demo.dto.thirdTable.TeacherCourseDTO;
import com.example.demo.mapper.univMapper.UniversalMapper;
import com.example.demo.models.thirdTables.StudentCourse;
import com.example.demo.models.thirdTables.TeacherCourse;
import org.springframework.stereotype.Service;

@Service
public class TeacherCourseMapper {
    public TeacherCourseMapper() {
    }
    public TeacherCourse mapTeacherCourseDTOToTeacherCourse(TeacherCourseDTO teacherCourseDTO){
        return UniversalMapper.generalMapper(teacherCourseDTO, TeacherCourse.class);
    }
    public TeacherCourseDTO mapTeacherCourseToTeacherCourseDTO(TeacherCourse teacherCourse){
        return UniversalMapper.generalMapper(teacherCourse, TeacherCourseDTO.class);
    }
}
