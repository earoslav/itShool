package com.example.demo.mapper.programe;

import com.example.demo.dto.programe.LessonDTO;
import com.example.demo.dto.adminLessons.LessonAdminLessonsDTO;
import com.example.demo.mapper.univMapper.UniversalMapper;
import com.example.demo.models.programe.Lesson;
import org.springframework.stereotype.Service;

// LessonMapper converts "lessons" module objects between Entity and DTO.
// This way, controllers and HTML templates receive simple objects without extra work with JPA relationships.
@Service
public class LessonMapper {

    // Creates an empty LessonMapper for Spring, JPA, or UniversalMapper.
    // Such a constructor is needed so the framework can create the object and then fill its fields.
    public LessonMapper() {

    }
    // Converts LessonDTO to Lesson using UniversalMapper.
    // This allows controllers to pass a DTO to the template or assemble an Entity from form data without manual field copying.
    public Lesson mapLessonDTOToLesson(LessonDTO lessonDTO) {
        return UniversalMapper.generalMapper(lessonDTO, Lesson.class);
    }
    // Converts Lesson to LessonDTO using UniversalMapper.
    // This allows controllers to pass a DTO to the template or assemble an Entity from form data without manual field copying.
    public LessonDTO mapLessonToLessonDTO(Lesson lesson) {
        return UniversalMapper.generalMapper(lesson, LessonDTO.class);
    }
    // Converts Lesson to LessonAdminLessonsDTO using UniversalMapper.
    // This allows controllers to pass a DTO to the template or assemble an Entity from form data without manual field copying.
    public LessonAdminLessonsDTO mapLessonToLessonAdminLessonsDTO(Lesson lesson){
        return UniversalMapper.generalMapper(lesson, LessonAdminLessonsDTO.class);
    }
    // Converts LessonAdminLessonsDTO to Lesson using UniversalMapper.
    // This allows controllers to pass a DTO to the template or assemble an Entity from form data without manual field copying.
    public Lesson mapLessonAdminLessonsDTOToLesson(LessonAdminLessonsDTO lessonAdminLessonsDTO){
        return UniversalMapper.generalMapper(lessonAdminLessonsDTO, Lesson.class);
    }
}
