package com.example.demo.services.program;

import com.example.demo.models.programe.Course;
import com.example.demo.repositories.program.CourseRepository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

// Сервіс CourseService містить бізнес-операції для модуля «курси».
// Контролери звертаються сюди, щоб не працювати напряму з репозиторіями, mapper-ами та правилами розкладу.
@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private RedisTemplate redis;
    // Отримує через Spring залежності CourseRepository.
    // Ці сервіси й mapper-и потрібні методам класу для роботи з модулем «курси» без ручного створення об’єктів.
    public CourseService(CourseRepository courseRepository, @Qualifier("redisTemplate") RedisTemplate redis) {
        this.courseRepository = courseRepository;
        this.redis = redis;
    }
    // Повертає всі курси з репозиторію.
    // Списки, календарі та форми використовують цей метод, коли треба показати весь набір доступних записів.
    public List<Course> getAll() {
        List<Course> obj;
        if(redis.opsForValue().get("courses") == null){
            obj = courseRepository.findAll();
            redis.opsForValue().set("courses", obj);
        } else {
            obj = (List<Course>) redis.opsForValue().get("courses");
        }
        return obj;
    }
    // Знаходить один запис модуля «курси» за id.
    // Контролери викликають його перед редагуванням, видаленням або складанням сторінки з деталями.
    public Course getById(Integer id) {
        Course obj;
        if(redis.opsForValue().get("courseById"+id) == null){
            obj = courseRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
            redis.opsForValue().set("courseById"+id, obj);
        } else {
            obj = (Course) redis.opsForValue().get("courseById"+id);
        }
        return obj;
    }
    // Перетворює список id з форми на об’єкти Course.
    // Коли id не передані, повертає всі курси, щоб форма створення або редагування могла показати повний список.
    public List<Course> getCoursesThroughIds(List<Integer> ids){
        String cacheKey = "coursesByIds" + (ids != null ? ids.toString() : "all");
        if(redis.opsForValue().get(cacheKey) != null) {
            return (List<Course>) redis.opsForValue().get(cacheKey);
        }
        List<Course> courses;
        if(ids!=null && !ids.isEmpty()){
            courses = new ArrayList<>();
            ids.stream().forEach(course-> courses.add(getById(course)));
        }else{
            courses = getAll();
        }
        redis.opsForValue().set(cacheKey, courses);
        return courses;
    }
    // Зберігає новий запис модуля «курси».
    // Метод викликається після того, як контролер зібрав сутність з форми або сервіс згенерував її автоматично.
    public Course create(Course course) {
        Course created = courseRepository.save(course);
        redis.delete("courses");
        return created;
    }

    // Оновлює існуючий запис модуля «курси».
    // Спочатку знаходить поточну сутність, переносить поля name, costPerLesson, description і зберігає її назад у репозиторій.
    public Course update(Integer id, Course course) {
        Course existing = getById(id);
        existing.setName(course.getName());
        existing.setCostPerLesson(course.getCostPerLesson());
        existing.setDescription(course.getDescription());
        existing = courseRepository.save(existing);
        redis.delete("courseById"+id);
        redis.delete("courses");
        return existing;
    }
    // Видаляє запис модуля «курси» за id.
    // Перед deleteById метод читає сутність, щоб видалення проходило через сервісний шар і падало зрозуміло, якщо id некоректний.
    public void deleteById(Integer id) {
        getById(id);
        courseRepository.deleteById(id);
        redis.delete("courseById"+id);
        redis.delete("courses");
    }
}
