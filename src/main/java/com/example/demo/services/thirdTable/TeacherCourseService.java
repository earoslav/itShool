package com.example.demo.services.thirdTable;

import com.example.demo.models.thirdTables.TeacherCourse;
import com.example.demo.repositories.thirdTables.TeacherCourseRepository;
import java.util.List;
import org.springframework.stereotype.Service;

// Сервіс TeacherCourseService містить бізнес-операції для модуля «курси викладача».
// Контролери звертаються сюди, щоб не працювати напряму з репозиторіями, mapper-ами та правилами розкладу.
@Service
public class TeacherCourseService {
    private final TeacherCourseRepository teacherCourseRepository;
    // Отримує через Spring залежності TeacherCourseRepository.
    // Ці сервіси й mapper-и потрібні методам класу для роботи з модулем «курси викладача» без ручного створення об’єктів.
    public TeacherCourseService(TeacherCourseRepository teacherCourseRepository) {
        this.teacherCourseRepository = teacherCourseRepository;
    }
    // Повертає всі зв’язки викладача з курсами з репозиторію.
    // Списки, календарі та форми використовують цей метод, коли треба показати весь набір доступних записів.
    public List<TeacherCourse> getAll() {
        return teacherCourseRepository.findAll();
    }
    // Знаходить один запис модуля «курси викладача» за id.
    // Контролери викликають його перед редагуванням, видаленням або складанням сторінки з деталями.
    public TeacherCourse getById(Integer id) {
        return teacherCourseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TeacherCourse not found with id: " + id));
    }
    // Зберігає новий запис модуля «курси викладача».
    // Метод викликається після того, як контролер зібрав сутність з форми або сервіс згенерував її автоматично.
    public TeacherCourse create(TeacherCourse teacherCourse) {
        return teacherCourseRepository.save(teacherCourse);
    }

    // Оновлює існуючий запис модуля «курси викладача».
    // Спочатку знаходить поточну сутність, переносить поля course, teacher і зберігає її назад у репозиторій.
    public TeacherCourse update(Integer id, TeacherCourse teacherCourse) {
        TeacherCourse existing = getById(id);
        existing.setCourse(teacherCourse.getCourse());
        existing.setTeacher(teacherCourse.getTeacher());
        return teacherCourseRepository.save(existing);
    }
    // Видаляє запис модуля «курси викладача» за id.
    // Перед deleteById метод читає сутність, щоб видалення проходило через сервісний шар і падало зрозуміло, якщо id некоректний.
    public void deleteById(Integer id) {
        getById(id);
        teacherCourseRepository.deleteById(id);
    }
}
