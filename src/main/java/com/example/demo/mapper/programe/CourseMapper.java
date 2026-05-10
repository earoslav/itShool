package com.example.demo.mapper.programe;

import com.example.demo.dto.programe.CourseDTO;
import com.example.demo.mapper.univMapper.UniversalMapper;
import com.example.demo.models.programe.Course;
import org.springframework.stereotype.Service;

// Mapper CourseMapper перетворює об’єкти модуля «курси» між Entity та DTO.
// Так контролери й HTML-шаблони отримують прості об’єкти без зайвої роботи з JPA-зв’язками.
@Service
public class CourseMapper {

    // Створює порожній CourseMapper для Spring, JPA або UniversalMapper.
    // Такий конструктор потрібен, щоб фреймворк міг створити об’єкт і потім заповнити його поля.
    public CourseMapper() {

    }
    // Перетворює CourseDTO у Course через UniversalMapper.
    // Це дозволяє контролерам передавати у шаблон DTO або збирати Entity з даних форми без ручного копіювання полів.
    public Course mapCourseDTOToCourse(CourseDTO courseDTO) {
        return UniversalMapper.generalMapper(courseDTO, Course.class);
    }
    // Перетворює Course у CourseDTO через UniversalMapper.
    // Це дозволяє контролерам передавати у шаблон DTO або збирати Entity з даних форми без ручного копіювання полів.
    public CourseDTO mapCourseToCourseDTO(Course course) {
        return UniversalMapper.generalMapper(course, CourseDTO.class);
    }
}
