package com.example.demo.services.program;

import com.example.demo.dto.adminLessons.LessonAdminLessonsDTO;
import com.example.demo.dto.adminLessons.TeacherAdminLessonsDTO;
import com.example.demo.dto.adminLessons.TimeOfTheWeekAdminLessonsDTO;
import com.example.demo.mapper.programe.LessonMapper;
import com.example.demo.mapper.univMapper.UniversalMapper;
import com.example.demo.models.programe.Lesson;
import com.example.demo.models.entities.Student;
import com.example.demo.models.entities.Teacher;
import com.example.demo.repositories.program.LessonRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import com.example.demo.services.entities.StudentService;
import com.example.demo.services.other.TimeOfTheWeekService;
import com.example.demo.services.entities.TeacherService;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
public class LessonService {
    private final LessonRepository lessonRepository;
    private LessonMapper lessonMapper;
    private TeacherService teacherService;
    private ObjectMapper objectMapper;
    private TimeOfTheWeekService tswService;
    private StudentService studentService;

    public LessonService(LessonRepository lessonRepository, LessonMapper lessonMapper, TeacherService teacherService, ObjectMapper objectMapper, TimeOfTheWeekService tswService, StudentService studentService) {
        this.lessonRepository = lessonRepository;
        this.lessonMapper = lessonMapper;
        this.teacherService = teacherService;
        this.objectMapper = objectMapper;
        this.tswService = tswService;
        this.studentService = studentService;
    }

    public List<Lesson> getAll() {
        return lessonRepository.findAll();
    }

    public Lesson getById(int id) {
        return lessonRepository.findById(id);
    }
    public void deleteAllLessonsByStudent(Student student){
        lessonRepository.deleteAllByStudent(student);
    }
    public Lesson create(Lesson lesson) {
        return lessonRepository.save(lesson);
    }
    public List<Lesson> findAllByTimeOfTheWeekIdAndTeacherId(int timeOfWeekId, int teachId){
        return lessonRepository.findAllByTimeOfTheWeekIdAndTeacherId(timeOfWeekId, teachId);
    }
    public boolean checkIfExistsByLessonTime(LocalDateTime time){
        return lessonRepository.findByLessonTime(time) != null;
    }
    public Lesson update(Integer id, Lesson lesson) {
        Lesson existing = getById(id);
        existing.setStudent(lesson.getStudent());
        existing.setTeacher(lesson.getTeacher());
        existing.setCourse(lesson.getCourse());
        existing.setLessonTime(lesson.getLessonTime());
        existing.setStatus(lesson.getStatus());
        return lessonRepository.save(existing);
    }
    public boolean check(LocalDateTime time, int id){
        return !lessonRepository.findAllByTeacherIdAndLessonTime(id, time).isEmpty();
    }

