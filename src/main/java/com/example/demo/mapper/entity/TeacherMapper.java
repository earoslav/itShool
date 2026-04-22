package com.example.demo.mapper.entity;

import com.example.demo.dto.entities.TeacherDTO;
import com.example.demo.mapper.univMapper.UniversalMapper;
import com.example.demo.models.entities.Teacher;
import org.springframework.stereotype.Service;

@Service
public class TeacherMapper {

    public TeacherMapper() {

    }
    public Teacher mapTeacherDTOToTeacher(TeacherDTO teacherDTO){
        return  UniversalMapper.generalMapper(teacherDTO, Teacher.class);
    }
    public TeacherDTO mapTeacherToTeacherDTO(Teacher teacher){
        return  UniversalMapper.generalMapper(teacher, TeacherDTO.class);
    }
}
