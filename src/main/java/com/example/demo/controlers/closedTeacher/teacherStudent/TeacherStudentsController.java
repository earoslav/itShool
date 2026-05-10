package com.example.demo.controlers.closedTeacher.teacherStudent;

import com.example.demo.mapper.entity.StudentMapper;
import com.example.demo.mapper.entity.TeacherMapper;
import com.example.demo.mapper.programe.CourseMapper;
import com.example.demo.mapper.programe.LessonMapper;
import com.example.demo.models.email.Mail;
import com.example.demo.models.entities.Student;
import com.example.demo.models.entities.Teacher;
import com.example.demo.models.programe.Lesson;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

// Контролер TeacherStudentsController обслуговує веб-сторінки модуля «викладачі».
// Методи нижче приймають параметри з URL або форм, викликають сервіси проекту і повертають потрібні Thymeleaf-шаблони чи redirect-и.
@Controller
@RequestMapping("/teacher")
public class TeacherStudentsController {
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
    private TeacherMapper teacherMapper;
    private StudentMapper studentMapper;
    private CourseMapper courseMapper;
    // Отримує через Spring залежності TeacherService, LessonMapper, TimeOfTheWeekService, CourseService, EmptyTimesForTeacherService, LessonService, TeacherCourseService та інші.
    // Ці сервіси й mapper-и потрібні методам класу для роботи з модулем «викладачі» без ручного створення об’єктів.
    public TeacherStudentsController(TeacherService teacherService, LessonMapper lessonMapper, TimeOfTheWeekService theWeekService, CourseService courseService, EmptyTimesForTeacherService emptyTimesForTeacherService, LessonService lessonService, TeacherCourseService teacherCourseService, MailService mailService, TeacherStudentTimeOfTheWeekService tswService, StudentService studentService, TeacherMapper teacherMapper, StudentMapper studentMapper, CourseMapper courseMapper) {
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
        this.teacherMapper = teacherMapper;
        this.studentMapper = studentMapper;
        this.courseMapper = courseMapper;
    }
    // Відкриває маршрут GET /{idTeach}/students і готує дані для шаблону "closedTeacher/teacherStudents/students".
    // У Model додає "students", "teacher"; дані бере через `tswService.findAllByTeachId`, `teacherService.getById`, `studentMapper.mapStudentToStudentDTO`, `teacherMapper.mapTeacherToTeacherDTO`.
    @GetMapping("/{idTeach}/students")
    public String gotoMyStudents(@PathVariable("idTeach") int id, Model model){
        HashSet<Student> students = new HashSet<>();
        HashSet<Student> finalStudents = students;
        tswService.findAllByTeachId(id).stream().forEach(obj-> finalStudents.add(obj.getStudent()));
        List<Student> retStudents = finalStudents.stream().sorted(Comparator.comparing(Student::getId)).toList();
        model.addAttribute("students", retStudents.stream().map(el->studentMapper.mapStudentToStudentDTO(el)).collect(Collectors.toList()));
        model.addAttribute("teacher", teacherMapper.mapTeacherToTeacherDTO(teacherService.getById(id)));
        return "closedTeacher/teacherStudents/students";
    }
    // Відкриває маршрут GET /{idTeach}/students/{output} і готує дані для шаблону "closedTeacher/teacherStudents/students".
    // У Model додає "output", "students", "teacher"; дані бере через `tswService.findAllByTeachId`, `teacherService.getById`, `studentMapper.mapStudentToStudentDTO`, `teacherMapper.mapTeacherToTeacherDTO`.
    @GetMapping("/{idTeach}/students/{output}")
    public String gotoMyStudentsWithOutput(@PathVariable("idTeach") int id, @PathVariable("output") String output, Model model) {
        HashSet<Student> students = new HashSet<>();
        HashSet<Student> finalStudents = students;
        tswService.findAllByTeachId(id).stream().forEach(obj -> finalStudents.add(obj.getStudent()));
        List<Student> retStudents = finalStudents.stream().sorted(Comparator.comparing(Student::getId)).toList();
        model.addAttribute("output", output);
        model.addAttribute("students", retStudents.stream().map(el -> studentMapper.mapStudentToStudentDTO(el)).collect(Collectors.toList()));
        model.addAttribute("teacher", teacherMapper.mapTeacherToTeacherDTO(teacherService.getById(id)));
        return "closedTeacher/teacherStudents/students";
    }
    // Видаляє або від’єднує дані за маршрутом POST /{idTeach}/student/{idSt}/delete у модулі «викладачі».
    // Викликає `lessonService.findAllByIdTandIdSt`, `lessonService.deleteById`, `tswService.removeAllByTidAndSid`, `studentService.getById`, `teacherService.getById` та інші; після завершення повертає "redirect:/teacher/".
    @PostMapping("/{idTeach}/student/{idSt}/delete")
    @Transactional
    public String deleteStudent(@PathVariable("idTeach") int idT, @PathVariable("idSt") int idSt, Model model) throws MessagingException {
        List<Lesson> lessons = lessonService.findAllByIdTandIdSt(idT, idSt);
        lessons.stream().filter(lesson -> lesson.getLessonTime().isAfter(LocalDateTime.now())).forEach(lesson -> lessonService.deleteById(lesson.getId()));
        tswService.removeAllByTidAndSid(idT, idSt);
        Mail mail = new Mail();
        mail.setTo(Collections.singletonList(studentService.getById(idSt).getEmail()));
        mail.setSubject("Лист про відміну занять");
        mail.setBody("");
        Teacher saved = teacherService.getById(idT);
        mailService.sendEmailWithThymeleafToStudentAboutTeacherRemover(mail, teacherMapper.mapTeacherToTeacherDTO(saved));
        model.addAttribute("students", studentMapper.mapStudentToStudentDTO(studentService.getById(idSt)));
        model.addAttribute("teacher", teacherMapper.mapTeacherToTeacherDTO(teacherService.getById(idT)));
        return "redirect:/teacher/"+idT+"/students/STUDENT_DELETED";
    }

}
