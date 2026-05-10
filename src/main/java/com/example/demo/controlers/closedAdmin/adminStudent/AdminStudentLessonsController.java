package com.example.demo.controlers.closedAdmin.adminStudent;

import com.example.demo.dto.adminLessons.LessonAdminLessonsDTO;
import com.example.demo.dto.entities.StudentDTO;
import com.example.demo.mapper.entity.StudentMapper;
import com.example.demo.mapper.entity.TeacherMapper;
import com.example.demo.mapper.other.TimeOfTheWeekMapper;
import com.example.demo.mapper.programe.CourseMapper;
import com.example.demo.mapper.programe.LessonMapper;
import com.example.demo.models.email.Mail;
import com.example.demo.models.entities.Student;
import com.example.demo.models.entities.Teacher;
import com.example.demo.models.other.TimeOfTheWeek;
import com.example.demo.models.programe.Course;
import com.example.demo.models.programe.Lesson;
import com.example.demo.services.Statuses;
import com.example.demo.services.email.MailService;
import com.example.demo.services.entities.AdminService;
import com.example.demo.services.entities.StudentService;
import com.example.demo.services.entities.TeacherService;
import com.example.demo.services.other.CommentService;
import com.example.demo.services.other.TimeOfTheWeekService;
import com.example.demo.services.program.CourseService;
import com.example.demo.services.program.LessonService;
import com.example.demo.services.thirdTable.EmptyTimesForTeacherService;
import com.example.demo.services.thirdTable.StudentCourseService;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

// Контролер AdminStudentLessonsController обслуговує веб-сторінки модуля «уроки».
// Методи нижче приймають параметри з URL або форм, викликають сервіси проекту і повертають потрібні Thymeleaf-шаблони чи redirect-и.
@Controller
@RequestMapping("/admin")
public class AdminStudentLessonsController {
    private TeacherCourseService teacherCourseService;
    private TeacherService teacherService;
    private EmptyTimesForTeacherService emptyTimesForTeacherService;

