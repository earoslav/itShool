package com.example.demo.services.thirdTable;

import com.example.demo.models.thirdTables.TeacherStudentTimeOfTheWeek;
import com.example.demo.repositories.thirdTables.TeacherStudentTimeOfTheWeekRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// TeacherStudentTimeOfTheWeekService contains business operations for the "teacher-student persistent schedule" module.
// Controllers call this service to avoid direct interaction with repositories, mappers, and scheduling rules.
@Service
@Transactional
public class TeacherStudentTimeOfTheWeekService {
    private final TeacherStudentTimeOfTheWeekRepository tSWRepository;
    // Receives TeacherStudentTimeOfTheWeekRepository dependency through Spring.
    // These services and mappers are required by the class methods to work with the "teacher-student-time associations" module without manual object creation.
    public TeacherStudentTimeOfTheWeekService(TeacherStudentTimeOfTheWeekRepository tSWRepository) {
        this.tSWRepository = tSWRepository;
    }
    // Зберігає новий запис у модулі «постійний розклад викладач-студент».
    // Цей метод викликається після того, як контролер зібрав сутність з форми або сервіс згенерував її автоматично.
    public void create(TeacherStudentTimeOfTheWeek tsw){
        tSWRepository.save(tsw);
    }
    // Видаляє записи модуля «постійний розклад викладача зі студентом» за умовою: видалити всі TSW за id студента.
    // Операція делегується `tSWRepository.removeAllByStudentId`, щоб очистити пов’язані дані після дії користувача.
    public void removeAllTswByStudentId(int id){
        tSWRepository.removeAllByStudentId(id);
    }
    // Finds a single record in the "teacher-student persistent schedule" module by ID.
    // Controllers call this before editing, deleting, or assembling a details page.
    public TeacherStudentTimeOfTheWeek getById(Integer id) {
        return tSWRepository.findById(id).get();
    }
    // Видаляє запис модуля «постійний розклад викладача зі студентом» за id.
    // Перед deleteById метод читає сутність, щоб видалення проходило через сервісний шар і падало зрозуміло, якщо id некоректний.
    public void deleteById(int id){
        tSWRepository.deleteById(id);
    }
    // Returns all persistent schedule records from the repository.
    // Lists, calendars, and forms use this method when the entire set of available records needs to be shown.
    public List<TeacherStudentTimeOfTheWeek> getAll() {
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
    // Searches for records in the "teacher-student persistent schedule" module by condition: teacher.
    // The actual query is performed by `tSWRepository.findAllByTeacherId`, and the controller receives the final result.
    public List<TeacherStudentTimeOfTheWeek> findAllByTeachId(int id){
        return tSWRepository.findAllByTeacherId(id);
    }
    // Видаляє записи модуля «постійний розклад викладача зі студентом» за умовами: викладачем, студентом.
    // Операція делегується `tSWRepository.removeAllByTeacherIdAndStudentId`, щоб очистити пов’язані дані після дії користувача.
    public void removeAllByTidAndSid(int idT, int idSt){
        tSWRepository.removeAllByTeacherIdAndStudentId(idT, idSt);
    }
    // Видаляє записи модуля «постійний розклад викладача зі студентом» за умовами: id st, is tsw.
    // Операція делегується `tSWRepository.removeAllByStudentIdAndTimeOfTheWeekId`, щоб очистити пов’язані дані після дії користувача.
    public void removeAllByIdStAndIsTsw(int idst, int idTsw){
        tSWRepository.removeAllByStudentIdAndTeacherIdAndTimeOfTheWeek(idst, idTsw, idTsw);
    }
}