    public void deleteById(Integer id) {
        getById(id);
        lessonRepository.deleteById(id);
    }
    public List<Lesson> findAllByTeachId(int id){
        return lessonRepository.findAllByTeacherId(id);
    }
    public Lesson findByStudentIdAndTime(LocalDateTime time, int id){
        return lessonRepository.findByLessonTimeAndStudentId(time, id);
    }
    public List<Lesson> findAllByTeachIdAndStIdAndWeekId(int teachId, int stId, int weekId){
        return lessonRepository.findAllByTeacherIdAndStudentIdAndTimeOfTheWeekId(teachId, stId, weekId);
    }
    public void deleteAllByTimeOfTheWeekId(int id){
        lessonRepository.removeAllByTimeOfTheWeekId(id);
    }
    public void deleteAllByStIdAndTeachIdAndTswIdAndLesTimeAfterNow(int stId, int teachId, int tswId, LocalDateTime now){
        lessonRepository.removeAllByStudentIdAndTeacherIdAndTimeOfTheWeekIdAndLessonTimeAfter(stId, teachId, tswId, now);
    }
    public List<Lesson> findAllByIdTandIdSt(int idT, int idSt){
        return lessonRepository.findAllByTeacherIdAndStudentId(idT, idSt);
    }
    public List<Object>  compileLessonsForStudent(int stId){
        Student student = studentService.getById(stId);
        LocalDate now = LocalDate.now();
        now = now.minusWeeks(2);
        List<String> weekDaysTemp = new ArrayList<>();
        weekDaysTemp.add("ПОН");weekDaysTemp.add("ВІВТ");weekDaysTemp.add("СЕР");weekDaysTemp.add("ЧЕТ");weekDaysTemp.add("ПЯТ");weekDaysTemp.add("СУБ");weekDaysTemp.add("НЕД");
        List<String> weekDays = new ArrayList<>();
        for(int i = 0; i<5;i++){
            for(String weekDay : weekDaysTemp){
                weekDays.add(weekDaysTemp.get(now.getDayOfWeek().getValue()-1)+" "+now.getDayOfMonth()+"."+now.getMonthValue());
                now = now.plusDays(1);
            }
        }
        List<Lesson> studentLessons = student.getLessons();
        HashMap<String, LessonAdminLessonsDTO> lessonHashMap = new HashMap<>();
        for(Lesson lesson : studentLessons){
            String timeOfTheLesson = "";
            timeOfTheLesson+=lesson.getLessonTime().getDayOfMonth()+"."+lesson.getLessonTime().getMonthValue()+" "+lesson.getLessonTime().getHour();
            lessonHashMap.put(timeOfTheLesson, lessonMapper.mapLessonToLessonAdminLessonsDTO(lesson));
        }

//////////////////////////

        LocalDateTime now2 = LocalDateTime.now();

        final LocalDateTime[] t = {now2};
        Set<TeacherAdminLessonsDTO> teachers =new HashSet<>();
        List<LessonAdminLessonsDTO> less = new ArrayList<>();
        lessonHashMap.values().stream().forEach(val->less.add(val)); ////////////////////
        lessonHashMap.values().stream().forEach(les->teachers.add(les.getTeacher()));
        for(TeacherAdminLessonsDTO teacher : teachers){

            final List<LessonAdminLessonsDTO>[] teacherLessons = new List[]{less.stream().filter(lesss -> lesss.getTeacher().equals(teacher)).toList()};
            HashMap<LocalDateTime,TimeOfTheWeekAdminLessonsDTO> teacherfreeTimes = new HashMap<>();
            teacherService.getById(teacher.getId()).getEmptyTimesForTeachers().stream().forEach(empTFT->{
                if(empTFT.getTime().getDayOfTheWeek()<now2.getDayOfWeek().getValue()){
                    t[0] = now2.minusDays(now2.getDayOfWeek().getValue()).plusDays(7).plusDays(empTFT.getTime().getDayOfTheWeek()).withHour(empTFT.getTime().getTimeOfTheDay());
                }else if(empTFT.getTime().getDayOfTheWeek()==now2.getDayOfWeek().getValue()){
                    if(empTFT.getTime().getTimeOfTheDay()>now2.getHour()+12){
                        t[0] = now2.withHour(empTFT.getTime().getTimeOfTheDay());
                    }else{
                        t[0] = now2.plusDays(7).withHour(empTFT.getTime().getTimeOfTheDay());
                    }

                }else{
                    if(empTFT.getTime().getDayOfTheWeek()-now2.getDayOfWeek().getValue()==1){
                        if(now2.plusHours(12).isBefore(now2.plusDays(1).withHour(empTFT.getTime().getTimeOfTheDay()))){
                            t[0] =now2.plusDays(1).withHour(empTFT.getTime().getTimeOfTheDay());
                        }else{
                            t[0] =now2.plusDays(8).withHour(empTFT.getTime().getTimeOfTheDay());
                        }
                    }else{
                        t[0] = now2.plusDays(empTFT.getTime().getDayOfTheWeek()-now2.getDayOfWeek().getValue()).withHour(empTFT.getTime().getTimeOfTheDay());
                    }
                }
                        teacherfreeTimes.put(t[0], UniversalMapper.generalMapper(empTFT.getTime(),TimeOfTheWeekAdminLessonsDTO.class));
            });
            for (TimeOfTheWeekAdminLessonsDTO notTakenTime : teacherfreeTimes.values().stream().toList()){
                for(LessonAdminLessonsDTO teacherLesson : teacherLessons[0]){
                    if(notTakenTime.getDayOfTheWeek()!=null && notTakenTime.getTimeOfTheDay()!=null){
                        if(notTakenTime.getDayOfTheWeek()==teacherLesson.getLessonTime().getDayOfWeek().getValue()&&notTakenTime.getTimeOfTheDay()==teacherLesson.getLessonTime().getHour()){
                            teacherfreeTimes.values().remove(notTakenTime);
                        }
                    }

                }
            }
            Map<LocalDateTime, Object> sortedFreeTimes = new TreeMap<>(teacherfreeTimes);
            lessonHashMap.values().stream().filter(les->les.getTeacher().equals(teacher)).forEach(val->val.getTeacher().setNotTakenTimes(objectMapper.writeValueAsString(sortedFreeTimes)));
        }

        List<Object> retValue = new ArrayList<>();
        retValue.add(weekDays);
        retValue.add(lessonHashMap);
        return retValue;
    }







