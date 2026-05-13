package com.example.demo.controlers.closedStudent;

import com.example.demo.dto.adminLessons.LessonAdminLessonsDTO;
import com.example.demo.mapper.entity.StudentMapper;
import com.example.demo.mapper.entity.TeacherMapper;
import com.example.demo.mapper.programe.CourseMapper;
import com.example.demo.mapper.programe.LessonMapper;
import com.example.demo.models.email.Mail;
import com.example.demo.models.entities.Student;
import com.example.demo.models.other.TimeOfTheWeek;
import com.example.demo.models.programe.Lesson;
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
import tools.jackson.databind.ObjectMapper;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

// Контролер StudentLessonsController обслуговує веб-сторінки модуля «уроки».
// Методи нижче приймають параметри з URL або форм, викликають сервіси проекту і повертають потрібні Thymeleaf-шаблони чи redirect-и.
@Controller
@RequestMapping("/student")
public class StudentLessonsController {
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
    private ObjectMapper objectMapper;
    private StudentMapper studentMapper;
    private TeacherMapper teacherMapper;
    private CourseMapper courseMapper;
    // Отримує через Spring залежності TeacherService, LessonMapper, TimeOfTheWeekService, CourseService, EmptyTimesForTeacherService, LessonService, TeacherCourseService та інші.
    // Ці сервіси й mapper-и потрібні методам класу для роботи з модулем «уроки» без ручного створення об’єктів.
    public StudentLessonsController(TeacherService teacherService, LessonMapper lessonMapper, TimeOfTheWeekService theWeekService, CourseService courseService, EmptyTimesForTeacherService emptyTimesForTeacherService, LessonService lessonService, TeacherCourseService teacherCourseService, MailService mailService, TeacherStudentTimeOfTheWeekService tswService, StudentService studentService, ObjectMapper objectMapper, StudentMapper studentMapper, TeacherMapper teacherMapper, CourseMapper courseMapper) {
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
        this.objectMapper = objectMapper;
        this.studentMapper = studentMapper;
        this.teacherMapper = teacherMapper;

        this.courseMapper = courseMapper;
    }
    // Відкриває маршрут GET /{idSt}/lessons і готує дані для шаблону "closedStudent/studentLessons/lessons".
    // У Model додає "lessonDurations", "lessons", "weekDays", "student", "hours"; дані бере через `studentService.getById`, `lessonService.compileLessonsForStudent`, `studentMapper.mapStudentToStudentDTO`.
    @GetMapping("/{idSt}/lessons")
    public String gotoLessons(Model model, @PathVariable("idSt") int id) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Student student = studentService.getById(id);

        List<Object> values = lessonService.compileLessonsForStudent(student.getId());
        List<String> weekDays = theWeekService.compileWeekDays();
        HashMap<String, LessonAdminLessonsDTO> lessonHashMap = (HashMap<String, LessonAdminLessonsDTO>) values.get(0);
        HashMap<String, LessonAdminLessonsDTO> lessonDurations = (HashMap<String, LessonAdminLessonsDTO>) values.get(1);


