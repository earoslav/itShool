package com.example.demo.controlers.openSource;

import com.example.demo.dto.other.CommentDTO;
import com.example.demo.dto.programe.CourseDTO;
import com.example.demo.dto.entities.TeacherDTO;
import com.example.demo.mapper.entity.StudentMapper;
import com.example.demo.mapper.other.CommentMapper;
import com.example.demo.mapper.programe.CourseMapper;
import com.example.demo.mapper.entity.TeacherMapper;
import com.example.demo.services.email.MailService;
import com.example.demo.services.entities.AdminService;
import com.example.demo.services.entities.StudentService;
import com.example.demo.services.entities.TeacherService;
import com.example.demo.services.other.CommentService;
import com.example.demo.services.other.TimeOfTheWeekService;
import com.example.demo.services.program.CourseService;
import com.example.demo.services.thirdTable.EmptyTimesForTeacherService;
import com.example.demo.services.thirdTable.TeacherCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// Контролер OpenSourcePagesController обслуговує веб-сторінки модуля «публічні сторінки та реєстрація».
// Методи нижче приймають параметри з URL або форм, викликають сервіси проекту і повертають потрібні Thymeleaf-шаблони чи redirect-и.
@Controller
@RequestMapping("/openSource")
public class OpenSourcePagesController {
    private CourseService courseService;
    private StudentService studentService;
    private CourseMapper courseMapper;
    private CommentMapper commentMapper;
    private CommentService commentService;
    private TeacherMapper teacherMapper;
    private TeacherService teacherService;
    private TimeOfTheWeekService timeWeekService;
    private TeacherCourseService teacherCourseService;
    private EmptyTimesForTeacherService emptyTimesForTeacherService;
    private AdminService adminService;

    private final MailService mailService;
    private StudentMapper studentMapper;
    // Отримує через Spring залежності CourseService, StudentService, CourseMapper, CommentMapper, CommentService, TeacherMapper, TeacherService та інші.
    // Ці сервіси й mapper-и потрібні методам класу для роботи з модулем «публічні сторінки та реєстрація» без ручного створення об’єктів.
    @Autowired
    public OpenSourcePagesController(CourseService courseService,
                                     StudentService studentService,
                                     CourseMapper courseMapper,
                                     CommentMapper commentMapper,
                                     CommentService commentService,
                                     TeacherMapper teacherMapper,
                                     TeacherService teacherService,
                                     TimeOfTheWeekService timeWeekService,
                                     TeacherCourseService teacherCourseService,
                                     EmptyTimesForTeacherService emptyTimesForTeacherService, AdminService adminService,
                                     MailService mailService, StudentMapper studentMapper) {

        this.courseService = courseService;
        this.studentService = studentService;
        this.courseMapper = courseMapper;
        this.commentMapper = commentMapper;
        this.commentService = commentService;
        this.teacherMapper = teacherMapper;
        this.teacherService = teacherService;
        this.timeWeekService = timeWeekService;
        this.teacherCourseService = teacherCourseService;
        this.emptyTimesForTeacherService = emptyTimesForTeacherService;
        this.adminService = adminService;
        this.mailService = mailService;
        this.studentMapper = studentMapper;
    }
    // Відкриває маршрут GET /homepage і готує дані для шаблону "openSource/openSourceHomepage/homepage".
    // У Model додає "courses", "comments", "teachers"; дані бере через `courseService.getAll`, `commentService.getAll`, `teacherService.getAll`, `courseMapper.mapCourseToCourseDTO`, `commentMapper.mapCommentToCommentDTO` та інші.
    @GetMapping("/homepage")
    public String gotoHomePage(Model model) {
        List<CourseDTO> courses = new ArrayList<>();
        List<CommentDTO> comments = new ArrayList<>();
        List<TeacherDTO> teachers = new ArrayList<>();
        try {
            courses = courseService.getAll().stream().map(course -> courseMapper.mapCourseToCourseDTO(course)).collect(Collectors.toList());
            comments = commentService.getAll().stream().map(comment -> commentMapper.mapCommentToCommentDTO(comment)).collect(Collectors.toList());
            teachers = teacherService.getAll().stream().map(teacher -> teacherMapper.mapTeacherToTeacherDTO(teacher)).collect(Collectors.toList());
        } catch (Exception e) {
            System.out.println(e.getMessage());}
        model.addAttribute("courses", courses);
        model.addAttribute("comments", comments);
        model.addAttribute("teachers", teachers);
        return "openSource/openSourceHomepage/homepage";
    }
    // Відкриває маршрут GET /courses і готує дані для шаблону "openSource/openSourceOther/courses".
    // У Model додає "courses"; дані бере через `courseService.getAll`, `courseMapper.mapCourseToCourseDTO`.
    @GetMapping("/courses")
    public String gotoCourses (Model model){
        List<CourseDTO> courses = new ArrayList<>();
        try {
            courses = courseService.getAll().stream().map(course -> courseMapper.mapCourseToCourseDTO(course)).collect(Collectors.toList());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        model.addAttribute("courses", courses);
        return "openSource/openSourceOther/courses";
    }

}