    public List<Object>  compileLessonsForStudentAndTeacher(int idT, int idSt){
        Teacher teacher = teacherService.getById(idT);
        LocalDate now = LocalDate.now();
        now = now.minusWeeks(2);
        List<String> weekDaysTemp = new ArrayList<>();
        weekDaysTemp.add("ПОН");weekDaysTemp.add("ВІВТ");weekDaysTemp.add("СЕР");weekDaysTemp.add("ЧЕТ");weekDaysTemp.add("ПЯТ");weekDaysTemp.add("СУБ");weekDaysTemp.add("НЕД");
        List<String> weekDays = new ArrayList<>();
        for(int i = 0; i<5;i++){
            for(String weekDay : weekDaysTemp){
                weekDays.add(weekDaysTemp.get(now.getDayOfWeek().getValue()-1)+" "+now.getDayOfMonth()+"."+now.getMonthValue());
                now = now.plusDays(1);
            }
        }
        List<Lesson> teacherLessons1 = findAllByIdTandIdSt(idT, idSt);
        HashMap<String, LessonAdminLessonsDTO> lessonHashMap = new HashMap<>();
        for(Lesson lesson : teacherLessons1){
            String timeOfTheLesson = "";
            timeOfTheLesson+=lesson.getLessonTime().getDayOfMonth()+"."+lesson.getLessonTime().getMonthValue()+" "+lesson.getLessonTime().getHour();
            lessonHashMap.put(timeOfTheLesson, lessonMapper.mapLessonToLessonAdminLessonsDTO(lesson));
        }
        //////////////////
        LocalDateTime now2 = LocalDateTime.now().withMinute(0).withSecond(0);

        final LocalDateTime[] t = {now2};
        List<LessonAdminLessonsDTO> less = new ArrayList<>();
        lessonHashMap.values().stream().forEach(val->less.add(val)); ////////////////////

/////////
        final List<LessonAdminLessonsDTO>[] teacherLessons = new List[]{new ArrayList()};
        findAllByTeachId(idT).stream().forEach(les->teacherLessons[0].add(lessonMapper.mapLessonToLessonAdminLessonsDTO(les)));
        HashMap<LocalDateTime,TimeOfTheWeekAdminLessonsDTO> teacherfreeTimes = new HashMap<>();
        tswService.getAll().stream().forEach(empTFT->{
            if(empTFT.getDayOfTheWeek()<now2.getDayOfWeek().getValue()){
                t[0] = now2.minusDays(now2.getDayOfWeek().getValue()).plusDays(7).plusDays(empTFT.getDayOfTheWeek()).withHour(empTFT.getTimeOfTheDay());
            }else if(empTFT.getDayOfTheWeek()==now2.getDayOfWeek().getValue()){
                if(empTFT.getTimeOfTheDay()>now2.getHour()){
                    t[0] = now2.withHour(empTFT.getTimeOfTheDay());
                }else{
                    t[0] = now2.plusDays(7).withHour(empTFT.getTimeOfTheDay());

                }

            }else{
                if(empTFT.getDayOfTheWeek()-now2.getDayOfWeek().getValue()==1){
                    t[0] =now2.plusDays(1).withHour(empTFT.getTimeOfTheDay());
                }else{
                    t[0] = now2.plusDays(empTFT.getDayOfTheWeek()-now2.getDayOfWeek().getValue()).withHour(empTFT.getTimeOfTheDay());
                }
            }
            teacherfreeTimes.put(t[0], UniversalMapper.generalMapper(empTFT,TimeOfTheWeekAdminLessonsDTO.class));
        });
        for (TimeOfTheWeekAdminLessonsDTO notTakenTime : teacherfreeTimes.values().stream().toList()){
            for(LessonAdminLessonsDTO teacherLesson : teacherLessons[0]){
                if(notTakenTime.getDayOfTheWeek()!=null && notTakenTime.getTimeOfTheDay()!=null){
                    if(notTakenTime.getDayOfTheWeek()==teacherLesson.getLessonTime().getDayOfWeek().getValue()&&notTakenTime.getTimeOfTheDay()==teacherLesson.getLessonTime().getHour()){
                        teacherfreeTimes.values().remove(notTakenTime);
                    }
                }

            }
        }
        Map<LocalDateTime, Object> sortedFreeTimes = new TreeMap<>(teacherfreeTimes);
        lessonHashMap.values().stream().forEach(val->val.getTeacher().setNotTakenTimes(objectMapper.writeValueAsString(sortedFreeTimes)));
///////////
        //////////////////
        List<Object> retValue = new ArrayList<>();
        retValue.add(weekDays);
        retValue.add(lessonHashMap);
        return retValue;
    }






