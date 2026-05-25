package com.example.demo.services.thirdTable;

import com.example.demo.models.thirdTables.TeacherCourse;
import com.example.demo.repositories.thirdTables.TeacherCourseRepository;
import java.util.List;
import org.springframework.stereotype.Service;

// TeacherCourseService contains business operations for the "teacher courses" module.
// Controllers call this service to avoid direct interaction with repositories, mappers, and scheduling rules.
@Service
public class TeacherCourseService {
    private final TeacherCourseRepository tCRepository;
    // Receives TeacherCourseRepository dependency through Spring.
    // These services and mappers are required by the class methods to work with the "teacher courses" module without manual object creation.
    public TeacherCourseService(TeacherCourseRepository tCRepository) {
        this.tCRepository = tCRepository;
    }
    // Повертає всі зв’язки викладач-курс з репозиторію.
    // Списки, календарі та форми використовують цей метод, коли треба показати весь набір доступних записів.
    public List<TeacherCourse> getAll() {
        return tCRepository.findAll();
    }
    // Finds a single record in the "teacher courses" module by ID.
    // Controllers call this before editing, deleting, or assembling a details page.
    public TeacherCourse getById(Integer id) {
        return tCRepository.findById(id).get();
    }
    public void deleteByCourseIdAndTeacherId(int cId, int tId){
        tCRepository.deleteByCourse_IdAndTeacherId(cId, tId);
    }
    // Зберігає новий запис у модулі «зв’язок викладача з курсом».
    // Цей метод викликається після того, як контролер зібрав сутність з форми або сервіс згенерував її автоматично.
    public TeacherCourse create(TeacherCourse teacherCourse) {
        return tCRepository.save(teacherCourse);
    }

    // Updates an existing record in the "teacher courses" module.
    // First finds the current entity, transfers course and teacher fields, then saves it back to the repository.
    public TeacherCourse update(Integer id, TeacherCourse teacherCourse) {
        TeacherCourse existing = getById(id);
        existing.setCourse(teacherCourse.getCourse());
        existing.setTeacher(teacherCourse.getTeacher());
        return tCRepository.save(existing);
    }
    // Видаляє запис модуля «зв’язок викладача з курсом» за id.
    // Перед deleteById метод читає сутність, щоб видалення проходило через сервісний шар і падало зрозуміло, якщо id некоректний.
    public void deleteById(Integer id) {
        getById(id);
        tCRepository.deleteById(id);
    }
}
