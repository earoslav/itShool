package com.example.demo.services.entities;

import com.example.demo.models.entities.Teacher;
import com.example.demo.repositories.entities.TeacherRepository;
import java.util.List;

import com.example.demo.repositories.entities.UserRepository;
import org.springframework.stereotype.Service;

// Сервіс TeacherService містить бізнес-операції для модуля «викладачі».
// Контролери звертаються сюди, щоб не працювати напряму з репозиторіями, mapper-ами та правилами розкладу.
@Service
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private UserRepository userRepository;
    // Отримує через Spring залежності TeacherRepository, UserRepository.
    // Ці сервіси й mapper-и потрібні методам класу для роботи з модулем «викладачі» без ручного створення об’єктів.
    public TeacherService(TeacherRepository teacherRepository, UserRepository userRepository) {
        this.teacherRepository = teacherRepository;
        this.userRepository = userRepository;
    }
    // Повертає всі викладачі з репозиторію.
    // Списки, календарі та форми використовують цей метод, коли треба показати весь набір доступних записів.
    public List<Teacher> getAll() {
        return teacherRepository.findAll();
    }
    // Знаходить один запис модуля «викладачі» за id.
    // Контролери викликають його перед редагуванням, видаленням або складанням сторінки з деталями.
    public Teacher getById(Integer id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + id));
    }
    // Зберігає новий запис модуля «викладачі».
    // Метод викликається після того, як контролер зібрав сутність з форми або сервіс згенерував її автоматично.
    public Teacher create(Teacher teacher) {
        return teacherRepository.save(teacher);
    }
    // Шукає записи модуля «викладачі» за умовами: акаунтом користувача.
    // Фактичний запит виконує `teacherRepository.findByUserId`, а контролер отримує вже готовий результат.
    public Teacher findByUserId(int userId){
        return teacherRepository.findByUserId(userId);
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
        return teacherRepository.save(existing);
    }
    // Перевіряє, чи вже існує запис модуля «викладачі» за умовою: email.
    // Повертає boolean для форм створення та редагування, щоб не допустити дубль або некоректний вибір.
    public boolean checkIfExistsByEmail(String email){
        return userRepository.findByEmailAndRole(email, "TEACHER")!=null;
    }
    // Перевіряє, чи вже існує запис модуля «викладачі» за умовою: акаунтом користувача.
    // Повертає boolean для форм створення та редагування, щоб не допустити дубль або некоректний вибір.
    public boolean checkIfExistsByUserId(int userId){
        return teacherRepository.findByUserId(userId)!=null;
    }
    // Видаляє запис модуля «викладачі» за id.
    // Перед deleteById метод читає сутність, щоб видалення проходило через сервісний шар і падало зрозуміло, якщо id некоректний.
    public void deleteById(Integer id) {
        getById(id);
        teacherRepository.deleteById(id);
    }
}
