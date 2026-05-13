package com.example.demo.services.program;

import com.example.demo.dto.adminLessons.LessonAdminLessonsDTO;
import com.example.demo.dto.adminLessons.TeacherAdminLessonsDTO;
import com.example.demo.dto.adminLessons.TimeOfTheWeekAdminLessonsDTO;
import com.example.demo.mapper.programe.LessonMapper;
import com.example.demo.mapper.univMapper.UniversalMapper;
import com.example.demo.models.other.TimeOfTheWeek;
import com.example.demo.models.programe.Lesson;
import com.example.demo.models.entities.Student;
import com.example.demo.models.entities.Teacher;
import com.example.demo.repositories.program.LessonRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import com.example.demo.services.Statuses;
import com.example.demo.services.entities.StudentService;
import com.example.demo.services.entities.TeacherService;
import com.example.demo.services.other.TimeOfTheWeekService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

// Сервіс LessonService містить бізнес-операції для модуля «уроки».
// Контролери звертаються сюди, щоб не працювати напряму з репозиторіями, mapper-ами та правилами розкладу.
@Service
public class LessonService {
    private final LessonRepository lessonRepository;
    private LessonMapper lessonMapper;
    private TeacherService teacherService;
    private ObjectMapper objectMapper;
    private TimeOfTheWeekService tswService;
    private CourseService courseService;
    private StudentService studentService;
    private TimeOfTheWeekService theWeekService;
    private RedisTemplate redis;
    // Отримує через Spring залежності LessonRepository, LessonMapper, TeacherService, ObjectMapper, TimeOfTheWeekService, CourseService, StudentService.
    // Ці сервіси й mapper-и потрібні методам класу для роботи з модулем «уроки» без ручного створення об’єктів.
    public LessonService(LessonRepository lessonRepository, LessonMapper lessonMapper, TeacherService teacherService, ObjectMapper objectMapper, TimeOfTheWeekService tswService, CourseService courseService, StudentService studentService, TimeOfTheWeekService theWeekService, @Qualifier("redisTemplate") RedisTemplate redis) {
        this.lessonRepository = lessonRepository;
        this.lessonMapper = lessonMapper;
        this.teacherService = teacherService;
        this.objectMapper = objectMapper;
        this.tswService = tswService;
        this.courseService = courseService;

        this.studentService = studentService;
        this.theWeekService = theWeekService;
        this.redis = redis;
    }
    // Повертає всі уроки з репозиторію.
    // Списки, календарі та форми використовують цей метод, коли треба показати весь набір доступних записів.
    public List<Lesson> getAll() {
        List<Lesson> obj;
        if(redis.opsForValue().get("lessons") == null){
            obj = lessonRepository.findAll();
            redis.opsForValue().set("lessons", obj);
        } else {
            obj = (List<Lesson>) redis.opsForValue().get("lessons");
        }
        return obj;
    }
    // Знаходить один запис модуля «уроки» за id.
    // Контролери викликають його перед редагуванням, видаленням або складанням сторінки з деталями.
    public Lesson getById(int id) {
        Lesson obj;
        if(redis.opsForValue().get("lessonById"+id) == null){
            obj = lessonRepository.findById(id);
            redis.opsForValue().set("lessonById"+id, obj);
        } else {
            obj = (Lesson) redis.opsForValue().get("lessonById"+id);
        }
        return obj;
    }
    // Видаляє всі уроки конкретного студента.
    // Метод використовують під час очищення даних студента, щоб у розкладі не лишилися заняття без власника.
    public void deleteAllLessonsByStudent(Student student) {
        lessonRepository.deleteAllByStudent(student);
        redis.delete("lessons");
        redis.delete("lessonsByStudentId"+student.getId());
        redis.delete("lessonHashMapForStudent"+student.getId());
        redis.delete("lessonDurationsForStudent"+student.getId());
        redis.delete("lessonHashMapForStudentInAdmin"+student.getId());
        redis.delete("lessonDurationsForStudentInAdmin"+student.getId());
    }
    // Зберігає новий запис модуля «уроки».
    // Метод викликається після того, як контролер зібрав сутність з форми або сервіс згенерував її автоматично.
    public Lesson create(Lesson lesson) {
        Lesson created = lessonRepository.save(lesson);
        redis.delete("lessons");
        if(lesson.getTeacher() != null) {
            int tId = lesson.getTeacher().getId();
            redis.delete("lessonsByTeacherId"+tId);
            redis.delete("lessonHashMapForTecherById"+tId);
            redis.delete("lessonDurationsForTecherById"+tId);
        }
        if(lesson.getStudent() != null) {
            int sId = lesson.getStudent().getId();
            redis.delete("lessonsByStudentId"+sId);
            redis.delete("lessonHashMapForStudent"+sId);
            redis.delete("lessonDurationsForStudent"+sId);
            redis.delete("lessonHashMapForStudentInAdmin"+sId);
            redis.delete("lessonDurationsForStudentInAdmin"+sId);
            if(lesson.getTeacher() != null) {
                int tId = lesson.getTeacher().getId();
                redis.delete("lessonHashMapForTecherAndStudent"+tId+"_"+sId);
                redis.delete("lessonDurationsForTecherAndStudent"+tId+"_"+sId);
                redis.delete("lessonsByTeacherAndStudent"+tId+"_"+sId);
            }
        }
        return created;
    }
    // Повертає уроки викладача, прив’язані до конкретного тижневого слота.
    // Це потрібно під час зміни доступного часу, щоб знайти заняття, які залежать від цього слота.
    public List<Lesson> findAllByTimeOfTheWeekIdAndTeacherId(int timeOfWeekId, int teachId) {
        List<Lesson> obj;
        if(redis.opsForValue().get("lessonsByTimeOfWeekAndTeacher"+timeOfWeekId+"_"+teachId) == null){
            obj = lessonRepository.findAllByTimeOfTheWeekAndTeacherId(timeOfWeekId, teachId);
            redis.opsForValue().set("lessonsByTimeOfWeekAndTeacher"+timeOfWeekId+"_"+teachId, obj);
        } else {
            obj = (List<Lesson>) redis.opsForValue().get("lessonsByTimeOfWeekAndTeacher"+timeOfWeekId+"_"+teachId);
        }
        return obj;
    }
    // Повертає всі уроки конкретного студента.
    // Метод використовують календар студента і перевірка перетинів перед перенесенням або створенням заняття.
    public List<Lesson> findAllByStudentId(int id) {
        List<Lesson> obj;
        if(redis.opsForValue().get("lessonsByStudentId"+id) == null){
            obj = lessonRepository.findByStudentId(id);
            redis.opsForValue().set("lessonsByStudentId"+id, obj);
        } else {
            obj = (List<Lesson>) redis.opsForValue().get("lessonsByStudentId"+id);
        }
        return obj;
    }

