package com.example.demo.mapper.entity;

import com.example.demo.dto.entities.StudentDTO;
import com.example.demo.mapper.univMapper.UniversalMapper;
import com.example.demo.models.entities.Student;
import org.springframework.stereotype.Service;

// StudentMapper converts "students" module objects between Entity and DTO.
// This way, controllers and HTML templates receive simple objects without extra work with JPA relationships.
@Service
public class StudentMapper {

    // Creates an empty StudentMapper for Spring, JPA, or UniversalMapper.
    // Such a constructor is needed so the framework can create the object and then fill its fields.
    public StudentMapper() {

    }
    // Converts StudentDTO to Student using UniversalMapper.
    // This allows controllers to pass a DTO to the template or assemble an Entity from form data without manual field copying.
    public Student mapStudentDTOToStudent(StudentDTO studentDTO) {
        return UniversalMapper.generalMapper(studentDTO, Student.class);
    }
    // Converts Student to StudentDTO using UniversalMapper.
    // This allows controllers to pass a DTO to the template or assemble an Entity from form data without manual field copying.
    public StudentDTO mapStudentToStudentDTO(Student student) {
        return UniversalMapper.generalMapper(student, StudentDTO.class);
    }
}
