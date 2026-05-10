package com.example.demo.services.entities;

import com.example.demo.models.entities.Student;
import com.example.demo.repositories.entities.StudentRepository;
import java.util.List;

import com.example.demo.repositories.entities.UserRepository;
import org.springframework.stereotype.Service;

// Сервіс StudentService містить бізнес-операції для модуля «студенти».
// Контролери звертаються сюди, щоб не працювати напряму з репозиторіями, mapper-ами та правилами розкладу.
@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private UserRepository userRepository;
    // Отримує через Spring залежності StudentRepository, UserRepository.
    // Ці сервіси й mapper-и потрібні методам класу для роботи з модулем «студенти» без ручного створення об’єктів.
    public StudentService(StudentRepository studentRepository, UserRepository userRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
    }
    // Повертає всі студенти з репозиторію.
    // Списки, календарі та форми використовують цей метод, коли треба показати весь набір доступних записів.
    public List<Student> getAll() {
        return studentRepository.findAll();
    }
    // Знаходить один запис модуля «студенти» за id.
    // Контролери викликають його перед редагуванням, видаленням або складанням сторінки з деталями.
    public Student getById(Integer id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
    }
    // Зберігає новий запис модуля «студенти».
    // Метод викликається після того, як контролер зібрав сутність з форми або сервіс згенерував її автоматично.
    public Student create(Student student) {
        return studentRepository.save(student);
    }
    // Перевіряє, чи вже існує запис модуля «студенти» за умовою: акаунтом користувача.
    // Повертає boolean для форм створення та редагування, щоб не допустити дубль або некоректний вибір.
    public boolean checkIfExistsByUserId(int userId){
        return studentRepository.findByUserId(userId)!=null;
    }
    // Перевіряє, чи вже існує запис модуля «студенти» за умовою: email.
    // Повертає boolean для форм створення та редагування, щоб не допустити дубль або некоректний вибір.
    public boolean checkIfExistsByEmail(String email){
        if(userRepository.findByEmailAndRole(email, "STUDENT")!=null){
            return true;
        }
        return false;
    }
    // Шукає записи модуля «студенти» за умовами: акаунтом користувача.
    // Фактичний запит виконує `studentRepository.findByUserId`, а контролер отримує вже готовий результат.
    public  Student findByUserId(int id){
        return studentRepository.findByUserId(id);
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
        return existing;
    }
    // Видаляє запис модуля «студенти» за id.
    // Перед deleteById метод читає сутність, щоб видалення проходило через сервісний шар і падало зрозуміло, якщо id некоректний.
    public void deleteById(Integer id) {
        getById(id);
        studentRepository.deleteById(id);
    }
}
