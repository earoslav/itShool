package com.example.demo.mapper.programe;

import com.example.demo.dto.programe.LessonDTO;
import com.example.demo.dto.adminLessons.LessonAdminLessonsDTO;
import com.example.demo.mapper.univMapper.UniversalMapper;
import com.example.demo.models.programe.Lesson;
import org.springframework.stereotype.Service;

@Service
public class LessonMapper {

    public LessonMapper() {

    }

    public Lesson mapLessonDTOToLesson(LessonDTO lessonDTO) {
        return UniversalMapper.generalMapper(lessonDTO, Lesson.class);
    }

    public LessonDTO mapLessonToLessonDTO(Lesson lesson) {
        return UniversalMapper.generalMapper(lesson, LessonDTO.class);
    }
    public LessonAdminLessonsDTO mapLessonToLessonAdminLessonsDTO(Lesson lesson){
        return UniversalMapper.generalMapper(lesson, LessonAdminLessonsDTO.class);
    }
    public Lesson mapLessonAdminLessonsDTOToLesson(LessonAdminLessonsDTO lessonAdminLessonsDTO){
        return UniversalMapper.generalMapper(lessonAdminLessonsDTO, Lesson.class);
    }
}
