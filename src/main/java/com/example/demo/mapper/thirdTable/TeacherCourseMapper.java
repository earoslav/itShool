package com.example.demo.mapper.thirdTable;

import com.example.demo.dto.thirdTable.StudentCourseDTO;
import com.example.demo.dto.thirdTable.TeacherCourseDTO;
import com.example.demo.mapper.univMapper.UniversalMapper;
import com.example.demo.models.thirdTables.StudentCourse;
import com.example.demo.models.thirdTables.TeacherCourse;
import org.springframework.stereotype.Service;

// Mapper TeacherCourseMapper перетворює об’єкти модуля «курси викладача» між Entity та DTO.
// Так контролери й HTML-шаблони отримують прості об’єкти без зайвої роботи з JPA-зв’язками.
@Service
public class TeacherCourseMapper {
    // Створює порожній TeacherCourseMapper для Spring, JPA або UniversalMapper.
    // Такий конструктор потрібен, щоб фреймворк міг створити об’єкт і потім заповнити його поля.
    public TeacherCourseMapper() {
    }
    // Перетворює TeacherCourseDTO у TeacherCourse через UniversalMapper.
    // Це дозволяє контролерам передавати у шаблон DTO або збирати Entity з даних форми без ручного копіювання полів.
    public TeacherCourse mapTeacherCourseDTOToTeacherCourse(TeacherCourseDTO teacherCourseDTO){
        return UniversalMapper.generalMapper(teacherCourseDTO, TeacherCourse.class);
    }
    // Перетворює TeacherCourse у TeacherCourseDTO через UniversalMapper.
    // Це дозволяє контролерам передавати у шаблон DTO або збирати Entity з даних форми без ручного копіювання полів.
    public TeacherCourseDTO mapTeacherCourseToTeacherCourseDTO(TeacherCourse teacherCourse){
        return UniversalMapper.generalMapper(teacherCourse, TeacherCourseDTO.class);
    }
}
