package com.example.demo.controlers.closedTeacher.teacher;

import com.example.demo.dto.adminLessons.LessonAdminLessonsDTO;
import com.example.demo.mapper.entity.StudentMapper;
import com.example.demo.mapper.entity.TeacherMapper;
import com.example.demo.mapper.programe.CourseMapper;
import com.example.demo.mapper.programe.LessonMapper;
import com.example.demo.models.email.Mail;
import com.example.demo.models.entities.Teacher;
import com.example.demo.models.other.TimeOfTheWeek;
import com.example.demo.models.programe.Course;
import com.example.demo.models.programe.Lesson;
import com.example.demo.models.thirdTables.TeacherStudentTimeOfTheWeek;
import com.example.demo.services.Statuses;
import com.example.demo.services.email.MailService;
import com.example.demo.services.entities.StudentService;
import com.example.demo.services.entities.TeacherService;
import com.example.demo.services.other.TimeOfTheWeekService;
import com.example.demo.services.program.CourseService;
import com.example.demo.services.program.LessonService;
import com.example.demo.services.thirdTable.EmptyTimesForTeacherService;
import com.example.demo.services.thirdTable.TeacherCourseService;
import com.example.demo.services.thirdTable.TeacherStudentTimeOfTheWeekService;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

// Контролер TeacherLessonsController обслуговує веб-сторінки модуля «уроки».
// Методи нижче приймають параметри з URL або форм, викликають сервіси проекту і повертають потрібні Thymeleaf-шаблони чи redirect-и.
@Controller
@RequestMapping("/teacher")
public class TeacherLessonsController {
    private TeacherService teacherService;
    private LessonMapper lessonMapper;
    private TimeOfTheWeekService theWeekService;
    private CourseService courseService;
    private EmptyTimesForTeacherService emptyTimesForTeacherService;
    private LessonService lessonService;
    private TeacherCourseService teacherCourseService;
    private MailService mailService;
    private TeacherStudentTimeOfTheWeekService tswService;
    private StudentService studentService;
    private StudentMapper studentMapper;
    private TeacherMapper teacherMapper;
    private CourseMapper courseMapper;
    // Отримує через Spring залежності TeacherService, LessonMapper, TimeOfTheWeekService, CourseService, EmptyTimesForTeacherService, LessonService, TeacherCourseService та інші.
    // Ці сервіси й mapper-и потрібні методам класу для роботи з модулем «уроки» без ручного створення об’єктів.
    public TeacherLessonsController(TeacherService teacherService, LessonMapper lessonMapper, TimeOfTheWeekService theWeekService, CourseService courseService, EmptyTimesForTeacherService emptyTimesForTeacherService, LessonService lessonService, TeacherCourseService teacherCourseService, MailService mailService, TeacherStudentTimeOfTheWeekService tswService, StudentService studentService, StudentMapper studentMapper, TeacherMapper teacherMapper, CourseMapper courseMapper) {
        this.teacherService = teacherService;
        this.lessonMapper = lessonMapper;
        this.theWeekService = theWeekService;
        this.courseService = courseService;
        this.emptyTimesForTeacherService = emptyTimesForTeacherService;
        this.lessonService = lessonService;
        this.teacherCourseService = teacherCourseService;
        this.mailService = mailService;
        this.tswService = tswService;
        this.studentService = studentService;
        this.studentMapper = studentMapper;
        this.teacherMapper = teacherMapper;
        this.courseMapper = courseMapper;
    }
    // Відкриває маршрут GET /{id}/lessons і готує дані для шаблону "closedTeacher/teacherLessons/lessons".
    // У Model додає "lessonDurations", "lessons", "students", "courses", "weekDays", "teacher" та інші; дані бере через `teacherService.getById`, `lessonService.compileLessonsForTeacher`, `studentService.getAll`, `studentMapper.mapStudentToStudentDTO`, `courseMapper.mapCourseToCourseDTO` та інші.
    @GetMapping("/{id}/lessons")
    public String gotoLessons(@PathVariable("id") int id, Model model) {
        Teacher teacher = teacherService.getById(id);
        List<Object> values = lessonService.compileLessonsForTeacher(teacher.getId());
        List<String> weekDays = theWeekService.compileWeekDays();
        HashMap<String, LessonAdminLessonsDTO> lessonHashMap = (HashMap<String, LessonAdminLessonsDTO>) values.get(0);
        HashMap<String, LessonAdminLessonsDTO> lessonDurations = (HashMap<String, LessonAdminLessonsDTO>) values.get(1);

        List<Course> courses = new ArrayList<>();
        teacher.getTeacherCourses().stream().forEach(course -> courses.add(course.getCourse()));

        model.addAttribute("lessonDurations", lessonDurations);
        model.addAttribute("lessons", lessonHashMap);
        model.addAttribute("students", studentService.getAll().stream().map(el -> studentMapper.mapStudentToStudentDTO(el)).collect(Collectors.toList()));
        model.addAttribute("courses", courses.stream().map(el -> courseMapper.mapCourseToCourseDTO(el)).collect(Collectors.toList()));
        model.addAttribute("weekDays", weekDays);
        model.addAttribute("teacher", teacherMapper.mapTeacherToTeacherDTO(teacher));
        model.addAttribute("hours", Arrays.asList("8:00", "8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30"));
        return "closedTeacher/teacherLessons/lessons";
    }
    // Оновлює дані за маршрутом POST /{idTeach}/lessons/editLesson/notPicked у модулі «уроки».
    // Після завершення повертає "redirect:/teacher/".
    @PostMapping("/{idTeach}/lessons/editLesson/notPicked")
    public String editLessoneFromTeacherWithNotPicked(@PathVariable("idTeach") int idTeach) {
        return "redirect:/teacher/" + idTeach + "/lessons/ITEM_NOT_PICKED";
    }
    // Відкриває маршрут GET /{id}/lessons/{output} і готує дані для шаблону "closedTeacher/teacherLessons/lessons".
    // У Model додає "lessonDurations", "output", "lessons", "students", "courses", "weekDays" та інші; дані бере через `teacherService.getById`, `lessonService.compileLessonsForTeacher`, `studentService.getAll`, `studentMapper.mapStudentToStudentDTO`, `courseMapper.mapCourseToCourseDTO` та інші.
    @GetMapping("/{id}/lessons/{output}")
    public String gotoLessonsWithOutput(@PathVariable("id") int id, @PathVariable("output") String output, Model model) {
        Teacher teacher = teacherService.getById(id);

        List<Object> values = lessonService.compileLessonsForTeacher(teacher.getId());
        List<String> weekDays = theWeekService.compileWeekDays();
        HashMap<String, LessonAdminLessonsDTO> lessonHashMap = (HashMap<String, LessonAdminLessonsDTO>) values.get(0);
        HashMap<String, LessonAdminLessonsDTO> lessonDurations = (HashMap<String, LessonAdminLessonsDTO>) values.get(1);
        List<Course> courses = new ArrayList<>();
        teacher.getTeacherCourses().stream().forEach(course -> courses.add(course.getCourse()));
        model.addAttribute("lessonDurations", lessonDurations);
        model.addAttribute("output", output);
        model.addAttribute("lessons", lessonHashMap);
        model.addAttribute("students", studentService.getAll().stream().map(el -> studentMapper.mapStudentToStudentDTO(el)).collect(Collectors.toList()));
        model.addAttribute("courses", courses.stream().map(el -> courseMapper.mapCourseToCourseDTO(el)).collect(Collectors.toList()));
        model.addAttribute("weekDays", weekDays);
        model.addAttribute("teacher", teacherMapper.mapTeacherToTeacherDTO(teacher));
        model.addAttribute("hours", Arrays.asList("8:00", "8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30"));
        return "closedTeacher/teacherLessons/lessons";
    }
    // Видаляє або від’єднує дані за маршрутом POST /{idTeach}/lessons/deleteLesson/{id} у модулі «уроки».
    // Викликає `lessonService.getById`, `teacherService.getById`, `mailService.sendEmailWithThymeleafToStudentAboutLessonRemoved`, `lessonService.deleteById`, `teacherMapper.mapTeacherToTeacherDTO`; після завершення повертає "redirect:/teacher/".
    @PostMapping("/{idTeach}/lessons/deleteLesson/{id}")
    @Transactional
    public String deleteLesson(@PathVariable("idTeach") int idTeach, @PathVariable("id") int idLes) throws MessagingException {
        Mail mail = new Mail();
        mail.setTo(Collections.singletonList(lessonService.getById(idLes).getStudent().getEmail()));
        mail.setSubject("Лист про відміну заняття");
        mail.setBody("");
        Teacher teacher = teacherService.getById(idTeach);
        mailService.sendEmailWithThymeleafToStudentAboutLessonRemoved(mail, teacherMapper.mapTeacherToTeacherDTO(teacher), lessonService.getById(idLes).getLessonTime(), lessonService.getById(idLes).getDuration());
        lessonService.deleteById(idLes);
        return "redirect:/teacher/" + idTeach + "/lessons/LESSON_DELETED";
    }
    // Видаляє або від’єднує дані за маршрутом POST /{idTeach}/lessons/deleteCourse/{id} у модулі «уроки».
    // Викликає `lessonService.getById`, `teacherService.getById`, `mailService.sendEmailWithThymeleafToStudentAboutCourseRemoved`, `lessonService.deleteAllByStIdAndTeachIdAndTswIdAndLesTimeAfterNow`, `tswService.removeAllByStIdAndTeachIdAndTswId` та інші; після завершення повертає "redirect:/teacher/".
    @PostMapping("/{idTeach}/lessons/deleteCourse/{id}")
    @Transactional
    public String deleteCourse(@PathVariable("idTeach") int idTeach, @PathVariable("id") int idLes) throws MessagingException {
        Lesson lesson = lessonService.getById(idLes);
        Mail mail = new Mail();
        mail.setTo(Collections.singletonList(lesson.getStudent().getEmail()));
        mail.setSubject("Лист про відміну заняття");
        mail.setBody("");
        Teacher teacher = teacherService.getById(idTeach);
        mailService.sendEmailWithThymeleafToStudentAboutCourseRemoved(mail, teacherMapper.mapTeacherToTeacherDTO(teacher), lesson.getLessonTime(), lesson.getDuration());
        lessonService.deleteAllByStIdAndTeachIdAndTswIdAndLesTimeAfterNow(lesson.getStudent().getId(), idTeach, lesson.getTimeOfTheWeek(), LocalDateTime.now().minusMinutes(30));
        tswService.removeAllByStIdAndTeachIdAndTswId(lesson.getStudent().getId(), idTeach, lesson.getTimeOfTheWeek());
        return "redirect:/teacher/" + idTeach + "/lessons/COURSE_DELETED";
    }

