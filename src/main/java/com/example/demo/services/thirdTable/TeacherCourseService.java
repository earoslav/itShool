package com.example.demo.services.thirdTable;

import com.example.demo.dto.thirdTable.TeacherCourseDTO;

import com.example.demo.mapper.thirdTable.TeacherCourseMapper;
import com.example.demo.models.thirdTables.TeacherCourse;
import com.example.demo.repositories.thirdTables.TeacherCourseRepository;
import java.util.List;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

// TeacherCourseService contains business operations for the "teacher courses" module.
// Controllers call this service to avoid direct interaction with repositories, mappers, and scheduling rules.
@Service
public class TeacherCourseService {
    private final TeacherCourseRepository tCRepository;
    private RedisTemplate<String, Object> redisTemplate;
    private ObjectMapper objectMapper;
    private TeacherCourseMapper teacherCourseMapper;
    // Receives TeacherCourseRepository dependency through Spring.
    // These services and mappers are required by the class methods to work with the "teacher courses" module without manual object creation.
    public TeacherCourseService(TeacherCourseRepository tCRepository, @Qualifier("schoolRedisTemplate") RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper, TeacherCourseMapper teacherCourseMapper) {
        this.tCRepository = tCRepository;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.teacherCourseMapper = teacherCourseMapper;
    }
    // Повертає всі зв’язки викладач-курс з репозиторію.
    // Списки, календарі та форми використовують цей метод, коли треба показати весь набір доступних записів.
    public List<TeacherCourse> getAll() {
        return tCRepository.findAll();
    }
    // Finds a single record in the "teacher courses" module by ID.
    // Controllers call this before editing, deleting, or assembling a details page.
    public TeacherCourse getById(Integer id) {
        TeacherCourse obj;
        if(redisTemplate.opsForValue().get("teacherCourseById"+id) == null){
            obj = tCRepository.findById(id).get();
            redisTemplate.opsForValue().set("teacherCourseById"+id, teacherCourseMapper.mapTeacherCourseToTeacherCourseDTO(obj));
        } else {
            obj = teacherCourseMapper.mapTeacherCourseDTOToTeacherCourse(objectMapper.convertValue(redisTemplate.opsForValue().get("teacherCourseById"+id), new TypeReference<TeacherCourseDTO>() {}));
        }
        return obj;
    }
    public void deleteByCourseIdAndTeacherId(int cId, int tId){
        tCRepository.deleteByCourse_IdAndTeacherId(cId, tId);
        // Clear teacher cache because their course list has changed
        redisTemplate.delete("teacherById" + tId);
        // Clear specific teacher-course cache if exists
        redisTemplate.delete("teacherCourseByCourseId" + cId + "AndTeacherId" + tId);
    }
    // Зберігає новий запис у модулі «зв’язок викладача з курсом».
    // Цей метод викликається після того, як контролер зібрав сутність з форми або сервіс згенерував її автоматично.
    public TeacherCourse create(TeacherCourse teacherCourse) {
        TeacherCourse saved = tCRepository.save(teacherCourse);
        if (saved.getTeacher() != null) {
            redisTemplate.delete("teacherById" + saved.getTeacher().getId());
        }
        return saved;
    }

    // Updates an existing record in the "teacher courses" module.
    // First finds the current entity, transfers course and teacher fields, then saves it back to the repository.
    public TeacherCourse update(Integer id, TeacherCourse teacherCourse) {
        TeacherCourse existing = getById(id);
        existing.setCourse(teacherCourse.getCourse());
        existing.setTeacher(teacherCourse.getTeacher());
        existing = tCRepository.save(existing);
        redisTemplate.opsForValue().set("teacherCourseById"+id, teacherCourseMapper.mapTeacherCourseToTeacherCourseDTO(existing));
        return existing;
    }
    // Видаляє запис модуля «зв’язок викладача з курсом» за id.
    // Перед deleteById метод читає сутність, щоб видалення проходило через сервісний шар і падало зрозуміло, якщо id некоректний.
    public void deleteById(Integer id) {
        getById(id);
        tCRepository.deleteById(id);
        redisTemplate.delete("teacherCourseById"+id);
    }
}
