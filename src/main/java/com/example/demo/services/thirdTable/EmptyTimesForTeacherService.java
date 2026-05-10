package com.example.demo.services.thirdTable;

import com.example.demo.models.thirdTables.EmptyTimesForTeacher;
import com.example.demo.repositories.thirdTables.EmptyTimesForTeacherRepository;
import java.util.List;
import org.springframework.stereotype.Service;

// Сервіс EmptyTimesForTeacherService містить бізнес-операції для модуля «вільний час викладача».
// Контролери звертаються сюди, щоб не працювати напряму з репозиторіями, mapper-ами та правилами розкладу.
@Service
public class EmptyTimesForTeacherService {
    private final EmptyTimesForTeacherRepository emptyTimesForTeacherRepository;
    // Отримує через Spring залежності EmptyTimesForTeacherRepository.
    // Ці сервіси й mapper-и потрібні методам класу для роботи з модулем «вільний час викладача» без ручного створення об’єктів.
    public EmptyTimesForTeacherService(EmptyTimesForTeacherRepository emptyTimesForTeacherRepository) {
        this.emptyTimesForTeacherRepository = emptyTimesForTeacherRepository;
    }
    // Повертає всі вільні часові слоти викладача з репозиторію.
    // Списки, календарі та форми використовують цей метод, коли треба показати весь набір доступних записів.
    public List<EmptyTimesForTeacher> getAll() {
        return emptyTimesForTeacherRepository.findAll();
    }
    // Знаходить один запис модуля «вільний час викладача» за id.
    // Контролери викликають його перед редагуванням, видаленням або складанням сторінки з деталями.
    public EmptyTimesForTeacher getById(Integer id) {
        return emptyTimesForTeacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("EmptyTimesForTeacher not found with id: " + id));
    }
    // Зберігає новий запис модуля «вільний час викладача».
    // Метод викликається після того, як контролер зібрав сутність з форми або сервіс згенерував її автоматично.
    public EmptyTimesForTeacher create(EmptyTimesForTeacher emptyTimesForTeacher) {
        return emptyTimesForTeacherRepository.save(emptyTimesForTeacher);
    }


    // Оновлює існуючий запис модуля «вільний час викладача».
    // Спочатку знаходить поточну сутність, переносить поля teacher, time і зберігає її назад у репозиторій.
    public EmptyTimesForTeacher update(Integer id, EmptyTimesForTeacher emptyTimesForTeacher) {
        EmptyTimesForTeacher existing = getById(id);
        existing.setTeacher(emptyTimesForTeacher.getTeacher());
        existing.setTime(emptyTimesForTeacher.getTime());
        return emptyTimesForTeacherRepository.save(existing);
    }
    // Шукає записи модуля «вільний час викладача» за умовами: викладачем.
    // Фактичний запит виконує `emptyTimesForTeacherRepository.findAllByTeacherId`, а контролер отримує вже готовий результат.
    public List<EmptyTimesForTeacher> findAllByTeachId(int tId){
        return emptyTimesForTeacherRepository.findAllByTeacherId(tId);
    }
    // Видаляє запис модуля «вільний час викладача» за id.
    // Перед deleteById метод читає сутність, щоб видалення проходило через сервісний шар і падало зрозуміло, якщо id некоректний.
    public void deleteById(Integer id) {
        getById(id);
        emptyTimesForTeacherRepository.deleteById(id);
    }
    // Видаляє записи модуля «вільний час викладача» за умовами: викладачем.
    // Операція делегується `emptyTimesForTeacherRepository.removeAllByTeacherId`, щоб очистити пов’язані дані після дії користувача.
    public void removeAllByTeacherId(int teacherId){
        emptyTimesForTeacherRepository.removeAllByTeacherId(teacherId);
    }
}