    //    @PostMapping("/{idTeach}/lessons/editLesson/{id}/{nTId}/{date}")
//    public String editLesson(@PathVariable("idTeach") int idTeach, @PathVariable("id") int idLes, @PathVariable("nTId") int newTimeId, @PathVariable("date") String date, Model model) throws MessagingException {
//        TimeOfTheWeek newTime = theWeekService.getById(newTimeId);
//        Lesson lesson = lessonService.getById(idLes);
//        float dur = lesson.getDuration();
//        if (newDate.getDayOfWeek().getValue() == newTime.getDayOfTheWeek()) {
//            if (dur == (long) dur) {
//                lessonFinish = lessonFinish.plusHours((long) dur);
//            } else {
//                lessonFinish = lessonFinish.plusHours((long) dur).plusMinutes(30);
//            }
//            if (lessonFinish.isBefore(time.withHour(22).withMinute(1))) {
//                AtomicBoolean overlap = new AtomicBoolean(false);
//                List<Lesson> teacherLessons = lessonService.findAllByTeachId(idTeach);
//                teacherLessons.stream().filter(les->les.getId()!=lesson.getId()).forEach(l -> {
//                    float lDur = l.getDuration();
//                    if (lDur == (long) lDur) {
//                        lFinish = lFinish.plusHours((long) lDur);
//                    } else {
//                        lFinish = lFinish.plusHours((long) lDur).plusMinutes(30);
//                    }
//                    if ((l.getLessonTime().isAfter(finalTime) && l.getLessonTime().isBefore(finalLessonFinish)) || (lFinish.isAfter(finalTime) && finalTime.isAfter(l.getLessonTime()))) {
//                        overlap.set(true);
//                    }
//                });
//
//
//                List<Lesson> studentLessons = lessonService.findAllByStudentId(lesson.getStudent().getId());
//                studentLessons.stream().filter(les->les.getId()!=lesson.getId()).forEach(l -> {
//                    float lDur = l.getDuration();
//                    if (lDur == (long) lDur) {
//                        lFinish = lFinish.plusHours((long) lDur);
//                    } else {
//                        lFinish = lFinish.plusHours((long) lDur).plusMinutes(30);
//                    }
//                    if ((l.getLessonTime().isAfter(finalTime) && l.getLessonTime().isBefore(finalLessonFinish)) || (lFinish.isAfter(finalTime) && finalTime.isAfter(l.getLessonTime()))) {
//                        overlap.set(true);
//                    }
//                });
//                if (!overlap.get()) {
//                    Mail mail = new Mail();
//                    mail.setTo(Collections.singletonList(lesson.getStudent().getEmail()));
//                    mail.setSubject("Лист про зміну часу одного заняття");
//                    mail.setBody("");
//                    Teacher teacher = teacherService.getById(idTeach);
//                    mailService.sendEmailWithThymeleafToStudentAboutLessonTimeEdited(mail, teacherMapper.mapTeacherToTeacherDTO(teacher), lesson.getLessonTime(), newDate, dur);
//                    lesson.setLessonTime(newDate);
//                    lessonService.update(lesson.getId(), lesson);
//                    return "redirect:/teacher/" + idTeach + "/lessons/LESSON_EDITED";
//                } else {
//                    return "redirect:/teacher/" + idTeach + "/lessons/LESSON_OVERLAP";
//                }
//            } else {
//                return "redirect:/teacher/" + idTeach + "/lessons/LESSON_AFTER_ACCEPTED_TIME";
//            }
//        } else {
//            return "redirect:/teacher/" + idTeach + "/lessons/DATE_INCONSISTENCY";
//        }
//
//    }
    // Оновлює дані за маршрутом POST /{idTeach}/lessons/editLesson/{id}/{nTId}/{date} у модулі «уроки».
    // Викликає `lessonService.getById`, `lessonService.checkIfLessonValidWithReputitions`, `teacherService.getById`, `mailService.sendEmailWithThymeleafToStudentAboutLessonTimeEdited`, `lessonService.update` та інші; працює зі статусами SUCCESS; після завершення повертає "redirect:/teacher/".
    @PostMapping("/{idTeach}/lessons/editLesson/{id}/{nTId}/{date}")
    public String editLesson(@PathVariable("idTeach") int idTeach, @PathVariable("id") int idLes, @PathVariable("date") String date, Model model) throws MessagingException {
        Lesson lesson = lessonService.getById(idLes);
        List<Object> objs = lessonService.checkIfLessonValidWithReputitions(idTeach, lesson.getStudent().getId(),lesson.getCourse().getId(), idLes, lesson.getDuration(), date, 1);
        Statuses status = (Statuses) objs.get(0);
        LocalDateTime newDate = (LocalDateTime) objs.get(1);
        if (status == Statuses.SUCCESS) {
            Mail mail = new Mail();
            mail.setTo(Collections.singletonList(lesson.getStudent().getEmail()));
            mail.setSubject("Лист про зміну часу одного заняття");
            mail.setBody("");
            Teacher teacher = teacherService.getById(idTeach);
            mailService.sendEmailWithThymeleafToStudentAboutLessonTimeEdited(mail, teacherMapper.mapTeacherToTeacherDTO(teacher), lesson.getLessonTime(), newDate, lesson.getDuration());
            lesson.setLessonTime(newDate);
            lessonService.update(lesson.getId(), lesson);
            return "redirect:/teacher/" + idTeach + "/lessons/LESSON_EDITED";
        } else {
            return "redirect:/teacher/" + idTeach + "/lessons/" + status.name();
        }
    }

