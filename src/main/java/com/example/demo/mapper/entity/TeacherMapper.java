package com.example.demo.mapper.entity;

import com.example.demo.dto.entities.TeacherDTO;
import com.example.demo.mapper.univMapper.UniversalMapper;
import com.example.demo.models.entities.Teacher;
import org.springframework.stereotype.Service;

// TeacherMapper converts "teachers" module objects between Entity and DTO.
// This way, controllers and HTML templates receive simple objects without extra work with JPA relationships.
@Service
public class TeacherMapper {

    // Creates an empty TeacherMapper for Spring, JPA, or UniversalMapper.
    // Such a constructor is needed so the framework can create the object and then fill its fields.
    public TeacherMapper() {

    }
    // Converts TeacherDTO to Teacher using UniversalMapper.
    // This allows controllers to pass a DTO to the template or assemble an Entity from form data without manual field copying.
    public Teacher mapTeacherDTOToTeacher(TeacherDTO teacherDTO){
        return  UniversalMapper.generalMapper(teacherDTO, Teacher.class);
    }
    // Converts Teacher to TeacherDTO using UniversalMapper.
    // This allows controllers to pass a DTO to the template or assemble an Entity from form data without manual field copying.
    public TeacherDTO mapTeacherToTeacherDTO(Teacher teacher){
        return  UniversalMapper.generalMapper(teacher, TeacherDTO.class);
    }
}
