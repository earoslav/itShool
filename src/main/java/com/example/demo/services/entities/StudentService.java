package com.example.demo.services.entities;

import com.example.demo.models.entities.Student;
import com.example.demo.models.entities.User;
import com.example.demo.repositories.entities.StudentRepository;
import java.util.List;
import java.util.Optional;

import com.example.demo.repositories.entities.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

// Сервіс StudentService містить бізнес-операції для модуля «студенти».
// Контролери звертаються сюди, щоб не працювати напряму з репозиторіями, mapper-ами та правилами розкладу.
@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private UserRepository userRepository;
    private RedisTemplate redis;
    // Отримує через Spring залежності StudentRepository, UserRepository.
    // Ці сервіси й mapper-и потрібні методам класу для роботи з модулем «студенти» без ручного створення об’єктів.
    public StudentService(StudentRepository studentRepository, UserRepository userRepository, @Qualifier("redisTemplate") RedisTemplate redis) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.redis = redis;
    }
    // Повертає всі студенти з репозиторію.
    // Списки, календарі та форми використовують цей метод, коли треба показати весь набір доступних записів.
    public List<Student> getAll() {
        List<Student> obj;
        if(redis.opsForValue().get("students") == null){
            obj = studentRepository.findAll();
            redis.opsForValue().set("students", obj);
        } else {
            obj = (List<Student>) redis.opsForValue().get("students");
        }
        return obj;
    }
    // Знаходить один запис модуля «студенти» за id.
    // Контролери викликають його перед редагуванням, видаленням або складанням сторінки з деталями.
    public Student getById(Integer id) {
        Optional<Student> obj;
        if(redis.opsForValue().get("studentById"+id) == null){
            obj = studentRepository.findById(id);
            redis.opsForValue().set("studentById"+id, obj);
        } else {
            obj = (Optional<Student>) redis.opsForValue().get("studentById"+id);
        }
        return obj.get();
    }
    // Зберігає новий запис модуля «студенти».
    // Метод викликається після того, як контролер зібрав сутність з форми або сервіс згенерував її автоматично.
    public Student create(Student student) {
        Student created = studentRepository.save(student);
        redis.delete("students");
        return created;
    }
    // Перевіряє, чи вже існує запис модуля «студенти» за умовою: акаунтом користувача.
    // Повертає boolean для форм створення та редагування, щоб не допустити дубль або некоректний вибір.
    public boolean checkIfExistsByUserId(int userId){
        Student obj;
        if(redis.opsForValue().get("studentByUserId"+userId) == null){
            obj = studentRepository.findByUserId(userId);
            redis.opsForValue().set("studentByUserId"+userId, obj);
        } else {
            obj = (Student) redis.opsForValue().get("studentByUserId"+userId);
        }
        return obj!=null;
    }
    // Перевіряє, чи вже існує запис модуля «студенти» за умовою: email.
    // Повертає boolean для форм створення та редагування, щоб не допустити дубль або некоректний вибір.
    public boolean checkIfExistsByEmail(String email){
        User obj;
        if(redis.opsForValue().get("studentByEmail"+email) == null){
            obj = userRepository.findByEmailAndRole(email, "STUDENT");
            redis.opsForValue().set("studentByEmail"+email, obj);
        } else {
            obj = (User) redis.opsForValue().get("studentByEmail"+email);
        }
        return obj!=null;
    }
    // Шукає записи модуля «студенти» за умовами: акаунтом користувача.
    // Фактичний запит виконує `studentRepository.findByUserId`, а контролер отримує вже готовий результат.
    public  Student findByUserId(int id){
        Student obj;
        if(redis.opsForValue().get("studentByUserId"+id) == null){
            obj = studentRepository.findByUserId(id);
            redis.opsForValue().set("studentByUserId"+id, obj);
        } else {
            obj = (Student) redis.opsForValue().get("studentByUserId"+id);
        }
        return obj;
    }
    // Оновлює існуючий запис модуля «студенти».
    // Спочатку знаходить поточну сутність, переносить поля name, age, email, phoneNumber, password, comment і зберігає її назад у репозиторій.
    public Student update(Integer id, Student student) {
        Student existing = getById(id);
        existing.setName(student.getName());
        existing.setAge(student.getAge());
        existing.setEmail(student.getEmail());
        existing.setPhoneNumber(student.getPhoneNumber());
        existing.setPassword(student.getPassword());
        existing.setComment(student.getComment());
        existing = studentRepository.save(existing);
        redis.delete("studentById"+id);
        redis.delete("students");
        if(existing.getUser() != null) redis.delete("studentByUserId"+existing.getUser().getId());
        redis.delete("studentByEmail"+existing.getEmail());
        return existing;
    }
    // Видаляє запис модуля «студенти» за id.
    // Перед deleteById метод читає сутність, щоб видалення проходило через сервісний шар і падало зрозуміло, якщо id некоректний.
    public void deleteById(Integer id) {
        Student existing = getById(id);
        studentRepository.deleteById(id);
        redis.delete("studentById"+id);
        redis.delete("students");
        if(existing.getUser() != null) redis.delete("studentByUserId"+existing.getUser().getId());
        redis.delete("studentByEmail"+existing.getEmail());
    }
}