    public List<Lesson> findAllByTeacherIdAndLessonTimeAfterAndLessonTimeBefore(int teachId, LocalDateTime after, LocalDateTime before){
        List<Lesson> obj;
        String cacheKey = "lessonsByTeacherAfterBefore"+teachId+"_"+after+"_"+before;
        if(redis.opsForValue().get(cacheKey) == null){
            obj = lessonRepository.findAllByTeacherIdAndLessonTimeAfterAndLessonTimeBefore(teachId, after, before);
            redis.opsForValue().set(cacheKey, obj);
        } else {
            obj = (List<Lesson>) redis.opsForValue().get(cacheKey);
        }
        return obj;
    }

    // Перевіряє, чи в базі вже є урок на точний LocalDateTime.
    // Повертає boolean для швидкої перевірки дубля у розкладі.
//    public boolean checkIfExistsByLessonTime(LocalDateTime time) {
//        return lessonRepository.findByLessonTime(time) != null;
//    }

    // Оновлює існуючий запис модуля «уроки».
    // Спочатку знаходить поточну сутність, переносить поля student, teacher, course, lessonTime, status і зберігає її назад у репозиторій.
    public Lesson update(Integer id, Lesson lesson) {
        Lesson existing = getById(id);
        existing.setStudent(lesson.getStudent());
        existing.setTeacher(lesson.getTeacher());
        existing.setCourse(lesson.getCourse());
        existing.setLessonTime(lesson.getLessonTime());
        existing.setStatus(lesson.getStatus());
        existing = lessonRepository.save(existing);
        redis.delete("lessonById"+id);
        redis.delete("lessons");
        if(existing.getTeacher() != null) {
            int tId = existing.getTeacher().getId();
            redis.delete("lessonsByTeacherId"+tId);
            redis.delete("lessonHashMapForTecherById"+tId);
            redis.delete("lessonDurationsForTecherById"+tId);
        }
        if(existing.getStudent() != null) {
            int sId = existing.getStudent().getId();
            redis.delete("lessonsByStudentId"+sId);
            redis.delete("lessonHashMapForStudent"+sId);
            redis.delete("lessonDurationsForStudent"+sId);
            redis.delete("lessonHashMapForStudentInAdmin"+sId);
            redis.delete("lessonDurationsForStudentInAdmin"+sId);
            if(existing.getTeacher() != null) {
                int tId = existing.getTeacher().getId();
                redis.delete("lessonHashMapForTecherAndStudent"+tId+"_"+sId);
                redis.delete("lessonDurationsForTecherAndStudent"+tId+"_"+sId);
                redis.delete("lessonsByTeacherAndStudent"+tId+"_"+sId);
            }
        }
        return existing;
    }

