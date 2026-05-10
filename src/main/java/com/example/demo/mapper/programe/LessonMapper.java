package com.example.demo.mapper.programe;

import com.example.demo.dto.programe.LessonDTO;
import com.example.demo.dto.adminLessons.LessonAdminLessonsDTO;
import com.example.demo.mapper.univMapper.UniversalMapper;
import com.example.demo.models.programe.Lesson;
import org.springframework.stereotype.Service;

// Mapper LessonMapper перетворює об’єкти модуля «уроки» між Entity та DTO.
// Так контролери й HTML-шаблони отримують прості об’єкти без зайвої роботи з JPA-зв’язками.
@Service
public class LessonMapper {

    // Створює порожній LessonMapper для Spring, JPA або UniversalMapper.
    // Такий конструктор потрібен, щоб фреймворк міг створити об’єкт і потім заповнити його поля.
    public LessonMapper() {

    }
    // Перетворює LessonDTO у Lesson через UniversalMapper.
    // Це дозволяє контролерам передавати у шаблон DTO або збирати Entity з даних форми без ручного копіювання полів.
    public Lesson mapLessonDTOToLesson(LessonDTO lessonDTO) {
        return UniversalMapper.generalMapper(lessonDTO, Lesson.class);
    }
    // Перетворює Lesson у LessonDTO через UniversalMapper.
    // Це дозволяє контролерам передавати у шаблон DTO або збирати Entity з даних форми без ручного копіювання полів.
    public LessonDTO mapLessonToLessonDTO(Lesson lesson) {
        return UniversalMapper.generalMapper(lesson, LessonDTO.class);
    }
    // Перетворює Lesson у LessonAdminLessonsDTO через UniversalMapper.
    // Це дозволяє контролерам передавати у шаблон DTO або збирати Entity з даних форми без ручного копіювання полів.
    public LessonAdminLessonsDTO mapLessonToLessonAdminLessonsDTO(Lesson lesson){
        return UniversalMapper.generalMapper(lesson, LessonAdminLessonsDTO.class);
    }
    // Перетворює LessonAdminLessonsDTO у Lesson через UniversalMapper.
    // Це дозволяє контролерам передавати у шаблон DTO або збирати Entity з даних форми без ручного копіювання полів.
    public Lesson mapLessonAdminLessonsDTOToLesson(LessonAdminLessonsDTO lessonAdminLessonsDTO){
        return UniversalMapper.generalMapper(lessonAdminLessonsDTO, Lesson.class);
    }
}