    private TimeOfTheWeekService theWeekService;
    private CourseService courseService;
    private StudentCourseService studentCourseService;
    private CommentService commentService;
    private LessonService lessonService;
    private StudentService studentService;
    private LessonMapper lessonMapper;
    private TeacherStudentTimeOfTheWeekService tswService;
    private MailService mailService;
    private AdminService adminService;
    private TeacherMapper teacherMapper;
    private StudentMapper studentMapper;
    private CourseMapper courseMapper;
    private TimeOfTheWeekMapper theWeekMapper;
    // Отримує через Spring залежності TeacherCourseService, TeacherService, EmptyTimesForTeacherService, TimeOfTheWeekService, CourseService, StudentCourseService, CommentService та інші.
    // Ці сервіси й mapper-и потрібні методам класу для роботи з модулем «уроки» без ручного створення об’єктів.
    public AdminStudentLessonsController(TeacherCourseService teacherCourseService, TeacherService teacherService, EmptyTimesForTeacherService emptyTimesForTeacherService, TimeOfTheWeekService theWeekService, CourseService courseService, StudentCourseService studentCourseService, CommentService commentService, LessonService lessonService, StudentService studentService, LessonMapper lessonMapper, TeacherStudentTimeOfTheWeekService tswService, MailService mailService, AdminService adminService, TeacherMapper teacherMapper, StudentMapper studentMapper, CourseMapper courseMapper, TimeOfTheWeekMapper theWeekMapper) {
        this.teacherCourseService = teacherCourseService;
        this.teacherService = teacherService;
        this.emptyTimesForTeacherService = emptyTimesForTeacherService;
        this.theWeekService = theWeekService;
        this.courseService = courseService;
        this.studentCourseService = studentCourseService;
        this.commentService = commentService;
        this.lessonService = lessonService;
        this.studentService = studentService;
        this.lessonMapper = lessonMapper;
        this.tswService = tswService;
        this.mailService = mailService;
        this.adminService = adminService;
        this.teacherMapper = teacherMapper;
        this.studentMapper = studentMapper;
        this.courseMapper = courseMapper;
        this.theWeekMapper = theWeekMapper;
    }
    // Відкриває маршрут GET /student/{idSt}/lessons і готує дані для шаблону "closedAdmin/adminStudents/studentLessons".
    // У Model додає "lessonDurations", "lessons", "courses", "weekDays", "teachers", "student" та інші; дані бере через `teacherService.getAll`, `studentService.getById`, `lessonService.compileLessonsForStudentInAdmin`, `courseService.getAll`, `courseMapper.mapCourseToCourseDTO` та інші.
    @GetMapping("/student/{idSt}/lessons")
    public String gotoStudentLessons(@PathVariable("idSt") int idSt, Model model) {
        List<Teacher> teachers = teacherService.getAll();
        Student student = studentService.getById(idSt);

        List<Object> values = lessonService.compileLessonsForStudentInAdmin(student.getId());
        List<String> weekDays = (List<String>) values.get(0);
        HashMap<String, LessonAdminLessonsDTO> lessonHashMap = (HashMap<String, LessonAdminLessonsDTO>) values.get(1);
        HashMap<String, LessonAdminLessonsDTO> lessonDurations = (HashMap<String, LessonAdminLessonsDTO>) values.get(2);

        List<Course> courses = courseService.getAll();

        model.addAttribute("lessonDurations", lessonDurations);
        model.addAttribute("lessonDurations", lessonDurations);
        model.addAttribute("lessons", lessonHashMap);
        model.addAttribute("courses", courses.stream().map(el -> courseMapper.mapCourseToCourseDTO(el)).collect(Collectors.toList()));
        model.addAttribute("weekDays", weekDays);
        model.addAttribute("teachers", teachers.stream().map(el -> teacherMapper.mapTeacherToTeacherDTO(el)).collect(Collectors.toList()));
        model.addAttribute("student", studentMapper.mapStudentToStudentDTO(student));
        model.addAttribute("hours", Arrays.asList("8:00", "8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30"));
        return "closedAdmin/adminStudents/studentLessons";
    }
    // Відкриває маршрут GET /student/{idSt}/lessons/{output} і готує дані для шаблону "closedAdmin/adminStudents/studentLessons".
    // У Model додає "lessonDurations", "lessons", "courses", "weekDays", "output", "teachers" та інші; дані бере через `teacherService.getAll`, `studentService.getById`, `lessonService.compileLessonsForStudentInAdmin`, `courseService.getAll`, `courseMapper.mapCourseToCourseDTO` та інші.
    @GetMapping("/student/{idSt}/lessons/{output}")
    public String gotoStudentLessonsWithOutput(@PathVariable("idSt") int idSt, @PathVariable(value = "output", required = false) String output, Model model) {
        List<Teacher> teachers = teacherService.getAll();
        Student student = studentService.getById(idSt);
        List<Object> values = lessonService.compileLessonsForStudentInAdmin(student.getId());
        List<String> weekDays = (List<String>) values.get(0);
        HashMap<String, LessonAdminLessonsDTO> lessonHashMap = (HashMap<String, LessonAdminLessonsDTO>) values.get(1);
        HashMap<String, LessonAdminLessonsDTO> lessonDurations = (HashMap<String, LessonAdminLessonsDTO>) values.get(2);
        List<Course> courses = courseService.getAll();
        model.addAttribute("lessonDurations", lessonDurations);
        model.addAttribute("lessonDurations", lessonDurations);
        model.addAttribute("lessons", lessonHashMap);
        model.addAttribute("courses", courses.stream().map(el -> courseMapper.mapCourseToCourseDTO(el)).collect(Collectors.toList()));
        model.addAttribute("weekDays", weekDays);
        if (!output.isEmpty()) {
            model.addAttribute("output", output);
        }
        model.addAttribute("teachers", teachers.stream().map(el -> teacherMapper.mapTeacherToTeacherDTO(el)).collect(Collectors.toList()));
        model.addAttribute("student", studentMapper.mapStudentToStudentDTO(student));
        model.addAttribute("hours", Arrays.asList("8:00", "8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30"));
        return "closedAdmin/adminStudents/studentLessons";
    }
    // Видаляє або від’єднує дані за маршрутом POST /student/{idSt}/lessons/deleteLesson/{id} у модулі «уроки».
    // Викликає `lessonService.getById`, `studentService.getById`, `mailService.sendEmailWithThymeleafTeacherAboutLessonRemoved`, `lessonService.deleteById`, `studentMapper.mapStudentToStudentDTO`; після завершення повертає "redirect:/admin/student/".
    @PostMapping("/student/{idSt}/lessons/deleteLesson/{id}")
    @Transactional
    public String deleteLesson(@PathVariable("idSt") int idSt, @PathVariable("id") int idLes, Model model) throws MessagingException {

        Mail mail = new Mail();
        mail.setTo(Collections.singletonList((lessonService.getById(idLes).getTeacher().getEmail())));
        mail.setSubject("Лист про відміну заняття");
        mail.setBody("");
        StudentDTO student = studentMapper.mapStudentToStudentDTO(studentService.getById(idSt));
        mailService.sendEmailWithThymeleafTeacherAboutLessonRemoved(mail, student, lessonService.getById(idLes).getLessonTime(), lessonService.getById(idLes).getDuration());
        lessonService.deleteById(idLes);
        return "redirect:/admin/student/" + idSt + "/lessons/LESSON_DELETED";
    }
    // Видаляє або від’єднує дані за маршрутом POST /student/{idSt}/lessons/deleteCourse/{id} у модулі «уроки».
    // Викликає `lessonService.getById`, `studentService.getById`, `mailService.sendEmailWithThymeleafTeacherAboutCourseRemoved`, `lessonService.deleteAllByStIdAndTeachIdAndTswIdAndLesTimeAfterNow`, `tswService.removeAllByStIdAndTeachIdAndTswId` та інші; після завершення повертає "redirect:/admin/student/".
    @PostMapping("/student/{idSt}/lessons/deleteCourse/{id}")
    @Transactional
    public String deleteCourse(@PathVariable("idSt") int idSt, @PathVariable("id") int idLes) throws MessagingException {
        Lesson lesson = lessonService.getById(idLes);
        Mail mail = new Mail();
        mail.setTo(Collections.singletonList(lesson.getTeacher().getEmail()));
        mail.setSubject("Лист про відміну заняття");
        mail.setBody("");
        StudentDTO student = studentMapper.mapStudentToStudentDTO(studentService.getById(idSt));
        mailService.sendEmailWithThymeleafTeacherAboutCourseRemoved(mail, student, lesson.getLessonTime(), lesson.getDuration());
        lessonService.deleteAllByStIdAndTeachIdAndTswIdAndLesTimeAfterNow(idSt, lesson.getTeacher().getId(), lesson.getTimeOfTheWeek().getId(), LocalDateTime.now().minusMinutes(30));
        tswService.removeAllByStIdAndTeachIdAndTswId(idSt, lesson.getTeacher().getId(), lesson.getTimeOfTheWeek().getId());
        return "redirect:/admin/student/" + idSt + "/lessons/COURSE_DELETED";
    }
    // Оновлює дані за маршрутом POST /student/{idSt}/lessons/editLesson/notPicked у модулі «уроки».
    // Після завершення повертає "redirect:/admin/student/".
    @PostMapping("/student/{idSt}/lessons/editLesson/notPicked")
    @Transactional
    public String manageNotPickedItemInEdit(@PathVariable("idSt") int idSt) {
        return "redirect:/admin/student/" + idSt + "/lessons/ITEM_NOT_PICKED";
    }