    // Перевіряє, чи має викладач урок у конкретний час.
    // Метод дивиться у LessonRepository за teacherId і lessonTime та повертає true, якщо слот зайнятий.
//    public boolean check(LocalDateTime time, int id) {
//        return !lessonRepository.findAllByTeacherIdAndLessonTime(id, time).isEmpty();
//    }
    // Видаляє запис модуля «уроки» за id.
    // Перед deleteById метод читає сутність, щоб видалення проходило через сервісний шар і падало зрозуміло, якщо id некоректний.
    public void deleteById(Integer id) {
        Lesson existing = getById(id);
        lessonRepository.deleteById(id);
        redis.delete("lessonById"+id);
        redis.delete("lessons");
        if(existing.getTeacher() != null) {
            int tId = existing.getTeacher().getId();
            redis.delete("lessonsByTeacherId"+tId);
            redis.delete("lessonHashMapForTecherById"+tId);
            redis.delete("lessonDurationsForTecherById"+tId);
        }
        if(existing.getStudent() != null) {
            int sId = existing.getStudent().getId();
            redis.delete("lessonsByStudentId"+sId);
            redis.delete("lessonHashMapForStudent"+sId);
            redis.delete("lessonDurationsForStudent"+sId);
            redis.delete("lessonHashMapForStudentInAdmin"+sId);
            redis.delete("lessonDurationsForStudentInAdmin"+sId);
            if(existing.getTeacher() != null) {
                int tId = existing.getTeacher().getId();
                redis.delete("lessonHashMapForTecherAndStudent"+tId+"_"+sId);
                redis.delete("lessonDurationsForTecherAndStudent"+tId+"_"+sId);
                redis.delete("lessonsByTeacherAndStudent"+tId+"_"+sId);
            }
        }
    }
    // Шукає записи модуля «уроки» за умовами: викладачем.
    // Фактичний запит виконує `lessonRepository.findAllByTeacherId`, а контролер отримує вже готовий результат.
    public List<Lesson> findAllByTeachId(int id) {
        List<Lesson> obj;
        if(redis.opsForValue().get("lessonsByTeacherId"+id) == null){
            obj = lessonRepository.findAllByTeacherId(id);
            redis.opsForValue().set("lessonsByTeacherId"+id, obj);
        } else {
            obj = (List<Lesson>) redis.opsForValue().get("lessonsByTeacherId"+id);
        }
        return obj;
    }
    // Шукає урок студента на конкретну дату й час.
    // Такий пошук потрібен, коли дія з UI приходить як вибраний часовий слот.
//    public Lesson findByStudentIdAndTime(LocalDateTime time, int id) {
//        return lessonRepository.findByLessonTimeAndStudentId(time, id);
//    }
    // Повертає уроки однієї пари викладач-студент у межах одного тижневого слота.
    // Фоновий менеджер уроків використовує це, щоб продовжувати саме потрібний ланцюжок WILL-занять.
    public List<Lesson> findAllByTeachIdAndStIdAndWeekId(int tId, int sId, int wId){
        List<Lesson> obj;
        String cacheKey = "lessonsByTeachStWeek"+tId+"_"+sId+"_"+wId;
        if(redis.opsForValue().get(cacheKey) == null){
            obj = lessonRepository.findAllByTeacherIdAndStudentIdAndTimeOfTheWeek(tId, sId, wId);
            redis.opsForValue().set(cacheKey, obj);
        } else {
            obj = (List<Lesson>) redis.opsForValue().get(cacheKey);
        }
        return obj;
    }
    public List<Lesson> findAllByStudentIdAndLessonTimeAfterAndLessonTimeBefore(int studentId, LocalDateTime after, LocalDateTime before){
        List<Lesson> obj;
        String cacheKey = "lessonsByStudentAfterBefore"+studentId+"_"+after+"_"+before;
        if(redis.opsForValue().get(cacheKey) == null){
            obj = lessonRepository.findAllByStudentIdAndLessonTimeAfterAndLessonTimeBefore(studentId, after, before);
            redis.opsForValue().set(cacheKey, obj);
        } else {
            obj = (List<Lesson>) redis.opsForValue().get(cacheKey);
        }
        return obj;
    }
    // Видаляє уроки, прив’язані до вибраного слота тижня.
    // Метод потрібен, коли адміністратор або викладач прибирає доступний час і треба очистити залежні заняття.
//    public void deleteAllByTimeOfTheWeekId(int id) {
//        lessonRepository.removeAllByTimeOfTheWeekId(id);
//    }
    // Видаляє майбутні уроки конкретної пари студент-викладач у заданому слоті.
    // Так курс можна прибрати без зачіпання вже минулих занять.
    public void deleteAllByStIdAndTeachIdAndTswIdAndLesTimeAfterNow(int stId, int teachId, int tswId, LocalDateTime now) {
        lessonRepository.removeAllByStudentIdAndTeacherIdAndTimeOfTheWeekAndLessonTimeAfter(stId, teachId, tswId, now);
        redis.delete("lessons");
        redis.delete("lessonsByTeacherId"+teachId);
        redis.delete("lessonsByStudentId"+stId);
        redis.delete("lessonsByTeacherAndStudent"+teachId+"_"+stId);
        redis.delete("lessonHashMapForTecherById"+teachId);
        redis.delete("lessonDurationsForTecherById"+teachId);
        redis.delete("lessonHashMapForStudent"+stId);
        redis.delete("lessonDurationsForStudent"+stId);
        redis.delete("lessonHashMapForStudentInAdmin"+stId);
        redis.delete("lessonDurationsForStudentInAdmin"+stId);
        redis.delete("lessonHashMapForTecherAndStudent"+teachId+"_"+stId);
        redis.delete("lessonDurationsForTecherAndStudent"+teachId+"_"+stId);
    }
    // Повертає всі уроки між конкретним викладачем і студентом.
    // Ці дані використовують сторінки teacherStudentLessons/studentLessons для календаря однієї навчальної пари.
    public List<Lesson> findAllByIdTandIdSt(int idT, int idSt) {
        List<Lesson> obj;
        if(redis.opsForValue().get("lessonsByTeacherAndStudent"+idT+"_"+idSt) == null){
            obj = lessonRepository.findAllByTeacherIdAndStudentId(idT, idSt);
            redis.opsForValue().set("lessonsByTeacherAndStudent"+idT+"_"+idSt, obj);
        } else {
            obj = (List<Lesson>) redis.opsForValue().get("lessonsByTeacherAndStudent"+idT+"_"+idSt);
        }
        return obj;
    }
    public LocalDateTime manageOneTime(TimeOfTheWeek empTFT, LocalDateTime finalNow, String status){
        LocalDateTime time = LocalDateTime.now();
        if (empTFT.getDayOfTheWeek() < finalNow.getDayOfWeek().getValue()) {
            time = finalNow.minusDays(finalNow.getDayOfWeek().getValue()).plusDays(7).plusDays(empTFT.getDayOfTheWeek()).withHour(empTFT.getTimeOfTheDay()).withMinute(empTFT.getMinute());
        } else if (empTFT.getDayOfTheWeek() == finalNow.getDayOfWeek().getValue()) {
            if (empTFT.getTimeOfTheDay() > finalNow.getHour() + 12) {
                time = finalNow.withHour(empTFT.getTimeOfTheDay()).withMinute(empTFT.getMinute());
            } else {
                time = finalNow.plusDays(7).withHour(empTFT.getTimeOfTheDay()).withMinute(empTFT.getMinute());
            }

        } else {
            if (empTFT.getDayOfTheWeek() - finalNow.getDayOfWeek().getValue() == 1) {
                if (finalNow.plusHours(12).isBefore(finalNow.plusDays(1).withHour(empTFT.getTimeOfTheDay()))) {
                    time = finalNow.plusDays(1).withHour(empTFT.getTimeOfTheDay()).withMinute(empTFT.getMinute());
                } else {
                    time = finalNow.plusDays(8).withHour(empTFT.getTimeOfTheDay()).withMinute(empTFT.getMinute());
                }
            } else if(status == "STUDENT" || status == "STUDENTINADMIN"){
                time = finalNow.plusDays(empTFT.getDayOfTheWeek() - finalNow.getDayOfWeek().getValue()).withHour(empTFT.getTimeOfTheDay()).withMinute(empTFT.getMinute());
            }
        }
        return time;
    }
    public String manageTimeOfTheLesson(Lesson lesson){
        String timeOfTheLesson = "";
        if (lesson.getLessonTime().getMinute() == 0) {
            timeOfTheLesson += String.format("%02d", lesson.getLessonTime().getDayOfMonth()) + "." + String.format("%02d", lesson.getLessonTime().getMonthValue()) + " " + lesson.getLessonTime().getHour() + ":00";
        } else {
            timeOfTheLesson += String.format("%02d", lesson.getLessonTime().getDayOfMonth()) + "." + String.format("%02d", lesson.getLessonTime().getMonthValue()) + " " + lesson.getLessonTime().getHour() + ":" + lesson.getLessonTime().getMinute();
        }
        return timeOfTheLesson;
    }
    public String manageDurationOfTheLesson(Lesson lesson, int i){
        LocalDateTime durationTime = lesson.getLessonTime();
        durationTime = durationTime.plusMinutes(i * 30);
        String durationTimeStr = "";
        if (durationTime.getMinute() == 0) {
            durationTimeStr = String.format("%02d", durationTime.getDayOfMonth()) + "." + String.format("%02d", durationTime.getMonthValue()) + " " + durationTime.getHour() + ":00";
        } else {
            durationTimeStr = String.format("%02d", durationTime.getDayOfMonth()) + "." + String.format("%02d", durationTime.getMonthValue()) + " " + durationTime.getHour() + ":" + durationTime.getMinute();
        }
        return durationTimeStr;
    }
    // Збирає календар викладача: дні, уроки, клітинки продовження тривалих занять і доступні слоти для перенесення.
    // Метод повертає список з weekDays, lessonHashMap і lessonDurations, щоб контролер одразу передав їх у Thymeleaf.
    public List<Object> compileLessonsForTeacher(int teacherId) {
        String hashKey = "lessonHashMapForTecherById" + teacherId;
        String durKey = "lessonDurationsForTecherById" + teacherId;
        if (redis.opsForValue().get(hashKey) != null && redis.opsForValue().get(durKey) != null) {
            List<Object> retValue = new ArrayList<>();
            retValue.add(redis.opsForValue().get(hashKey));
            retValue.add(redis.opsForValue().get(durKey));
            return retValue;
        }

        HashMap<String, LessonAdminLessonsDTO> lessonDurations = new HashMap<>();
        Teacher teacher = teacherService.getById(teacherId);

        List<Lesson> teacherLessons1 = teacher.getLessons();
        HashMap<String, LessonAdminLessonsDTO> lessonHashMap = new HashMap<>();
        for (Lesson lesson : teacherLessons1) {

            lessonHashMap.put( manageTimeOfTheLesson(lesson), lessonMapper.mapLessonToLessonAdminLessonsDTO(lesson));

            for (int i = 1; i < lesson.getDuration() * 2; i++) {
                lessonDurations.put(manageDurationOfTheLesson(lesson, i), lessonMapper.mapLessonToLessonAdminLessonsDTO(lesson));
            }
        }
        //////////////////
        LocalDateTime now2 = LocalDateTime.now().withMinute(0).withSecond(0);

        final LocalDateTime[] t = {now2};
        List<LessonAdminLessonsDTO> less = new ArrayList<>();
        lessonHashMap.values().stream().forEach(val -> less.add(val)); ////////////////////

/////////
        final List<LessonAdminLessonsDTO>[] teacherLessons = new List[]{less};
        HashMap<LocalDateTime, TimeOfTheWeekAdminLessonsDTO> teacherfreeTimes = new HashMap<>();
        for (int i = 0; i < 3; i++) {
            LocalDateTime finalNow = now2;
            tswService.getAll().stream().forEach(empTFT -> {
                t[0] = manageOneTime(empTFT, finalNow, "TEACHER");
                teacherfreeTimes.put(t[0], UniversalMapper.generalMapper(empTFT, TimeOfTheWeekAdminLessonsDTO.class));

            });
            now2 = now2.plusWeeks(1);
        }


        Map<LocalDateTime, Object> sortedFreeTimes = new TreeMap<>(teacherfreeTimes);
        lessonHashMap.values().stream().forEach(val -> val.getTeacher().setNotTakenTimes(objectMapper.writeValueAsString(sortedFreeTimes)));
        lessonDurations.values().stream().forEach(val -> val.getTeacher().setNotTakenTimes(objectMapper.writeValueAsString(sortedFreeTimes)));

        ///////////

        //////////////////
        List<Object> retValue = new ArrayList<>();
        retValue.add(lessonHashMap);
        retValue.add(lessonDurations);
        redis.opsForValue().set(hashKey, lessonHashMap);
        redis.opsForValue().set(durKey, lessonDurations);
        return retValue;
    }

