package com.example.demo.controlers.closedStudent;

import com.example.demo.dto.entities.StudentDTO;
import com.example.demo.mapper.entity.StudentMapper;
import com.example.demo.mapper.entity.TeacherMapper;
import com.example.demo.mapper.programe.CourseMapper;
import com.example.demo.mapper.programe.LessonMapper;
import com.example.demo.models.entities.Student;
import com.example.demo.services.email.MailService;
import com.example.demo.services.entities.StudentService;
import com.example.demo.services.entities.TeacherService;
import com.example.demo.services.other.TimeOfTheWeekService;
import com.example.demo.services.program.CourseService;
import com.example.demo.services.program.LessonService;
import com.example.demo.services.security.UserService;
import com.example.demo.services.thirdTable.EmptyTimesForTeacherService;
import com.example.demo.services.thirdTable.TeacherCourseService;
import com.example.demo.services.thirdTable.TeacherStudentTimeOfTheWeekService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.ObjectMapper;

// Контролер StudentHomepageController обслуговує веб-сторінки модуля «студенти».
// Методи нижче приймають параметри з URL або форм, викликають сервіси проекту і повертають потрібні Thymeleaf-шаблони чи redirect-и.
@Controller
@RequestMapping("/student")
public class StudentHomepageController {
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
    private PasswordEncoder passwordEncoder;
    private UserService userService;
    // Отримує через Spring залежності TeacherService, LessonMapper, TimeOfTheWeekService, CourseService, EmptyTimesForTeacherService, LessonService, TeacherCourseService та інші.
    // Ці сервіси й mapper-и потрібні методам класу для роботи з модулем «студенти» без ручного створення об’єктів.
    public StudentHomepageController(TeacherService teacherService, LessonMapper lessonMapper, TimeOfTheWeekService theWeekService, CourseService courseService, EmptyTimesForTeacherService emptyTimesForTeacherService, LessonService lessonService, TeacherCourseService teacherCourseService, MailService mailService, TeacherStudentTimeOfTheWeekService tswService, StudentService studentService, ObjectMapper objectMapper, StudentMapper studentMapper, PasswordEncoder passwordEncoder, UserService userService) {
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
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }
    // Відкриває маршрут GET /{idSt}/homepage і готує дані для шаблону "closedStudent/studentHomepage/homepage".
    // У Model додає "password", "student"; дані бере через `studentService.getById`, `studentMapper.mapStudentToStudentDTO`.
    @GetMapping("/{idSt}/homepage")
    public String gotoStudent(@PathVariable("idSt") int id, Model model){
        Student student = studentService.getById(id);
        model.addAttribute("password", "");
        model.addAttribute("student", studentMapper.mapStudentToStudentDTO(student));
        return "closedStudent/studentHomepage/homepage";
    }
    // Відкриває маршрут GET /{idSt}/homepage/{output} і готує дані для шаблону "closedStudent/studentHomepage/homepage".
    // У Model додає "output", "password", "student"; дані бере через `studentService.getById`, `studentMapper.mapStudentToStudentDTO`.
    @GetMapping("/{idSt}/homepage/{output}")
    public String gotoStudentWithOutput(@PathVariable("idSt") int id,@PathVariable("output") String output, Model model){
        Student student = studentService.getById(id);
        model.addAttribute("output", output);
        model.addAttribute("password", "");
        model.addAttribute("student", studentMapper.mapStudentToStudentDTO(student));
        return "closedStudent/studentHomepage/homepage";
    }
    // Оновлює дані за маршрутом POST /{idSt}/homepage/{password} у модулі «студенти».
    // Викликає `userService.checkIfExistsByEmail`, `studentService.getById`, `studentService.update`, `studentMapper.mapStudentDTOToStudent`; після завершення повертає "redirect:/student/".
    @PostMapping("/{idSt}/homepage/{password}")
    public String updateStudent(@ModelAttribute("student") StudentDTO student, @PathVariable("password") String password, @PathVariable("idSt") int id, Model model) {
        if(userService.checkIfExistsByEmail(student.getUser().getEmail()) && !studentService.getById(id).getEmail().equals(student.getUser().getEmail())){
            return "redirect:/student/"+id+"/homepage/exists";
        }
        else{
            if(password.equals("OLDPASS")){
                password = studentService.getById(id).getPassword();
                Student retStudent = studentMapper.mapStudentDTOToStudent(student);
                retStudent.setPassword(password);
                studentService.update(id, retStudent);
                return "redirect:/student/"+id+"/homepage/studentEdited";

            }
            Student retStudent = studentMapper.mapStudentDTOToStudent(student);
            retStudent.setPassword(passwordEncoder.encode(password));
            studentService.update(id, retStudent);

        }
        return "redirect:/student/"+id+"/homepage/studentEdited";
    }
}
