package com.example.demo.services.entities;

import com.example.demo.models.entities.Teacher;
import com.example.demo.repositories.entities.TeacherRepository;
import java.util.List;
import java.util.stream.Collectors;

import com.example.demo.repositories.entities.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

// Сервіс TeacherService містить бізнес-операції для модуля «викладачі».
// Контролери звертаються сюди, щоб не працювати напряму з репозиторіями, mapper-ами та правилами розкладу.
@Service
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private UserRepository userRepository;
    private RedisTemplate redis;

    // Отримує через Spring залежності TeacherRepository, UserRepository.
    // Ці сервіси й mapper-и потрібні методам класу для роботи з модулем «викладачі» без ручного створення об’єктів.
    public TeacherService(TeacherRepository teacherRepository, UserRepository userRepository, @Qualifier("redisTemplate") RedisTemplate redis) {
        this.teacherRepository = teacherRepository;
        this.userRepository = userRepository;
        this.redis = redis;

    }
    // Повертає всі викладачі з репозиторію.
    // Списки, календарі та форми використовують цей метод, коли треба показати весь набір доступних записів.
    public List<Teacher> getAll() {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Teacher> obj;
        if(redis.opsForValue().get("teachers") == null){
            obj = teacherRepository.findAll();
            redis.opsForValue().set("teachers", obj);
        } else {
            obj = objectMapper.convertValue(redis.opsForValue().get("teachers"), new TypeReference<List<Teacher>>() {});
            System.out.println(obj);
        }
        return obj;
    }
    // Знаходить один запис модуля «викладачі» за id.
    // Контролери викликають його перед редагуванням, видаленням або складанням сторінки з деталями.
    public Teacher getById(Integer id) {
        Teacher obj;
        if(redis.opsForValue().get("teacherById"+id) == null){
            obj = teacherRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + id));
            redis.opsForValue().set("teacherById"+id, obj);
        } else {
            obj = (Teacher) redis.opsForValue().get("teacherById"+id);
        }
        return obj;
    }
    // Зберігає новий запис модуля «викладачі».
    // Метод викликається після того, як контролер зібрав сутність з форми або сервіс згенерував її автоматично.
    public Teacher create(Teacher teacher) {
        Teacher created = teacherRepository.save(teacher);
        redis.delete("teachers");
        return created;
    }
    // Шукає записи модуля «викладачі» за умовами: акаунтом користувача.
    // Фактичний запит виконує `teacherRepository.findByUserId`, а контролер отримує вже готовий результат.
    public Teacher findByUserId(int userId){
        Teacher obj;
        if(redis.opsForValue().get("teacherByUserId"+userId) == null){
            obj = teacherRepository.findByUserId(userId);
            redis.opsForValue().set("teacherByUserId"+userId, obj);
        } else {
            obj = (Teacher) redis.opsForValue().get("teacherByUserId"+userId);
        }
        return obj;
    }
    // Оновлює існуючий запис модуля «викладачі».
    // Спочатку знаходить поточну сутність, переносить поля name, age, email, password, comment, phoneNumber, unpaidMoney і зберігає її назад у репозиторій.
    public Teacher update(Integer id, Teacher teacher) {
        Teacher existing = getById(id);
        existing.setName(teacher.getName());
        existing.setAge(teacher.getAge());
        existing.setEmail(teacher.getEmail());
        existing.setPassword(teacher.getPassword());
        existing.setComment(teacher.getComment());
        existing.setPhoneNumber(teacher.getPhoneNumber());
        existing.setUnpaidMoney(teacher.getUnpaidMoney());
        existing.setTgUsername(teacher.getTgUsername());
        existing.setFreeTimeIds(teacher.getFreeTimeIds());
        existing = teacherRepository.save(existing);
        redis.delete("teacherById"+id);
        redis.delete("teachers");
        if(existing.getUser() != null) redis.delete("teacherByUserId"+existing.getUser().getId());
        redis.delete("teacherByEmail"+existing.getEmail());
        return existing;
    }
    // Перевіряє, чи вже існує запис модуля «викладачі» за умовою: email.
    // Повертає boolean для форм створення та редагування, щоб не допустити дубль або некоректний вибір.
    public boolean checkIfExistsByEmail(String email){
        Object obj;
        if(redis.opsForValue().get("teacherByEmail"+email) == null){
            obj = userRepository.findByEmailAndRole(email, "TEACHER");
            redis.opsForValue().set("teacherByEmail"+email, obj);
        } else {
            obj = redis.opsForValue().get("teacherByEmail"+email);
        }
        return obj!=null;
    }
    // Перевіряє, чи вже існує запис модуля «викладачі» за умовою: акаунтом користувача.
    // Повертає boolean для форм створення та редагування, щоб не допустити дубль або некоректний вибір.
    public boolean checkIfExistsByUserId(int userId){
        Teacher obj;
        if(redis.opsForValue().get("teacherByUserId"+userId) == null){
            obj = teacherRepository.findByUserId(userId);
            redis.opsForValue().set("teacherByUserId"+userId, obj);
        } else {
            obj = (Teacher) redis.opsForValue().get("teacherByUserId"+userId);
        }
        return obj!=null;
    }
    // Видаляє запис модуля «викладачі» за id.
    // Перед deleteById метод читає сутність, щоб видалення проходило через сервісний шар і падало зрозуміло, якщо id некоректний.
    public void deleteById(Integer id) {
        Teacher existing = getById(id);
        teacherRepository.deleteById(id);
        redis.delete("teacherById"+id);
        redis.delete("teachers");
        if(existing.getUser() != null) redis.delete("teacherByUserId"+existing.getUser().getId());
        redis.delete("teacherByEmail"+existing.getEmail());
    }
}