    // Збирає календар тільки для однієї пари викладач-студент.
    // Він фільтрує Lesson цієї пари, додає доступні часи викладача і повертає структури для teacherStudentLessons/studentLessons.
    public List<Object> compileLessonsForStudentAndTeacher(int idT, int idSt) {
        String hashKey = "lessonHashMapForTecherAndStudent"+idT+"_"+idSt;
        String durKey = "lessonDurationsForTecherAndStudent"+idT+"_"+idSt;
        if (redis.opsForValue().get(hashKey) != null && redis.opsForValue().get(durKey) != null) {
            List<Object> retValue = new ArrayList<>();
            retValue.add(redis.opsForValue().get(hashKey));
            retValue.add(redis.opsForValue().get(durKey));
            return retValue;
        }

        HashMap<String, LessonAdminLessonsDTO> lessonDurations = new HashMap<>();
        List<Lesson> teacherLessons1 = findAllByIdTandIdSt(idT, idSt);
        HashMap<String, LessonAdminLessonsDTO> lessonHashMap = new HashMap<>();
        for (Lesson lesson : teacherLessons1) {
            lessonHashMap.put( manageTimeOfTheLesson(lesson), lessonMapper.mapLessonToLessonAdminLessonsDTO(lesson));

            for (int i = 1; i < lesson.getDuration() * 2; i++) {
                lessonDurations.put(manageDurationOfTheLesson(lesson, i), lessonMapper.mapLessonToLessonAdminLessonsDTO(lesson));
            }

        }
        //////////////////
        LocalDateTime now2 = LocalDateTime.now().withMinute(0).withSecond(0);

        final LocalDateTime[] t = {now2};
        List<LessonAdminLessonsDTO> less = new ArrayList<>();
        lessonHashMap.values().stream().forEach(val -> less.add(val)); ////////////////////

/////////
        final List<LessonAdminLessonsDTO>[] teacherLessons = new List[]{new ArrayList()};
        findAllByTeachId(idT).stream().forEach(les -> teacherLessons[0].add(lessonMapper.mapLessonToLessonAdminLessonsDTO(les)));
        HashMap<LocalDateTime, TimeOfTheWeekAdminLessonsDTO> teacherfreeTimes = new HashMap<>();
        for (int i = 0; i < 3; i++) {
            LocalDateTime finalNow = now2;
            tswService.getAll().stream().forEach(empTFT -> {
                t[0] = manageOneTime(empTFT, finalNow, "TEACHERANDSTUDENT");
                teacherfreeTimes.put(t[0], UniversalMapper.generalMapper(empTFT, TimeOfTheWeekAdminLessonsDTO.class));
            });
            now2 = now2.plusWeeks(1);
        }

        Map<LocalDateTime, Object> sortedFreeTimes = new TreeMap<>(teacherfreeTimes);
        lessonHashMap.values().stream().forEach(val -> val.getTeacher().setNotTakenTimes(objectMapper.writeValueAsString(sortedFreeTimes)));
        lessonDurations.values().stream().forEach(val -> val.getTeacher().setNotTakenTimes(objectMapper.writeValueAsString(sortedFreeTimes)));
        ///////////
        //////////////////
        List<Object> retValue = new ArrayList<>();
        retValue.add(lessonHashMap);
        retValue.add(lessonDurations);
        redis.opsForValue().set(hashKey, lessonHashMap);
        redis.opsForValue().set(durKey, lessonDurations);
        return retValue;
    }
    // Збирає календар студента з уроками всіх його викладачів.
    // Для кожного викладача додає notTakenTimes, щоб студент бачив варіанти перенесення заняття.
    public List<Object> compileLessonsForStudent(int stId) {
        String hashKey = "lessonHashMapForStudent"+stId;
        String durKey = "lessonDurationsForStudent"+stId;
        if (redis.opsForValue().get(hashKey) != null && redis.opsForValue().get(durKey) != null) {
            List<Object> retValue = new ArrayList<>();
            retValue.add(redis.opsForValue().get(hashKey));
            retValue.add(redis.opsForValue().get(durKey));
            return retValue;
        }

        HashMap<String, LessonAdminLessonsDTO> lessonDurations = new HashMap<>();
        Student student = studentService.getById(stId);
        List<Lesson> studentLessons = student.getLessons();
        HashMap<String, LessonAdminLessonsDTO> lessonHashMap = new HashMap<>();
        for (Lesson lesson : studentLessons) {
            lessonHashMap.put( manageTimeOfTheLesson(lesson), lessonMapper.mapLessonToLessonAdminLessonsDTO(lesson));

            for (int i = 1; i < lesson.getDuration() * 2; i++) {
                lessonDurations.put(manageDurationOfTheLesson(lesson, i), lessonMapper.mapLessonToLessonAdminLessonsDTO(lesson));
            }
        }

//////////////////////////

        LocalDateTime now2 = LocalDateTime.now();

        final LocalDateTime[] t = {now2};
        Set<TeacherAdminLessonsDTO> teachers = new HashSet<>();
        List<LessonAdminLessonsDTO> less = new ArrayList<>();
        lessonHashMap.values().stream().forEach(val -> less.add(val)); ////////////////////
        lessonHashMap.values().stream().forEach(les -> {
            if (!teachers.stream().map(teach -> teach.getId()).toList().contains(les.getTeacher().getId())) {
                teachers.add(les.getTeacher());
            }

        });
        for (TeacherAdminLessonsDTO teacher : teachers) {
            now2 = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
            HashMap<LocalDateTime, TimeOfTheWeekAdminLessonsDTO> teacherfreeTimes = new HashMap<>();
            for (int i = 0; i < 3; i++) {
                LocalDateTime finalNow = now2;
                theWeekService.getTimesOfTheWeekThroughIds(teacherService.getById(teacher.getId()).getFreeTimeIds()).forEach(empTFT -> {
                    t[0] = manageOneTime(empTFT, finalNow, "STUDENT");
                    teacherfreeTimes.put(t[0], UniversalMapper.generalMapper(empTFT, TimeOfTheWeekAdminLessonsDTO.class));
                });
                now2 = now2.plusWeeks(1);
            }


            Map<LocalDateTime, Object> sortedFreeTimes = new TreeMap<>(teacherfreeTimes);
            lessonHashMap.values().stream().filter(les -> les.getTeacher().getId() == teacher.getId()).forEach(val -> val.getTeacher().setNotTakenTimes(objectMapper.writeValueAsString(sortedFreeTimes)));
            lessonDurations.values().stream().filter(les -> les.getTeacher().getId() == teacher.getId()).forEach(val -> val.getTeacher().setNotTakenTimes(objectMapper.writeValueAsString(sortedFreeTimes)));
        }

        List<Object> retValue = new ArrayList<>();
        retValue.add(lessonHashMap);
        retValue.add(lessonDurations);
        redis.opsForValue().set(hashKey, lessonHashMap);
        redis.opsForValue().set(durKey, lessonDurations);
        return retValue;
    }
    // Збирає адмінську версію календаря студента.
    // Структура така сама, як у студентському кабінеті, але дані готуються для адміністративного шаблону.
    public List<Object> compileLessonsForStudentInAdmin(int stId) {
        String hashKey = "lessonHashMapForStudentInAdmin"+stId;
        String durKey = "lessonDurationsForStudentInAdmin"+stId;
        if (redis.opsForValue().get(hashKey) != null && redis.opsForValue().get(durKey) != null) {
            List<Object> retValue = new ArrayList<>();
            retValue.add(redis.opsForValue().get(hashKey));
            retValue.add(redis.opsForValue().get(durKey));
            return retValue;
        }

        HashMap<String, LessonAdminLessonsDTO> lessonDurations = new HashMap<>();
        Student student = studentService.getById(stId);
        List<Lesson> studentLessons = student.getLessons();
        HashMap<String, LessonAdminLessonsDTO> lessonHashMap = new HashMap<>();
        for (Lesson lesson : studentLessons) {
            lessonHashMap.put( manageTimeOfTheLesson(lesson), lessonMapper.mapLessonToLessonAdminLessonsDTO(lesson));

            for (int i = 1; i < lesson.getDuration() * 2; i++) {
                lessonDurations.put(manageDurationOfTheLesson(lesson, i), lessonMapper.mapLessonToLessonAdminLessonsDTO(lesson));
            }
        }


        LocalDateTime now2 = LocalDateTime.now();

        final LocalDateTime[] t = {now2};
        Set<TeacherAdminLessonsDTO> teachers = new HashSet<>();
        List<LessonAdminLessonsDTO> less = new ArrayList<>();
        lessonHashMap.values().stream().forEach(val -> less.add(val)); ////////////////////
        lessonHashMap.values().stream().forEach(les -> {
            if (!teachers.stream().map(teach -> teach.getId()).toList().contains(les.getTeacher().getId())) {
                teachers.add(les.getTeacher());
            }

        });
        for (TeacherAdminLessonsDTO teacher : teachers) {
            now2 = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
            final List<LessonAdminLessonsDTO>[] teacherLessons = new List[]{less.stream().filter(lesss -> lesss.getTeacher().equals(teacher)).toList()};
            HashMap<LocalDateTime, TimeOfTheWeekAdminLessonsDTO> teacherfreeTimes = new HashMap<>();
            for (int i = 0; i < 3; i++) {
                LocalDateTime finalNow = now2;
                theWeekService.getTimesOfTheWeekThroughIds(teacherService.getById(teacher.getId()).getFreeTimeIds()).stream().forEach(empTFT -> {
                    t[0] = manageOneTime(empTFT, finalNow, "STUDENTINADMIN");
                    teacherfreeTimes.put(t[0], UniversalMapper.generalMapper(empTFT, TimeOfTheWeekAdminLessonsDTO.class));
                });
                now2 = now2.plusWeeks(1);
            }

            Map<LocalDateTime, Object> sortedFreeTimes = new TreeMap<>(teacherfreeTimes);
            lessonHashMap.values().stream().filter(les -> les.getTeacher().getId() == teacher.getId()).forEach(val -> val.getTeacher().setNotTakenTimes(objectMapper.writeValueAsString(sortedFreeTimes)));
            lessonDurations.values().stream().filter(les -> les.getTeacher().getId() == teacher.getId()).forEach(val -> val.getTeacher().setNotTakenTimes(objectMapper.writeValueAsString(sortedFreeTimes)));
        }

        List<Object> retValue = new ArrayList<>();
        retValue.add(lessonHashMap);
        retValue.add(lessonDurations);
        redis.opsForValue().set(hashKey, lessonHashMap);
        redis.opsForValue().set(durKey, lessonDurations);
        return retValue;
    }



