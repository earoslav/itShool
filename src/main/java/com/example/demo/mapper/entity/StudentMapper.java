package com.example.demo.mapper.entity;

import com.example.demo.dto.entities.StudentDTO;
import com.example.demo.mapper.univMapper.UniversalMapper;
import com.example.demo.models.entities.Student;
import org.springframework.stereotype.Service;

// Mapper StudentMapper перетворює об’єкти модуля «студенти» між Entity та DTO.
// Так контролери й HTML-шаблони отримують прості об’єкти без зайвої роботи з JPA-зв’язками.
@Service
public class StudentMapper {

    // Створює порожній StudentMapper для Spring, JPA або UniversalMapper.
    // Такий конструктор потрібен, щоб фреймворк міг створити об’єкт і потім заповнити його поля.
    public StudentMapper() {

    }
    // Перетворює StudentDTO у Student через UniversalMapper.
    // Це дозволяє контролерам передавати у шаблон DTO або збирати Entity з даних форми без ручного копіювання полів.
    public Student mapStudentDTOToStudent(StudentDTO studentDTO) {
        return UniversalMapper.generalMapper(studentDTO, Student.class);
    }
    // Перетворює Student у StudentDTO через UniversalMapper.
    // Це дозволяє контролерам передавати у шаблон DTO або збирати Entity з даних форми без ручного копіювання полів.
    public StudentDTO mapStudentToStudentDTO(Student student) {
        return UniversalMapper.generalMapper(student, StudentDTO.class);
    }
}