    // Створює дані за маршрутом POST /{teachId}/lessons/addLesson/{cId}/{stId}/{retDate}/{dur} у модулі «уроки».
    // Викликає `lessonService.checkIfLessonValidWithReputitions`, `studentService.getById`, `teacherService.getById`, `courseService.getById`, `theWeekService.findByDayOfTheWeekAndTimeOfTheDayAndMinute` та інші; працює зі статусами SUCCESS; після завершення повертає "redirect:/teacher/".
    @PostMapping("/{teachId}/lessons/addLesson/{cId}/{stId}/{retDate}/{dur}")
    public String addLesson(@PathVariable("teachId") int idTeach, @PathVariable("cId") int cId, @PathVariable("stId") int stId, @PathVariable("retDate") String date, @PathVariable("dur") float dur, Model model) throws MessagingException {

        List<Object> objs = lessonService.checkIfLessonValidWithReputitions(idTeach, stId, cId, dur, date, 1);
        Statuses status = (Statuses) objs.get(0);
        LocalDateTime time = (LocalDateTime) objs.get(1);

        if (status == Statuses.SUCCESS) {
            Lesson lesson = new Lesson(studentService.getById(stId), teacherService.getById(idTeach), courseService.getById(cId), time, dur, theWeekService.findByDayOfTheWeekAndTimeOfTheDayAndMinute(time.getDayOfWeek().getValue(), time.getHour(), time.getMinute()).getId(), "WILL");
            lessonService.create(lesson);
            Mail mail = new Mail();
            mail.setTo(Collections.singletonList(lesson.getStudent().getEmail()));
            mail.setSubject("Лист про додання одного заняття");
            mail.setBody("");
            Teacher teacher = teacherService.getById(idTeach);
            mailService.sendEmailWithThymeleafToStudentAboutLessonAdded(mail, teacherMapper.mapTeacherToTeacherDTO(teacher), lesson.getLessonTime(), dur);
            return "redirect:/teacher/" + idTeach + "/lessons/LESSON_ADDED";
        } else {
            return "redirect:/teacher/" + idTeach + "/lessons/" + status.name();
        }

    }