    // Перевіряє, чи новий інтервал уроку перетинається з уроками викладача або студента.
    // Метод рахує кінець заняття з duration, порівнює часові проміжки і пропускає урок, який зараз редагується.
    public boolean checkIfLessonOverlap(int tId, int sId, int lId, float dur, String date) {
        /////////////

        LocalDateTime newDate = LocalDateTime.now();
        if (LocalDateTime.now().getMonth().getValue() == 12 && Integer.parseInt(date.split(" ")[1].split("\\.")[1]) == 1) {
            newDate = LocalDateTime.now().withYear(LocalDateTime.now().getYear() + 1).withMonth(Integer.parseInt(date.split(" ")[1].split("\\.")[1])).withDayOfMonth(Integer.parseInt(date.split(" ")[1].split("\\.")[0])).withHour(Integer.parseInt(date.split(" ")[2].split(":")[0])).withMinute(Integer.parseInt(date.split(" ")[2].split(":")[1])).withSecond(0).withNano(0);
        } else {
            newDate = LocalDateTime.now().withYear(LocalDateTime.now().getYear()).withMonth(Integer.parseInt(date.split(" ")[1].split("\\.")[1])).withDayOfMonth(Integer.parseInt(date.split(" ")[1].split("\\.")[0])).withHour(Integer.parseInt(date.split(" ")[2].split(":")[0])).withMinute(Integer.parseInt(date.split(" ")[2].split(":")[1])).withSecond(0).withNano(0);
        }

        LocalDateTime time = newDate;
        LocalDateTime lessonFinish = time;
        if (dur == (long) dur) {
            lessonFinish = lessonFinish.plusHours((long) dur);
        } else {
            lessonFinish = lessonFinish.plusHours((long) dur).plusMinutes(30);
        }

        /////////////////

        AtomicBoolean overlap = new AtomicBoolean(false);
        List<Lesson> teacherLessons = findAllByTeacherIdAndLessonTimeAfterAndLessonTimeBefore(tId, newDate.minusHours(4), newDate.plusHours(4));
        LocalDateTime finalLessonFinish = lessonFinish;
        LocalDateTime finalTime = time;
        teacherLessons.stream().filter(les -> les.getId() != lId).forEach(l -> {
            LocalDateTime lFinish = l.getLessonTime();
            float lDur = l.getDuration();
            if (lDur == (long) lDur) {
                lFinish = lFinish.plusHours((long) lDur);
            } else {
                lFinish = lFinish.plusHours((long) lDur).plusMinutes(30);
            }
            if ((l.getLessonTime().isAfter(finalTime) && l.getLessonTime().isBefore(finalLessonFinish)) || (lFinish.isAfter(finalTime) && finalTime.isAfter(l.getLessonTime())) || l.getLessonTime().truncatedTo(ChronoUnit.MINUTES).equals(finalTime.truncatedTo(ChronoUnit.MINUTES))) {
                overlap.set(true);
            }
        });

////////////////////////////////////
        List<Lesson> studentLessons = findAllByStudentIdAndLessonTimeAfterAndLessonTimeBefore(sId, newDate.minusHours(4), newDate.plusHours(4));
        studentLessons.stream().filter(les -> les.getId() != lId).forEach(l -> {
            LocalDateTime lFinish = l.getLessonTime();
            float lDur = l.getDuration();
            if (lDur == (long) lDur) {
                lFinish = lFinish.plusHours((long) lDur);
            } else {
                lFinish = lFinish.plusHours((long) lDur).plusMinutes(30);
            }
            if ((l.getLessonTime().isAfter(finalTime) && l.getLessonTime().isBefore(finalLessonFinish)) || (lFinish.isAfter(finalTime) && finalTime.isAfter(l.getLessonTime())) || l.getLessonTime().truncatedTo(ChronoUnit.MINUTES).equals(finalTime.truncatedTo(ChronoUnit.MINUTES))) {
                overlap.set(true);
            }
        });
        return overlap.get();
    }

