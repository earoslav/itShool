package com.example.demo.mapper.entity;

import com.example.demo.dto.entities.StudentDTO;
import com.example.demo.mapper.univMapper.UniversalMapper;
import com.example.demo.models.entities.Student;
import org.springframework.stereotype.Service;

@Service
public class StudentMapper {

    public StudentMapper() {

    }

    public Student mapStudentDTOToStudent(StudentDTO studentDTO) {
        return UniversalMapper.generalMapper(studentDTO, Student.class);
    }

    public StudentDTO mapStudentToStudentDTO(Student student) {
        return UniversalMapper.generalMapper(student, StudentDTO.class);
    }
}
