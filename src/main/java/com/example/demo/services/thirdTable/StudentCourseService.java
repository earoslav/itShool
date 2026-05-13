package com.example.demo.services.thirdTable;

import com.example.demo.models.entities.Student;
import com.example.demo.models.thirdTables.StudentCourse;
import com.example.demo.repositories.thirdTables.StudentCourseRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

// Сервіс StudentCourseService містить бізнес-операції для модуля «курси студента».
// Контролери звертаються сюди, щоб не працювати напряму з репозиторіями, mapper-ами та правилами розкладу.
@Service
public class StudentCourseService {
    private final StudentCourseRepository studentCourseRepository;
    private RedisTemplate redis;
    // Отримує через Spring залежності StudentCourseRepository.
    // Ці сервіси й mapper-и потрібні методам класу для роботи з модулем «курси студента» без ручного створення об’єктів.
    public StudentCourseService(StudentCourseRepository studentCourseRepository, @Qualifier("redisTemplate") RedisTemplate redis) {
        this.studentCourseRepository = studentCourseRepository;
        this.redis = redis;
    }
    // Повертає всі зв’язки студента з курсами з репозиторію.
    // Списки, календарі та форми використовують цей метод, коли треба показати весь набір доступних записів.
    public List<StudentCourse> getAll() {
        List<StudentCourse> obj;
        if(redis.opsForValue().get("studentCourses") == null){
            obj = studentCourseRepository.findAll();
            redis.opsForValue().set("studentCourses", obj);
        } else {
            obj = (List<StudentCourse>) redis.opsForValue().get("studentCourses");
        }
        return obj;
    }
    // Знаходить один запис модуля «курси студента» за id.
    // Контролери викликають його перед редагуванням, видаленням або складанням сторінки з деталями.
    public StudentCourse getById(Integer id) {
        StudentCourse obj;
        if(redis.opsForValue().get("studentCourseById"+id) == null){
            obj = studentCourseRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("StudentCourse not found with id: " + id));
            redis.opsForValue().set("studentCourseById"+id, obj);
        } else {
            obj = (StudentCourse) redis.opsForValue().get("studentCourseById"+id);
        }
        return obj;
    }
    // Видаляє всі зв’язки студента з курсами.
    // Це потрібно під час видалення або повного оновлення профілю студента, щоб прибрати старі навчальні прив’язки.
    public void deleteAllCoursesByStudent(Student student){
        studentCourseRepository.deleteAllByStudent(student);
        redis.delete("studentCourses");
    }
    // Зберігає новий запис модуля «курси студента».
    // Метод викликається після того, як контролер зібрав сутність з форми або сервіс згенерував її автоматично.
    public StudentCourse create(StudentCourse studentCourse) {
        StudentCourse created = studentCourseRepository.save(studentCourse);
        redis.delete("studentCourses");
        return created;
    }

    // Оновлює існуючий запис модуля «курси студента».
    // Спочатку знаходить поточну сутність, переносить поля course, student і зберігає її назад у репозиторій.
    public StudentCourse update(Integer id, StudentCourse studentCourse) {
        StudentCourse existing = getById(id);
        existing.setCourse(studentCourse.getCourse());
        existing.setStudent(studentCourse.getStudent());
        existing = studentCourseRepository.save(existing);
        redis.delete("studentCourseById"+id);
        redis.delete("studentCourses");
        return existing;
    }
    // Видаляє запис модуля «курси студента» за id.
    // Перед deleteById метод читає сутність, щоб видалення проходило через сервісний шар і падало зрозуміло, якщо id некоректний.
    public void deleteById(Integer id) {
        getById(id);
        studentCourseRepository.deleteById(id);
        redis.delete("studentCourseById"+id);
        redis.delete("studentCourses");
    }
}