    // Оновлює дані за маршрутом POST /student/{idSt}/lessons/editLesson/{id}/{nTId}/{date} у модулі «уроки».
    // Викликає `lessonService.getById`, `lessonService.checkIfLessonValidWithReputitions`, `studentService.getById`, `mailService.sendEmailWithThymeleafToTeacherAboutLessonTimeEdited`, `lessonService.update` та інші; працює зі статусами SUCCESS; після завершення повертає "redirect:/admin/student/".
    @PostMapping("/student/{idSt}/lessons/editLesson/{id}/{nTId}/{date}")
    @Transactional
    public String editLesson(@PathVariable("idSt") int idSt, @PathVariable("id") int idLes, @PathVariable("nTId") int newTimeId, @PathVariable("date") String date, Model model) throws MessagingException {
        Lesson lesson = lessonService.getById(idLes);
        List<Object> objs = lessonService.checkIfLessonValidWithReputitions(lesson.getTeacher().getId(), lesson.getStudent().getId(), lesson.getCourse().getId(), idLes, lesson.getDuration(), date, 1);
        Statuses status = (Statuses) objs.get(0);
        LocalDateTime newDate = (LocalDateTime) objs.get(1);
        if (status == Statuses.SUCCESS) {
            Mail mail = new Mail();
            mail.setTo(Collections.singletonList(lesson.getTeacher().getEmail()));
            mail.setSubject("Лист про зміну часу одного заняття");
            mail.setBody("");
            StudentDTO student = studentMapper.mapStudentToStudentDTO(studentService.getById(idSt));
            mailService.sendEmailWithThymeleafToTeacherAboutLessonTimeEdited(mail, student, lesson.getLessonTime(), newDate, lesson.getDuration());
            lesson.setLessonTime(newDate);
            lessonService.update(lesson.getId(), lesson);
            return "redirect:/admin/student/" + idSt + "/lessons/LESSON_EDITED";
        } else {
            return "redirect:/admin/student/" + idSt + "/lessons/" + status.name();
        }
    }
}