    public List<Object>  compileLessonsForTeacher(int teacherId){
        Teacher teacher = teacherService.getById(teacherId);
        LocalDate now = LocalDate.now();
        now = now.minusWeeks(2);
        List<String> weekDaysTemp = new ArrayList<>();
        weekDaysTemp.add("ПОН");weekDaysTemp.add("ВІВТ");weekDaysTemp.add("СЕР");weekDaysTemp.add("ЧЕТ");weekDaysTemp.add("ПЯТ");weekDaysTemp.add("СУБ");weekDaysTemp.add("НЕД");
        List<String> weekDays = new ArrayList<>();
        for(int i = 0; i<5;i++){
            for(String weekDay : weekDaysTemp){
                weekDays.add(weekDaysTemp.get(now.getDayOfWeek().getValue()-1)+" "+now.getDayOfMonth()+"."+now.getMonthValue());
                now = now.plusDays(1);
            }
        }
        List<Lesson> teacherLessons1 = teacher.getLessons();
        HashMap<String, LessonAdminLessonsDTO> lessonHashMap = new HashMap<>();
        for(Lesson lesson : teacherLessons1){
            String timeOfTheLesson = "";
            timeOfTheLesson+=lesson.getLessonTime().getDayOfMonth()+"."+lesson.getLessonTime().getMonthValue()+" "+lesson.getLessonTime().getHour();
            lessonHashMap.put(timeOfTheLesson, lessonMapper.mapLessonToLessonAdminLessonsDTO(lesson));
        }
        //////////////////
        LocalDateTime now2 = LocalDateTime.now().withMinute(0).withSecond(0);

        final LocalDateTime[] t = {now2};
        List<LessonAdminLessonsDTO> less = new ArrayList<>();
        lessonHashMap.values().stream().forEach(val->less.add(val)); ////////////////////

/////////
            final List<LessonAdminLessonsDTO>[] teacherLessons = new List[]{less};
            HashMap<LocalDateTime,TimeOfTheWeekAdminLessonsDTO> teacherfreeTimes = new HashMap<>();
            tswService.getAll().stream().forEach(empTFT->{
                if(empTFT.getDayOfTheWeek()<now2.getDayOfWeek().getValue()){
                    t[0] = now2.minusDays(now2.getDayOfWeek().getValue()).plusDays(7).plusDays(empTFT.getDayOfTheWeek()).withHour(empTFT.getTimeOfTheDay());
                }else if(empTFT.getDayOfTheWeek()==now2.getDayOfWeek().getValue()){
                    if(empTFT.getTimeOfTheDay()>now2.getHour()){
                        t[0] = now2.withHour(empTFT.getTimeOfTheDay());
                    }else{
                        t[0] = now2.plusDays(7).withHour(empTFT.getTimeOfTheDay());

                    }

                }else{
                    if(empTFT.getDayOfTheWeek()-now2.getDayOfWeek().getValue()==1){
                        t[0] =now2.plusDays(1).withHour(empTFT.getTimeOfTheDay());
                    }else{
                        t[0] = now2.plusDays(empTFT.getDayOfTheWeek()-now2.getDayOfWeek().getValue()).withHour(empTFT.getTimeOfTheDay());
                    }
                }
                teacherfreeTimes.put(t[0], UniversalMapper.generalMapper(empTFT,TimeOfTheWeekAdminLessonsDTO.class));
            });
            for (TimeOfTheWeekAdminLessonsDTO notTakenTime : teacherfreeTimes.values().stream().toList()){
                for(LessonAdminLessonsDTO teacherLesson : teacherLessons[0]){
                    if(notTakenTime.getDayOfTheWeek()!=null && notTakenTime.getTimeOfTheDay()!=null){
                        if(notTakenTime.getDayOfTheWeek()==teacherLesson.getLessonTime().getDayOfWeek().getValue()&&notTakenTime.getTimeOfTheDay()==teacherLesson.getLessonTime().getHour()){
                            teacherfreeTimes.values().remove(notTakenTime);
                        }
                    }

                }
            }
            Map<LocalDateTime, Object> sortedFreeTimes = new TreeMap<>(teacherfreeTimes);
            lessonHashMap.values().stream().forEach(val->val.getTeacher().setNotTakenTimes(objectMapper.writeValueAsString(sortedFreeTimes)));
///////////
        //////////////////
        List<Object> retValue = new ArrayList<>();
        retValue.add(weekDays);
        retValue.add(lessonHashMap);
        return retValue;
    }




