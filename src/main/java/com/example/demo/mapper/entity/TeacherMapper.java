package com.example.demo.mapper.entity;

import com.example.demo.dto.entities.TeacherDTO;
import com.example.demo.mapper.univMapper.UniversalMapper;
import com.example.demo.models.entities.Teacher;
import org.springframework.stereotype.Service;

// Mapper TeacherMapper перетворює об’єкти модуля «викладачі» між Entity та DTO.
// Так контролери й HTML-шаблони отримують прості об’єкти без зайвої роботи з JPA-зв’язками.
@Service
public class TeacherMapper {

    // Створює порожній TeacherMapper для Spring, JPA або UniversalMapper.
    // Такий конструктор потрібен, щоб фреймворк міг створити об’єкт і потім заповнити його поля.
    public TeacherMapper() {

    }
    // Перетворює TeacherDTO у Teacher через UniversalMapper.
    // Це дозволяє контролерам передавати у шаблон DTO або збирати Entity з даних форми без ручного копіювання полів.
    public Teacher mapTeacherDTOToTeacher(TeacherDTO teacherDTO){
        return  UniversalMapper.generalMapper(teacherDTO, Teacher.class);
    }
    // Перетворює Teacher у TeacherDTO через UniversalMapper.
    // Це дозволяє контролерам передавати у шаблон DTO або збирати Entity з даних форми без ручного копіювання полів.
    public TeacherDTO mapTeacherToTeacherDTO(Teacher teacher){
        return  UniversalMapper.generalMapper(teacher, TeacherDTO.class);
    }
}