    //    @PostMapping("/{teachId}/lessons/addCourse/{cId}/{stId}/{retDate}/{dur}")
//    public String addCourse(@PathVariable("teachId") int idTeach, @PathVariable("cId") int cId, @PathVariable("stId") int stId, @PathVariable("retDate") String date, @PathVariable("dur") float dur, Model model) throws MessagingException {
//        int dayOfMonth = Integer.parseInt(date.split(" ")[1].split("\\.")[0]);
//        int month = Integer.parseInt(date.split(" ")[1].split("\\.")[1]);
//        int hour = Integer.parseInt(date.split(" ")[2].split(":")[0]);
//        int minut = Integer.parseInt(date.split(" ")[2].split(":")[1]);
//        if (time.getMonth().getValue() == 12 && month == 1) {
//            time = time.withYear(time.getYear() + 1).withMonth(month).withDayOfMonth(dayOfMonth).withHour(hour).withMinute(minut).withSecond(0).withNano(0);
//        } else {
//            time = time.withMonth(month).withDayOfMonth(dayOfMonth).withHour(hour).withMinute(minut).withSecond(0).withNano(0);
//        }
//        int tId = theWeekService.findByDayOfTheWeekAndTimeOfTheDayAndMinute(time.getDayOfWeek().getValue(), time.getHour(), time.getMinute()).getId();
//        Statuses status = lessonService.checkIfLessonValid(idTeach, stId, String.valueOf(tId), cId, dur, date);
//
//        if (dur == (long) dur) {
//            lessonFinish = lessonFinish.plusHours((long) dur);
//        } else {
//            lessonFinish = lessonFinish.plusHours((long) dur).plusMinutes(30);
//        }
//        if (lessonFinish.isAfter(time.withHour(22).withMinute(1))) {
//            return "redirect:/teacher/" + idTeach + "/lessons/LESSON_AFTER_ACCEPTED_TIME";
//        }
//        if (time.isAfter(LocalDateTime.now())) {
//            if (cId != 0) {
//                if (stId != 0) {
//                    AtomicBoolean exists = new AtomicBoolean(false);
//                    Lesson lesson = new Lesson();
//                    AtomicBoolean overlap = new AtomicBoolean(false);
//
//                    for (int i = 1; i <= 5; i++) {
//                        for (Lesson les : teacherService.getById(idTeach).getLessons()) {
//
//                            if (les.getLessonTime().truncatedTo(ChronoUnit.MINUTES).isEqual(checkingTime.truncatedTo(ChronoUnit.MINUTES))) {
//                                exists.set(true);
//                            } else {
//                                float lDur = les.getDuration();
//                                if (lDur == (long) lDur) {
//                                    lFinish = lFinish.plusHours((long) lDur);
//                                } else {
//                                    lFinish = lFinish.plusHours((long) lDur).plusMinutes(30);
//                                }
//                                if ((les.getLessonTime().isAfter(finalTime) && les.getLessonTime().isBefore(finalLessonFinish)) || (lFinish.isAfter(finalTime) && finalTime.isAfter(les.getLessonTime()))) {
//                                    overlap.set(true);
//                                }
//                            }
//
//                        }
//                        List<Lesson> studentLessons = lessonService.findAllByStudentId(stId);
//                        studentLessons.stream().forEach(l -> {
//                            float lDur = l.getDuration();
//                            if (lDur == (long) lDur) {
//                                lFinish = lFinish.plusHours((long) lDur);
//                            } else {
//                                lFinish = lFinish.plusHours((long) lDur).plusMinutes(30);
//                            }
//                            if ((l.getLessonTime().isAfter(finalTime) && l.getLessonTime().isBefore(finalLessonFinish)) || (lFinish.isAfter(finalTime) && finalTime.isAfter(l.getLessonTime()))) {
//                                overlap.set(true);
//                            }
//                        });
//                        checkingTime = checkingTime.plusWeeks(1);
//                        lessonFinish = lessonFinish.plusWeeks(1);
//                    }
//                    if (overlap.get()) {
//                        return "redirect:/teacher/" + idTeach + "/lessons/LESSON_OVERLAP";
//                    }
//                    if (exists.get()) {
//                        return "redirect:/teacher/" + idTeach + "/lessons/TIME_NOT_EMPTY";
//                    } else {
//                        for (int i = 1; i <= 4; i++) {
//                            lesson = new Lesson(studentService.getById(stId), teacherService.getById(idTeach), courseService.getById(cId), time, dur, theWeekService.findByDayOfTheWeekAndTimeOfTheDayAndMinute(time.getDayOfWeek().getValue(), time.getHour(), time.getMinute()), "WILL");
//                            lessonService.create(lesson);
//
//                            time = time.plusWeeks(1);
//                        }
//                        TeacherStudentTimeOfTheWeek tsw = new TeacherStudentTimeOfTheWeek(teacherService.getById(idTeach), studentService.getById(stId), theWeekService.findByDayOfTheWeekAndTimeOfTheDayAndMinute(time.getDayOfWeek().getValue(), time.getHour(), time.getMinute()), courseService.getById(cId));
//                        tswService.create(tsw);
//
//                        Mail mail = new Mail();
//                        mail.setTo(Collections.singletonList(studentService.getById(stId).getEmail()));
//                        mail.setSubject("Лист про додання курсу занять");
//                        mail.setBody("");
//                        Teacher teacher = teacherService.getById(idTeach);
//                        mailService.sendEmailWithThymeleafToStudentAboutCourseAdded(mail, teacherMapper.mapTeacherToTeacherDTO(teacher), time, dur);
//                        return "redirect:/teacher/" + idTeach + "/lessons/COURSE_ADDED";
//                    }
//                } else {
//                    return "redirect:/teacher/" + idTeach + "/lessons/STUDENT_NOT_FOUND";
//                }
//            } else {
//                return "redirect:/teacher/" + idTeach + "/lessons/COURSE_NOT_FOUND";
//            }
//
//        } else {
//            return "redirect:/teacher/" + idTeach + "/lessons/LESSON_BEFORE_NOW";
//        }
//    }
    // Створює дані за маршрутом POST /{teachId}/lessons/addCourse/{cId}/{stId}/{retDate}/{dur} у модулі «уроки».
    // Викликає `lessonService.checkIfLessonValidWithReputitions`, `studentService.getById`, `teacherService.getById`, `courseService.getById`, `theWeekService.findByDayOfTheWeekAndTimeOfTheDayAndMinute` та інші; працює зі статусами SUCCESS; після завершення повертає "redirect:/teacher/".
    @PostMapping("/{teachId}/lessons/addCourse/{cId}/{stId}/{retDate}/{dur}")
    public String addCourse(@PathVariable("teachId") int idTeach, @PathVariable("cId") int cId, @PathVariable("stId") int stId, @PathVariable("retDate") String date, @PathVariable("dur") float dur, Model model) throws MessagingException {
        List<Object> objs = lessonService.checkIfLessonValidWithReputitions(idTeach, stId, cId, dur, date, 5);
        Statuses status = (Statuses) objs.get(0);
        LocalDateTime time = (LocalDateTime) objs.get(1);


        if (status == Statuses.SUCCESS) {
            for (int i = 1; i <= 4; i++) {
                Lesson lesson = new Lesson(studentService.getById(stId), teacherService.getById(idTeach), courseService.getById(cId), time, dur, theWeekService.findByDayOfTheWeekAndTimeOfTheDayAndMinute(time.getDayOfWeek().getValue(), time.getHour(), time.getMinute()).getId(), "WILL");
                lessonService.create(lesson);

                time = time.plusWeeks(1);
            }
            TeacherStudentTimeOfTheWeek tsw = new TeacherStudentTimeOfTheWeek(teacherService.getById(idTeach), studentService.getById(stId), theWeekService.findByDayOfTheWeekAndTimeOfTheDayAndMinute(time.getDayOfWeek().getValue(), time.getHour(), time.getMinute()).getId(), courseService.getById(cId));
            tswService.create(tsw);

            Mail mail = new Mail();
            mail.setTo(Collections.singletonList(studentService.getById(stId).getEmail()));
            mail.setSubject("Лист про додання курсу занять");
            mail.setBody("");
            Teacher teacher = teacherService.getById(idTeach);
            mailService.sendEmailWithThymeleafToStudentAboutCourseAdded(mail, teacherMapper.mapTeacherToTeacherDTO(teacher), time, dur);
            return "redirect:/teacher/" + idTeach + "/lessons/COURSE_ADDED";
        } else {
            return "redirect:/teacher/" + idTeach + "/lessons/" + status.name();

        }

    }
    // Створює дані за маршрутом POST /{idTeach}/addMoneyByLesson/{lesId} у модулі «уроки».
    // Викликає `teacherService.getById`, `lessonService.getById`, `teacherService.update`, `lessonService.update`; після завершення повертає "redirect:/teacher/".
    @PostMapping("/{idTeach}/addMoneyByLesson/{lesId}")
    public String addMoney(@PathVariable("idTeach") int teachId, @PathVariable("lesId") int lesId, Model model) {
        Teacher teacher = teacherService.getById(teachId);
        Lesson lesson = lessonService.getById(lesId);
        if (LocalDateTime.now().isAfter(lesson.getLessonTime())) {
            float sum = lesson.getDuration() * lesson.getCourse().getTeacherShare();
            teacher.setUnpaidMoney(teacher.getUnpaidMoney() + sum);
            teacherService.update(teachId, teacher);
            lesson.setStatus("WAS");
            lessonService.update(lesId, lesson);
            return "redirect:/teacher/" + teachId + "/lessons/LESSON_STATUS_CHANGED";
        } else {
            return "redirect:/teacher/" + teachId + "/lessons/TOO_EARLY";
        }
    }

}
