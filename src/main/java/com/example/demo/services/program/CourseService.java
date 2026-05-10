package com.example.demo.services.program;

import com.example.demo.models.programe.Course;
import com.example.demo.repositories.program.CourseRepository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

// Сервіс CourseService містить бізнес-операції для модуля «курси».
// Контролери звертаються сюди, щоб не працювати напряму з репозиторіями, mapper-ами та правилами розкладу.
@Service
public class CourseService {
    private final CourseRepository courseRepository;
    // Отримує через Spring залежності CourseRepository.
    // Ці сервіси й mapper-и потрібні методам класу для роботи з модулем «курси» без ручного створення об’єктів.
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }
    // Повертає всі курси з репозиторію.
    // Списки, календарі та форми використовують цей метод, коли треба показати весь набір доступних записів.
    public List<Course> getAll() {
        return courseRepository.findAll();
    }
    // Знаходить один запис модуля «курси» за id.
    // Контролери викликають його перед редагуванням, видаленням або складанням сторінки з деталями.
    public Course getById(Integer id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
    }
    // Перетворює список id з форми на об’єкти Course.
    // Коли id не передані, повертає всі курси, щоб форма створення або редагування могла показати повний список.
    public List<Course> getCoursesThroughIds(List<Integer> ids){
        if(ids!=null && !ids.isEmpty()){
            List<Course> courses = new ArrayList<>();
            ids.stream().forEach(course-> courses.add(getById(course)));
            return courses;
        }else{
            List<Course> courses = getAll();
            return courses;
        }
    }
    // Зберігає новий запис модуля «курси».
    // Метод викликається після того, як контролер зібрав сутність з форми або сервіс згенерував її автоматично.
    public Course create(Course course) {
        return courseRepository.save(course);
    }

    // Оновлює існуючий запис модуля «курси».
    // Спочатку знаходить поточну сутність, переносить поля name, costPerLesson, description і зберігає її назад у репозиторій.
    public Course update(Integer id, Course course) {
        Course existing = getById(id);
        existing.setName(course.getName());
        existing.setCostPerLesson(course.getCostPerLesson());
        existing.setDescription(course.getDescription());
        return courseRepository.save(existing);
    }
    // Видаляє запис модуля «курси» за id.
    // Перед deleteById метод читає сутність, щоб видалення проходило через сервісний шар і падало зрозуміло, якщо id некоректний.
    public void deleteById(Integer id) {
        getById(id);
        courseRepository.deleteById(id);
    }
}
