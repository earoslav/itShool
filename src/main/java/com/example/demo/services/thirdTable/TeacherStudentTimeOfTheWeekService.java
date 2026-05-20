package com.example.demo.services.thirdTable;

import com.example.demo.dto.thirdTable.TeacherStudentTimeOfTheWeekDTO;
import com.example.demo.mapper.thirdTable.TSWMapper;
import com.example.demo.models.thirdTables.TeacherStudentTimeOfTheWeek;
import com.example.demo.repositories.thirdTables.TeacherStudentTimeOfTheWeekRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// TeacherStudentTimeOfTheWeekService contains business operations for the "teacher-student persistent schedule" module.
// Controllers call this service to avoid direct interaction with repositories, mappers, and scheduling rules.
@Service
@Transactional
public class TeacherStudentTimeOfTheWeekService {
    private final TeacherStudentTimeOfTheWeekRepository tSWRepository;
    private RedisTemplate<String, Object> redisTemplate;
    private ObjectMapper objectMapper;
    private TSWMapper tswMapper;
    // Receives TeacherStudentTimeOfTheWeekRepository dependency through Spring.
    // These services and mappers are required by the class methods to work with the "teacher-student-time associations" module without manual object creation.
    public TeacherStudentTimeOfTheWeekService(TeacherStudentTimeOfTheWeekRepository tSWRepository, @Qualifier("schoolRedisTemplate") RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper, TSWMapper tswMapper) {
        this.tSWRepository = tSWRepository;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.tswMapper = tswMapper;
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
        TeacherStudentTimeOfTheWeek obj;
        if(redisTemplate.opsForValue().get("teacherStudentTimeOfWeekById"+id) == null){
            obj = tSWRepository.findById(id).get();
            redisTemplate.opsForValue().set("teacherStudentTimeOfWeekById"+id, tswMapper.mapTSWToTSWDTO(obj));
        } else {
            obj = tswMapper.mapTSWDTOToTSW(objectMapper.convertValue(redisTemplate.opsForValue().get("teacherStudentTimeOfWeekById"+id), new TypeReference<TeacherStudentTimeOfTheWeekDTO>() {}));
        }
        return obj;
    }
    // Видаляє запис модуля «постійний розклад викладача зі студентом» за id.
    // Перед deleteById метод читає сутність, щоб видалення проходило через сервісний шар і падало зрозуміло, якщо id некоректний.
    public void deleteById(int id){
        tSWRepository.deleteById(id);
        redisTemplate.delete("teacherStudentTimeOfWeekById"+id);
        redisTemplate.delete("teacherStudentTimeOfWeeks");
    }
    // Returns all persistent schedule records from the repository.
    // Lists, calendars, and forms use this method when the entire set of available records needs to be shown.
    public List<TeacherStudentTimeOfTheWeek> getAll() {
        Object cachedData = redisTemplate.opsForValue().get("teacherStudentTimeOfWeeks");
        if (cachedData == null) {
            List<TeacherStudentTimeOfTheWeek> obj = tSWRepository.findAll();
            redisTemplate.opsForValue().set("teacherStudentTimeOfWeeks", obj.stream().map(tswMapper::mapTSWToTSWDTO).toList());
            return obj;
        } else {
            List<TeacherStudentTimeOfTheWeekDTO> dtos = objectMapper.convertValue(cachedData, new TypeReference<List<TeacherStudentTimeOfTheWeekDTO>>() {});
            if (dtos == null) {
                List<TeacherStudentTimeOfTheWeek> obj = tSWRepository.findAll();
                redisTemplate.opsForValue().set("teacherStudentTimeOfWeeks", obj.stream().map(tswMapper::mapTSWToTSWDTO).toList());
                return obj;
            }
            return dtos.stream().map(tswMapper::mapTSWDTOToTSW).toList();
        }
    }
    // Видаляє записи модуля «постійний розклад викладача зі студентом» за умовами: remove by teacher id, часовим слотом тижня.
    // Операція делегується `tSWRepository.removeAllByTeacherIdAndTimeOfTheWeekId`, щоб очистити пов’язані дані після дії користувача.
    public void removeByTIdAndWId(int tId, int wId){
        tSWRepository.removeAllByTeacherIdAndTimeOfTheWeek(tId, wId);
        redisTemplate.delete("teacherStudentTimeOfWeeks");
        redisTemplate.delete("teacherStudentTimeOfWeeksByTeacherId"+tId);
    }
    // Видаляє записи модуля «постійний розклад викладача зі студентом» за умовами: студентом, викладачем, часовим слотом тижня.
    // Операція делегується `tSWRepository.removeAllByStudentIdAndTeacherIdAndTimeOfTheWeekId`, щоб очистити пов’язані дані після дії користувача.
    public void removeAllByStIdAndTeachIdAndTswId(int stId, int teachId, int tswId){
        tSWRepository.removeAllByStudentIdAndTeacherIdAndTimeOfTheWeek(stId, teachId, tswId);
        redisTemplate.delete("teacherStudentTimeOfWeeks");
        redisTemplate.delete("teacherStudentTimeOfWeeksByTeacherId"+teachId);
    }
    // Searches for records in the "teacher-student persistent schedule" module by condition: teacher.
    // The actual query is performed by `tSWRepository.findAllByTeacherId`, and the controller receives the final result.
    public List<TeacherStudentTimeOfTheWeek> findAllByTeachId(int id){
        List<TeacherStudentTimeOfTheWeek> obj;
        if(redisTemplate.opsForValue().get("teacherStudentTimeOfWeeksByTeacherId"+id) == null){
            obj = tSWRepository.findAllByTeacherId(id);
            redisTemplate.opsForValue().set("teacherStudentTimeOfWeeksByTeacherId"+id, obj.stream().map(tswMapper::mapTSWToTSWDTO).toList());
        } else {
            obj = objectMapper.convertValue(redisTemplate.opsForValue().get("teacherStudentTimeOfWeeksByTeacherId"+id), new TypeReference<List<TeacherStudentTimeOfTheWeekDTO>>() {}).stream().map(tswMapper::mapTSWDTOToTSW).toList();
        }
        return obj;
    }
    // Видаляє записи модуля «постійний розклад викладача зі студентом» за умовами: викладачем, студентом.
    // Операція делегується `tSWRepository.removeAllByTeacherIdAndStudentId`, щоб очистити пов’язані дані після дії користувача.
    public void removeAllByTidAndSid(int idT, int idSt){
        tSWRepository.removeAllByTeacherIdAndStudentId(idT, idSt);
        redisTemplate.delete("teacherStudentTimeOfWeeks");
        redisTemplate.delete("teacherStudentTimeOfWeeksByTeacherId"+idT);
    }
    // Видаляє записи модуля «постійний розклад викладача зі студентом» за умовами: id st, is tsw.
    // Операція делегується `tSWRepository.removeAllByStudentIdAndTimeOfTheWeekId`, щоб очистити пов’язані дані після дії користувача.
    public void removeAllByIdStAndIsTsw(int idst, int idTsw){
        tSWRepository.removeAllByStudentIdAndTeacherIdAndTimeOfTheWeek(idst, idTsw, idTsw);
        redisTemplate.delete("teacherStudentTimeOfWeeks");
    }
}
