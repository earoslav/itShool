package com.example.demo.mapper;

import com.example.demo.dto.StudentDTO;
import com.example.demo.mapper.univMapper.UniversalMapper;
import com.example.demo.models.Student;
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