        model.addAttribute("lessonDurations", lessonDurations);
        model.addAttribute("lessons", lessonHashMap);
        model.addAttribute("weekDays", weekDays);
        model.addAttribute("student", studentMapper.mapStudentToStudentDTO(student));
        model.addAttribute("hours", Arrays.asList("8:00", "8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30"));
        return "closedStudent/studentLessons/lessons";
    }
    // Відкриває маршрут GET /{idSt}/lessons/{output} і готує дані для шаблону "closedStudent/studentLessons/lessons".
    // У Model додає "lessonDurations", "lessons", "weekDays", "output", "student", "hours"; дані бере через `studentService.getById`, `lessonService.compileLessonsForStudent`, `studentMapper.mapStudentToStudentDTO`.
    @GetMapping("/{idSt}/lessons/{output}")
    public String gotoLessonsWithOutput(Model model, @PathVariable("idSt") int id, @PathVariable("output") String output) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Student student = studentService.getById(id);

        List<Object> values = lessonService.compileLessonsForStudent(student.getId());
        List<String> weekDays = theWeekService.compileWeekDays();
        HashMap<String, LessonAdminLessonsDTO> lessonHashMap = (HashMap<String, LessonAdminLessonsDTO>) values.get(0);
        HashMap<String, LessonAdminLessonsDTO> lessonDurations = (HashMap<String, LessonAdminLessonsDTO>) values.get(1);


        model.addAttribute("lessonDurations", lessonDurations);
        model.addAttribute("lessons", lessonHashMap);
        model.addAttribute("weekDays", weekDays);
        model.addAttribute("output", output);
        model.addAttribute("student", studentMapper.mapStudentToStudentDTO(student));
        model.addAttribute("hours", Arrays.asList("8:00", "8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30"));
        return "closedStudent/studentLessons/lessons";
    }
    // Видаляє або від’єднує дані за маршрутом POST /{idSt}/lessons/deleteLesson/{id} у модулі «уроки».
    // Викликає `lessonService.getById`, `studentService.getById`, `mailService.sendRequestWithThymeleafTeacherAboutRequestLessonRemoved`, `studentMapper.mapStudentToStudentDTO`; після завершення повертає "redirect:/student/".
    @PostMapping("/{idSt}/lessons/deleteLesson/{id}")
    @Transactional
    public String deleteLesson(@PathVariable("idSt") int idSt, @PathVariable("id") int idLes, Model model) throws MessagingException {
        if (lessonService.getById(idLes).getLessonTime().isAfter(LocalDateTime.now().plusHours(12))) {
            Mail mail = new Mail();
            mail.setTo(Collections.singletonList((lessonService.getById(idLes).getTeacher().getEmail())));
            mail.setSubject("Лист про відміну заняття");
            mail.setBody("");
            Student student = studentService.getById(idSt);
            mailService.sendRequestWithThymeleafTeacherAboutRequestLessonRemoved(mail, studentMapper.mapStudentToStudentDTO(student), lessonService.getById(idLes).getTeacher().getId(), idLes, lessonService.getById(idLes).getLessonTime(), lessonService.getById(idLes).getDuration());

            return "redirect:/student/" + idSt + "/lessons/LESSON_DELETED";
        } else {
            return "redirect:/student/" + idSt + "/lessons/TOO_LATE";
        }

    }
    // Оновлює дані за маршрутом POST /{idSt}/lessons/editLesson/notPicked у модулі «уроки».
    // Після завершення повертає "redirect:/student/".
    @PostMapping("/{idSt}/lessons/editLesson/notPicked")
    @Transactional
    public String manageNotPickedItemInEdit(@PathVariable("idSt") int idSt) {
        return "redirect:/student/" + idSt + "/lessons/ITEM_NOT_PICKED";
    }
    // Видаляє або від’єднує дані за маршрутом POST /{idSt}/lessons/deleteCourse/{id} у модулі «уроки».
    // Викликає `lessonService.getById`, `studentService.getById`, `mailService.sendEmailWithThymeleafTeacherAboutCourseRemoved`, `lessonService.deleteAllByStIdAndTeachIdAndTswIdAndLesTimeAfterNow`, `tswService.removeAllByStIdAndTeachIdAndTswId` та інші; після завершення повертає "redirect:/student/".
    @PostMapping("/{idSt}/lessons/deleteCourse/{id}")
    @Transactional
    public String deleteCourse(@PathVariable("idSt") int idSt, @PathVariable("id") int idLes) throws MessagingException {
        Lesson lesson = lessonService.getById(idLes);
        Mail mail = new Mail();
        mail.setTo(Collections.singletonList(lesson.getTeacher().getEmail()));
        mail.setSubject("Лист про відміну заняття");
        mail.setBody("");
        Student student = studentService.getById(idSt);
        mailService.sendEmailWithThymeleafTeacherAboutCourseRemoved(mail, studentMapper.mapStudentToStudentDTO(student), lesson.getLessonTime(), lesson.getDuration());
        lessonService.deleteAllByStIdAndTeachIdAndTswIdAndLesTimeAfterNow(idSt, lesson.getTeacher().getId(), lesson.getTimeOfTheWeek(), LocalDateTime.now().minusMinutes(30));
        tswService.removeAllByStIdAndTeachIdAndTswId(idSt, lesson.getTeacher().getId(), lesson.getTimeOfTheWeek());
        return "redirect:/student/" + idSt + "/lessons/COURSE_DELETED";
    }

    // Видаляє або від’єднує дані за маршрутом POST /{idSt}/lessons/editLesson/{id}/{nTId}/{date} у модулі «уроки».
    // Викликає `lessonService.getById`, `lessonService.checkIfLessonValidWithReputitions`, `studentService.getById`, `mailService.sendEmailWithThymeleafToTeacherAboutLessonTimeEdited`, `lessonService.update` та інші; працює зі статусами SUCCESS; після завершення повертає "redirect:/student/".
    @PostMapping("/{idSt}/lessons/editLesson/{id}/{nTId}/{date}")
    public String deleteCourse(@PathVariable("idSt") int idSt, @PathVariable("id") int idLes, @PathVariable("date") String date, Model model) throws MessagingException {
        Lesson lesson = lessonService.getById(idLes);
        List<Object> objs = lessonService.checkIfLessonValidWithReputitions(lesson.getTeacher().getId(), lesson.getStudent().getId(), lesson.getCourse().getId(), idLes, lesson.getDuration(), date, 1);
        Statuses status = (Statuses) objs.get(0);
        LocalDateTime newDate = (LocalDateTime) objs.get(1);
        if (status == Statuses.SUCCESS) {
            Mail mail = new Mail();
            mail.setTo(Collections.singletonList(lesson.getTeacher().getEmail()));
            mail.setSubject("Лист про зміну часу одного заняття");
            mail.setBody("");
            Student student = studentService.getById(idSt);
            mailService.sendEmailWithThymeleafToTeacherAboutLessonTimeEdited(mail, studentMapper.mapStudentToStudentDTO(student), lesson.getLessonTime(), newDate, lesson.getDuration());
            lesson.setLessonTime(newDate);
            lessonService.update(lesson.getId(), lesson);
            return "redirect:/student/" + idSt + "/lessons/LESSON_EDITED";
        } else {
            return "redirect:/student/" + idSt + "/lessons/" + status.name();
        }
    }
}
