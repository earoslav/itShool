package com.example.demo.mapper.thirdTable;

import com.example.demo.dto.thirdTable.StudentCourseDTO;
import com.example.demo.mapper.univMapper.UniversalMapper;
import com.example.demo.models.thirdTables.StudentCourse;
import org.springframework.stereotype.Service;

// Mapper StudentCourseMapper перетворює об’єкти модуля «курси студента» між Entity та DTO.
// Так контролери й HTML-шаблони отримують прості об’єкти без зайвої роботи з JPA-зв’язками.
@Service
public class StudentCourseMapper {
    // Створює порожній StudentCourseMapper для Spring, JPA або UniversalMapper.
    // Такий конструктор потрібен, щоб фреймворк міг створити об’єкт і потім заповнити його поля.
    public StudentCourseMapper() {
    }
    // Перетворює StudentCourseDTO у StudentCourse через UniversalMapper.
    // Це дозволяє контролерам передавати у шаблон DTO або збирати Entity з даних форми без ручного копіювання полів.
    public StudentCourse mapStudentCourseDTOToStudentCourse(StudentCourseDTO studentCourseDTO){
        return UniversalMapper.generalMapper(studentCourseDTO, StudentCourse.class);
    }
    // Перетворює StudentCourse у StudentCourseDTO через UniversalMapper.
    // Це дозволяє контролерам передавати у шаблон DTO або збирати Entity з даних форми без ручного копіювання полів.
    public StudentCourseDTO mapStudentCourseToStudentCourseDTO(StudentCourse studentCourse){
        return UniversalMapper.generalMapper(studentCourse, StudentCourseDTO.class);
    }
}
