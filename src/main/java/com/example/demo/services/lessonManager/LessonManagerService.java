package com.example.demo.services.lessonManager;

import com.example.demo.models.entities.Student;
import com.example.demo.models.entities.Teacher;
import com.example.demo.models.other.TimeOfTheWeek;
import com.example.demo.models.programe.Course;
import com.example.demo.models.programe.Lesson;
import com.example.demo.models.thirdTables.TeacherStudentTimeOfTheWeek;
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

@Service
public class LessonManagerService {
    private TeacherStudentTimeOfTheWeekService tswService;
    private LessonService lessonService;

    public LessonManagerService(TeacherStudentTimeOfTheWeekService tswService, LessonService lessonService) {
        this.tswService = tswService;
        this.lessonService = lessonService;
    }

//    @Scheduled(cron = "0 0 7,22 * * *")
    @Scheduled( fixedRate = 300000)
    @Async
    public void manageGenerateLessons(){
        LocalDateTime now = LocalDateTime.now();
        List<TeacherStudentTimeOfTheWeek> schedulesForLessons = tswService.getAll();
        for (TeacherStudentTimeOfTheWeek tsw : schedulesForLessons){
            Teacher teacher = tsw.getTeacher();
            Student student = tsw.getStudent();
            TimeOfTheWeek timeOfTheWeek = tsw.getTimeOfTheWeek();
            LocalDateTime time = now.minusDays(now.getDayOfWeek().getValue()).plusDays(tsw.getTimeOfTheWeek().getDayOfTheWeek()).withHour(tsw.getTimeOfTheWeek().getTimeOfTheDay()).withMinute(0).withSecond(0).withNano(0);
            List<Lesson> tswLessons = lessonService.findAllByTeachIdAndStIdAndWeekId(teacher.getId(), student.getId(), timeOfTheWeek.getId());
            tswLessons =  tswLessons.stream().filter(lesson -> lesson.getStatus().equals("will")).collect(Collectors.toList());
            if(!tswLessons.isEmpty()){
                if (tswLessons.get(tswLessons.size()-1).getLessonTime().isBefore(time.plusWeeks(4).minusDays(time.getDayOfWeek().getValue()).withHour(1))){
                    for (int i = 1; i<=5; i++){
                        if(tswLessons.get(tswLessons.size()-1).getLessonTime().isBefore(time.plusWeeks(5).minusDays(time.getDayOfWeek().getValue()).withHour(1))) {
                            LocalDateTime lessonTime = tswLessons.get(tswLessons.size()-1).getLessonTime().minusDays(tswLessons.get(tswLessons.size()-1).getLessonTime().getDayOfWeek().getValue()).plusDays(tsw.getTimeOfTheWeek().getDayOfTheWeek()).withHour(tsw.getTimeOfTheWeek().getTimeOfTheDay()).plusWeeks(1);
                            Lesson lesson = new Lesson(student, teacher, tswLessons.get(0).getCourse(), lessonTime, 1, tsw.getTimeOfTheWeek(), "will");
                            lessonService.create(lesson);
                            tswLessons.add(lesson);
                        }
                    }

                }
            }else{
                Course course = tsw.getCourse();
                if(now.getDayOfWeek().getValue()>tsw.getTimeOfTheWeek().getDayOfTheWeek()){
                    for(int i1 = 1; i1<=5; i1++){
                        Lesson lesson = new Lesson(student, teacher, course, time.plusWeeks(i1), 1, tsw.getTimeOfTheWeek(), "will");
                        lessonService.create(lesson);
                    }
                }else if(now.getDayOfWeek().getValue()==tsw.getTimeOfTheWeek().getDayOfTheWeek()){
                    if(now.getHour()<tsw.getTimeOfTheWeek().getTimeOfTheDay()){
                        for(int i1 = 0; i1<=4; i1++){
                            Lesson lesson = new Lesson(student, teacher, course, time.plusWeeks(i1), 1, tsw.getTimeOfTheWeek(), "will");
                            lessonService.create(lesson);
                        }
                    }else{
                        for(int i1 = 1; i1<=5; i1++){
                            Lesson lesson = new Lesson(student, teacher, course, time.plusWeeks(i1), 1, tsw.getTimeOfTheWeek(), "will");
                            lessonService.create(lesson);
                        }
                    }

                }else{
                    for(int i1 = 0; i1<=4; i1++){
                        Lesson lesson = new Lesson(student, teacher, course, time.plusWeeks(i1), 1, tsw.getTimeOfTheWeek(), "will");
                        lessonService.create(lesson);
                    }
                }
            }
        }
    }
    //    @Scheduled(cron = "0 0 1,23 * * *")?????
    @Scheduled( fixedRate = 300000)
    @Async
    public void manageLessonStatus(){
        List<Lesson> lessons = lessonService.getAll();
        lessons = lessons.stream().filter(lesson -> lesson.getLessonTime().isBefore(LocalDateTime.now().minusHours(4))).collect(Collectors.toList());
        lessons = lessons.stream().filter(lesson -> lesson.getStatus().equals("will")).collect(Collectors.toList());
        for (Lesson lesson : lessons){
            lesson.setStatus("wasnt");
            lessonService.update(lesson.getId(), lesson);
        }
    }
}