    // Перевіряє, чи можна створити або перенести урок на вибраний день і час.
    // Повертає конкретний Statuses: минула дата, занадто пізно, перетин, не вибраний учасник, курс або тривалість.
    public Statuses checkIfLessonValid(int tId, int sId, int lId, int cId, float dur, String date) {
        LocalDateTime newDate = LocalDateTime.now();
        if (LocalDateTime.now().getMonth().getValue() == 12 && Integer.parseInt(date.split(" ")[1].split("\\.")[1]) == 1) {
            newDate = LocalDateTime.now().withYear(LocalDateTime.now().getYear() + 1).withMonth(Integer.parseInt(date.split(" ")[1].split("\\.")[1])).withDayOfMonth(Integer.parseInt(date.split(" ")[1].split("\\.")[0])).withHour(Integer.parseInt(date.split(" ")[2].split(":")[0])).withMinute(Integer.parseInt(date.split(" ")[2].split(":")[1])).withSecond(0).withNano(0);
        } else {
            newDate = LocalDateTime.now().withYear(LocalDateTime.now().getYear()).withMonth(Integer.parseInt(date.split(" ")[1].split("\\.")[1])).withDayOfMonth(Integer.parseInt(date.split(" ")[1].split("\\.")[0])).withHour(Integer.parseInt(date.split(" ")[2].split(":")[0])).withMinute(Integer.parseInt(date.split(" ")[2].split(":")[1])).withSecond(0).withNano(0);
        }
        LocalDateTime lessonFinish = newDate;
        if (dur == (long) dur) {
            lessonFinish = lessonFinish.plusHours((long) dur);
        } else {
            lessonFinish = lessonFinish.plusHours((long) dur).plusMinutes(30);
        }
        //////////////
        if (newDate.isBefore(LocalDateTime.now())) {
            return Statuses.LESSON_BEFORE_NOW;
        } else if (lessonFinish.isAfter(newDate.withHour(22).withMinute(1))) {
            return Statuses.LESSON_AFTER_ACCEPTED_TIME;
        } else if (checkIfLessonOverlap(tId, sId, lId, dur, date)) {
            return Statuses.LESSON_OVERLAP;
        }else if (lId==0) {
            return Statuses.ITEM_NOT_PICKED;
        }else if (dur==0) {
            return Statuses.ITEM_NOT_PICKED;
        } else if (cId == 0) {
            return Statuses.ITEM_NOT_PICKED;
        } else if (sId == 0) {
            return Statuses.STUDENT_NOT_FOUND;
        }else if (tId == 0) {
            return Statuses.TEACHER_NOT_FOUND;
        } else if (date == "") {
            return Statuses.ITEM_NOT_PICKED;
        }else if (studentService.getById(sId)==null) {
            return Statuses.STUDENT_NOT_FOUND;
        } else if (teacherService.getById(sId)==null) {
            return Statuses.TEACHER_NOT_FOUND;
        }else if (courseService.getById(sId)==null) {
            return Statuses.COURSE_NOT_FOUND;
        }else {
            return Statuses.SUCCESS;
        }

    }