    public List<Object> compileLessonsForStudentInAdmin(int stId){
        Student student = studentService.getById(stId);
        LocalDate now = LocalDate.now();
        now = now.minusWeeks(2);
        List<String> weekDaysTemp = new ArrayList<>();
        weekDaysTemp.add("ПОН");weekDaysTemp.add("ВІВТ");weekDaysTemp.add("СЕР");weekDaysTemp.add("ЧЕТ");weekDaysTemp.add("ПЯТ");weekDaysTemp.add("СУБ");weekDaysTemp.add("НЕД");
        List<String> weekDays = new ArrayList<>();
        for(int i = 0; i<5;i++){
            for(String weekDay : weekDaysTemp){
                weekDays.add(weekDaysTemp.get(now.getDayOfWeek().getValue()-1)+" "+now.getDayOfMonth()+"."+now.getMonthValue());
                now = now.plusDays(1);
            }
        }
        List<Lesson> studentLessons = student.getLessons();
        HashMap<String, LessonAdminLessonsDTO> lessonHashMap = new HashMap<>();
        for(Lesson lesson : studentLessons){
            String timeOfTheLesson = "";
            timeOfTheLesson+=lesson.getLessonTime().getDayOfMonth()+"."+lesson.getLessonTime().getMonthValue()+" "+lesson.getLessonTime().getHour();
            lessonHashMap.put(timeOfTheLesson, lessonMapper.mapLessonToLessonAdminLessonsDTO(lesson));
        }


        LocalDateTime now2 = LocalDateTime.now();

        final LocalDateTime[] t = {now2};
        Set<TeacherAdminLessonsDTO> teachers =new HashSet<>();
        List<LessonAdminLessonsDTO> less = new ArrayList<>();
        lessonHashMap.values().stream().forEach(val->less.add(val)); ////////////////////
        lessonHashMap.values().stream().forEach(les->teachers.add(les.getTeacher()));
        for(TeacherAdminLessonsDTO teacher : teachers){

            final List<LessonAdminLessonsDTO>[] teacherLessons = new List[]{less.stream().filter(lesss -> lesss.getTeacher().equals(teacher)).toList()};
            HashMap<LocalDateTime,TimeOfTheWeekAdminLessonsDTO> teacherfreeTimes = new HashMap<>();
            teacherService.getById(teacher.getId()).getEmptyTimesForTeachers().stream().forEach(empTFT->{
                if(empTFT.getTime().getDayOfTheWeek()<now2.getDayOfWeek().getValue()){
                    t[0] = now2.minusDays(now2.getDayOfWeek().getValue()).plusDays(7).plusDays(empTFT.getTime().getDayOfTheWeek()).withHour(empTFT.getTime().getTimeOfTheDay());
                }else if(empTFT.getTime().getDayOfTheWeek()==now2.getDayOfWeek().getValue()){
                    if(empTFT.getTime().getTimeOfTheDay()>now2.getHour()){
                        t[0] = now2.withHour(empTFT.getTime().getTimeOfTheDay());
                    }else{
                        t[0] = now2.plusDays(7).withHour(empTFT.getTime().getTimeOfTheDay());
                    }

                }else{
                    if(empTFT.getTime().getDayOfTheWeek()-now2.getDayOfWeek().getValue()==1){
                        if(now2.plusHours(12).isBefore(now2.plusDays(1).withHour(empTFT.getTime().getTimeOfTheDay()))){
                            t[0] =now2.plusDays(1).withHour(empTFT.getTime().getTimeOfTheDay());
                        }else{
                            t[0] =now2.plusDays(8).withHour(empTFT.getTime().getTimeOfTheDay());
                        }
                    }else{
                        t[0] = now2.plusDays(empTFT.getTime().getDayOfTheWeek()-now2.getDayOfWeek().getValue()).withHour(empTFT.getTime().getTimeOfTheDay());
                    }
                }
                teacherfreeTimes.put(t[0], UniversalMapper.generalMapper(empTFT.getTime(),TimeOfTheWeekAdminLessonsDTO.class));
            });
            for (TimeOfTheWeekAdminLessonsDTO notTakenTime : teacherfreeTimes.values().stream().toList()){
                for(LessonAdminLessonsDTO teacherLesson : teacherLessons[0]){
                    if(notTakenTime.getDayOfTheWeek()!=null && notTakenTime.getTimeOfTheDay()!=null){
                        if(notTakenTime.getDayOfTheWeek()==teacherLesson.getLessonTime().getDayOfWeek().getValue()&&notTakenTime.getTimeOfTheDay()==teacherLesson.getLessonTime().getHour()){
                            teacherfreeTimes.values().remove(notTakenTime);
                        }
                    }

                }
            }
            Map<LocalDateTime, Object> sortedFreeTimes = new TreeMap<>(teacherfreeTimes);
            lessonHashMap.values().stream().filter(les->les.getTeacher().equals(teacher)).forEach(val->val.getTeacher().setNotTakenTimes(objectMapper.writeValueAsString(sortedFreeTimes)));
        }

        List<Object> retValue = new ArrayList<>();
        retValue.add(weekDays);
        retValue.add(lessonHashMap);
        return retValue;
    }
}
