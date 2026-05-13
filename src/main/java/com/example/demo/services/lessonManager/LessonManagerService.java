package com.example.demo.services.lessonManager;

import com.example.demo.models.entities.Student;
import com.example.demo.models.entities.Teacher;
import com.example.demo.models.other.TimeOfTheWeek;
import com.example.demo.models.programe.Course;
import com.example.demo.models.programe.Lesson;
import com.example.demo.models.thirdTables.TeacherStudentTimeOfTheWeek;
import com.example.demo.services.other.TimeOfTheWeekService;
import com.example.demo.services.program.LessonService;
import com.example.demo.services.thirdTable.TeacherStudentTimeOfTheWeekService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

// Сервіс LessonManagerService містить бізнес-операції для модуля «уроки».
// Контролери звертаються сюди, щоб не працювати напряму з репозиторіями, mapper-ами та правилами розкладу.
@Service
public class LessonManagerService {
    private TeacherStudentTimeOfTheWeekService tswService;
    private LessonService lessonService;
    private TimeOfTheWeekService theWeekService;
    // Отримує сервіси постійного розкладу і уроків для фонової генерації занять.
    // Менеджер не працює з репозиторіями напряму, а створює Lesson через LessonService.
    public LessonManagerService(TeacherStudentTimeOfTheWeekService tswService, LessonService lessonService, TimeOfTheWeekService theWeekService) {
        this.tswService = tswService;
        this.lessonService = lessonService;
        this.theWeekService = theWeekService;
    }

//    @Scheduled(cron = "0 0 7,22 * * *")
    // Фонова задача підтримує запас майбутніх WILL-уроків за записами TeacherStudentTimeOfTheWeek.
    // Для кожної пари викладач-студент вона або продовжує існуючий ланцюжок, або створює перші п’ять занять за курсом і слотом.
    @Scheduled( fixedRate = 300000)
    @Async
    public void manageGenerateLessons(){
        LocalDateTime now = LocalDateTime.now();
        List<TeacherStudentTimeOfTheWeek> schedulesForLessons = tswService.getAll();
        for (TeacherStudentTimeOfTheWeek tsw : schedulesForLessons){
            Teacher teacher = tsw.getTeacher();
            Student student = tsw.getStudent();
            TimeOfTheWeek timeOfTheWeek = theWeekService.getById(tsw.getTimeOfTheWeek());
            // Базовий час уроку в межах поточного тижня.
            LocalDateTime time = now.minusDays(now.getDayOfWeek().getValue()).plusDays(timeOfTheWeek.getDayOfTheWeek()).withHour(timeOfTheWeek.getTimeOfTheDay()).withMinute(0).withSecond(0).withNano(0);
            List<Lesson> tswLessons = lessonService.findAllByTeachIdAndStIdAndWeekId(teacher.getId(), student.getId(), timeOfTheWeek.getId());
            tswLessons =  tswLessons.stream().filter(lesson -> lesson.getStatus().equals("WILL")).collect(Collectors.toList());
            if(!tswLessons.isEmpty()){
                if (tswLessons.get(tswLessons.size()-1).getLessonTime().isBefore(time.plusWeeks(4).minusDays(time.getDayOfWeek().getValue()).withHour(1))){
                    for (int i = 1; i<=5; i++){
                        if(tswLessons.get(tswLessons.size()-1).getLessonTime().isBefore(time.plusWeeks(5).minusDays(time.getDayOfWeek().getValue()).withHour(1))) {
                            LocalDateTime lessonTime = tswLessons.get(tswLessons.size()-1).getLessonTime().minusDays(tswLessons.get(tswLessons.size()-1).getLessonTime().getDayOfWeek().getValue()).plusDays(timeOfTheWeek.getDayOfTheWeek()).withHour(timeOfTheWeek.getTimeOfTheDay()).withMinute(timeOfTheWeek.getMinute()).plusWeeks(1);
                            Lesson lesson = new Lesson(student, teacher, tswLessons.get(0).getCourse(), lessonTime, 1, tsw.getTimeOfTheWeek(), "WILL");
                            lessonService.create(lesson);
                            tswLessons.add(lesson);
                        }
                    }

                }
            }else{
                Course course = tsw.getCourse();
                if(now.getDayOfWeek().getValue()>timeOfTheWeek.getDayOfTheWeek()){
                    for(int i1 = 1; i1<=5; i1++){
                        Lesson lesson = new Lesson(student, teacher, course, time.plusWeeks(i1), 1, tsw.getTimeOfTheWeek(), "WILL");
                        lessonService.create(lesson);
                    }
                }else if(now.getDayOfWeek().getValue()==timeOfTheWeek.getDayOfTheWeek()){
                    if(now.getHour()<timeOfTheWeek.getTimeOfTheDay()){
                        for(int i1 = 0; i1<=4; i1++){
                            Lesson lesson = new Lesson(student, teacher, course, time.plusWeeks(i1), 1, tsw.getTimeOfTheWeek(), "WILL");
                            lessonService.create(lesson);
                        }
                    }else{
                        for(int i1 = 1; i1<=5; i1++){
                            Lesson lesson = new Lesson(student, teacher, course, time.plusWeeks(i1), 1, tsw.getTimeOfTheWeek(), "WILL");
                            lessonService.create(lesson);
                        }
                    }

                }else{
                    for(int i1 = 0; i1<=4; i1++){
                        Lesson lesson = new Lesson(student, teacher, course, time.plusWeeks(i1), 1, tsw.getTimeOfTheWeek(), "WILL");
                        lessonService.create(lesson);
                    }
                }
            }
        }
    }
    //    @Scheduled(cron = "0 0 1,23 * * *")?????
    // Фонова задача переводить прострочені WILL-уроки у статус WASNT.
    // Вона бере всі уроки, залишає ті, що мали відбутися понад 4 години тому, і оновлює їх через LessonService.
    @Scheduled( fixedRate = 300000)
    @Async
    public void manageLessonStatus(){
        List<Lesson> lessons = lessonService.getAll();
        // Прострочені заплановані уроки позначаємо як ті, що не відбулися.
        lessons = lessons.stream().filter(lesson -> lesson.getLessonTime().isBefore(LocalDateTime.now().minusHours(4))).collect(Collectors.toList());
        lessons = lessons.stream().filter(lesson -> lesson.getStatus().equals("WILL")).collect(Collectors.toList());
        for (Lesson lesson : lessons){
            lesson.setStatus("WASNT");
            lessonService.update(lesson.getId(), lesson);
        }
    }
}