    // Перевіряє серію повторюваних уроків на кілька тижнів вперед.
    // Для кожного повторення викликає checkIfLessonValid і повертає перший статус помилки або SUCCESS разом з останньою перевіреною датою.
    public List<Object> checkIfLessonValidWithReputitions(int tId, int sId, int cId, float dur, String date, int repetitions) {
    // Для кожного повторення він викликає checkIfLessonValid і повертає перший статус помилки або SUCCESS.
        return checkIfLessonValidWithReputitions(tId, sId, cId, -1, dur, date, repetitions);
    }

    // Перевіряє серію повторюваних уроків на кілька тижнів вперед.
    // Для кожного повторення викликає checkIfLessonValid і повертає перший статус помилки або SUCCESS разом з останньою перевіреною датою.
    public List<Object> checkIfLessonValidWithReputitions(int tId, int sId, int cId, int idLes, float dur, String date, int repetitions) {
        String checkingDate = date;
        LocalDateTime time = LocalDateTime.now();
        if (LocalDateTime.now().getMonth().getValue() == 12 && Integer.parseInt(date.split(" ")[1].split("\\.")[1]) == 1) {
            time = LocalDateTime.now().withYear(LocalDateTime.now().getYear() + 1).withMonth(Integer.parseInt(date.split(" ")[1].split("\\.")[1])).withDayOfMonth(Integer.parseInt(date.split(" ")[1].split("\\.")[0])).withHour(Integer.parseInt(date.split(" ")[2].split(":")[0])).withMinute(Integer.parseInt(date.split(" ")[2].split(":")[1])).withSecond(0).withNano(0);
        } else {
            time = LocalDateTime.now().withYear(LocalDateTime.now().getYear()).withMonth(Integer.parseInt(date.split(" ")[1].split("\\.")[1])).withDayOfMonth(Integer.parseInt(date.split(" ")[1].split("\\.")[0])).withHour(Integer.parseInt(date.split(" ")[2].split(":")[0])).withMinute(Integer.parseInt(date.split(" ")[2].split(":")[1])).withSecond(0).withNano(0);
        }
        Statuses status = Statuses.GENERAL;
        for (int i = 0; i < repetitions; i++) {
            LocalDateTime checkingTime = time.plusWeeks(i);
            checkingDate = checkingTime.getDayOfWeek() + " " + checkingTime.getDayOfMonth() + "." + checkingTime.getMonth().getValue() + " " + checkingTime.getHour() + ":" + checkingTime.getMinute();
            Statuses checkingSt = checkIfLessonValid(tId, sId, idLes, cId, dur, checkingDate);
            if (checkingSt == Statuses.SUCCESS) {
                status = checkingSt;
            } else {
                List<Object> retValues = new ArrayList<>();
                retValues.add(checkingSt);
                retValues.add(time);
                return retValues;
            }
        }
        List<Object> retValues = new ArrayList<>();
        retValues.add(status);
        retValues.add(time);
        return retValues;
    }
}
