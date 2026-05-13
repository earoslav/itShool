package com.example.demo.services.thirdTable;

import com.example.demo.models.thirdTables.TeacherStudentTimeOfTheWeek;
import com.example.demo.repositories.thirdTables.TeacherStudentTimeOfTheWeekRepository;
import org.springframework.stereotype.Service;

import java.util.List;

// Сервіс TeacherStudentTimeOfTheWeekService містить бізнес-операції для модуля «постійний розклад викладача зі студентом».
// Контролери звертаються сюди, щоб не працювати напряму з репозиторіями, mapper-ами та правилами розкладу.
@Service
public class TeacherStudentTimeOfTheWeekService {
    private TeacherStudentTimeOfTheWeekRepository tSWRepository;
    // Отримує через Spring залежності TeacherStudentTimeOfTheWeekRepository.
    // Ці сервіси й mapper-и потрібні методам класу для роботи з модулем «постійний розклад викладача зі студентом» без ручного створення об’єктів.
    public TeacherStudentTimeOfTheWeekService(TeacherStudentTimeOfTheWeekRepository tSWRepository) {
        this.tSWRepository = tSWRepository;
    }
    // Зберігає новий запис модуля «постійний розклад викладача зі студентом».
    // Метод викликається після того, як контролер зібрав сутність з форми або сервіс згенерував її автоматично.
    public void create(TeacherStudentTimeOfTheWeek tsw){
        tSWRepository.save(tsw);
    }
    // Видаляє записи модуля «постійний розклад викладача зі студентом» за умовами: remove all tsw by student id.
    // Операція делегується `tSWRepository.removeAllByStudentId`, щоб очистити пов’язані дані після дії користувача.
    public void removeAllTswByStudentId(int id){
        tSWRepository.removeAllByStudentId(id);
    }
    // Знаходить один запис модуля «постійний розклад викладача зі студентом» за id.
    // Контролери викликають його перед редагуванням, видаленням або складанням сторінки з деталями.
    public TeacherStudentTimeOfTheWeek getById(int id){
        return tSWRepository.findById(id).get();
    }
    // Видаляє запис модуля «постійний розклад викладача зі студентом» за id.
    // Перед deleteById метод читає сутність, щоб видалення проходило через сервісний шар і падало зрозуміло, якщо id некоректний.
    public void deleteById(int id){
        tSWRepository.deleteById(id);
    }
    // Повертає всі записи постійного розкладу з репозиторію.
    // Списки, календарі та форми використовують цей метод, коли треба показати весь набір доступних записів.
    public List<TeacherStudentTimeOfTheWeek> getAll(){
        return tSWRepository.findAll();
    }
    // Видаляє записи модуля «постійний розклад викладача зі студентом» за умовами: remove by teacher id, часовим слотом тижня.
    // Операція делегується `tSWRepository.removeAllByTeacherIdAndTimeOfTheWeekId`, щоб очистити пов’язані дані після дії користувача.
    public void removeByTIdAndWId(int tId, int wId){
        tSWRepository.removeAllByTeacherIdAndTimeOfTheWeek(tId, wId);
    }
    // Видаляє записи модуля «постійний розклад викладача зі студентом» за умовами: студентом, викладачем, часовим слотом тижня.
    // Операція делегується `tSWRepository.removeAllByStudentIdAndTeacherIdAndTimeOfTheWeekId`, щоб очистити пов’язані дані після дії користувача.
    public void removeAllByStIdAndTeachIdAndTswId(int stId, int teachId, int tswId){
        tSWRepository.removeAllByStudentIdAndTeacherIdAndTimeOfTheWeek(stId, teachId, tswId);
    }
    // Шукає записи модуля «постійний розклад викладача зі студентом» за умовами: викладачем.
    // Фактичний запит виконує `tSWRepository.findAllByTeacherId`, а контролер отримує вже готовий результат.
    public List<TeacherStudentTimeOfTheWeek> findAllByTeachId(int id){
        return  tSWRepository.findAllByTeacherId(id);
    }
    // Видаляє записи модуля «постійний розклад викладача зі студентом» за умовами: викладачем, студентом.
    // Операція делегується `tSWRepository.removeAllByTeacherIdAndStudentId`, щоб очистити пов’язані дані після дії користувача.
    public void removeAllByTidAndSid(int idT, int idSt){
        tSWRepository.removeAllByTeacherIdAndStudentId(idT, idSt);
    }
    // Видаляє записи модуля «постійний розклад викладача зі студентом» за умовами: id st, is tsw.
    // Операція делегується `tSWRepository.removeAllByStudentIdAndTimeOfTheWeekId`, щоб очистити пов’язані дані після дії користувача.
    public void removeAllByIdStAndIsTsw(int idst, int idTsw){tSWRepository.removeAllByStudentIdAndTimeOfTheWeek(idst, idTsw);}
}
