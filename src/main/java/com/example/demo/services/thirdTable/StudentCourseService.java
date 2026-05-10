package com.example.demo.services.thirdTable;

import com.example.demo.models.entities.Student;
import com.example.demo.models.thirdTables.StudentCourse;
import com.example.demo.repositories.thirdTables.StudentCourseRepository;
import java.util.List;
import org.springframework.stereotype.Service;

// Сервіс StudentCourseService містить бізнес-операції для модуля «курси студента».
// Контролери звертаються сюди, щоб не працювати напряму з репозиторіями, mapper-ами та правилами розкладу.
@Service
public class StudentCourseService {
    private final StudentCourseRepository studentCourseRepository;
    // Отримує через Spring залежності StudentCourseRepository.
    // Ці сервіси й mapper-и потрібні методам класу для роботи з модулем «курси студента» без ручного створення об’єктів.
    public StudentCourseService(StudentCourseRepository studentCourseRepository) {
        this.studentCourseRepository = studentCourseRepository;
    }
    // Повертає всі зв’язки студента з курсами з репозиторію.
    // Списки, календарі та форми використовують цей метод, коли треба показати весь набір доступних записів.
    public List<StudentCourse> getAll() {
        return studentCourseRepository.findAll();
    }
    // Знаходить один запис модуля «курси студента» за id.
    // Контролери викликають його перед редагуванням, видаленням або складанням сторінки з деталями.
    public StudentCourse getById(Integer id) {
        return studentCourseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("StudentCourse not found with id: " + id));
    }
    // Видаляє всі зв’язки студента з курсами.
    // Це потрібно під час видалення або повного оновлення профілю студента, щоб прибрати старі навчальні прив’язки.
    public void deleteAllCoursesByStudent(Student student){
        studentCourseRepository.deleteAllByStudent(student);
    }
    // Зберігає новий запис модуля «курси студента».
    // Метод викликається після того, як контролер зібрав сутність з форми або сервіс згенерував її автоматично.
    public StudentCourse create(StudentCourse studentCourse) {
        return studentCourseRepository.save(studentCourse);
    }

    // Оновлює існуючий запис модуля «курси студента».
    // Спочатку знаходить поточну сутність, переносить поля course, student і зберігає її назад у репозиторій.
    public StudentCourse update(Integer id, StudentCourse studentCourse) {
        StudentCourse existing = getById(id);
        existing.setCourse(studentCourse.getCourse());
        existing.setStudent(studentCourse.getStudent());
        return studentCourseRepository.save(existing);
    }
    // Видаляє запис модуля «курси студента» за id.
    // Перед deleteById метод читає сутність, щоб видалення проходило через сервісний шар і падало зрозуміло, якщо id некоректний.
    public void deleteById(Integer id) {
        getById(id);
        studentCourseRepository.deleteById(id);
    }
}
