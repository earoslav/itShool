package com.example.demo.controlers.closedAdmin.adminHomepage;

import com.example.demo.mapper.entity.AdminMapper;
import com.example.demo.mapper.programe.LessonMapper;
import com.example.demo.models.entities.Admin;
import com.example.demo.services.email.MailService;
import com.example.demo.services.entities.AdminService;
import com.example.demo.services.entities.StudentService;
import com.example.demo.services.entities.TeacherService;
import com.example.demo.services.other.CommentService;
import com.example.demo.services.other.TimeOfTheWeekService;
import com.example.demo.services.program.CourseService;
import com.example.demo.services.program.LessonService;
import com.example.demo.services.thirdTable.EmptyTimesForTeacherService;
import com.example.demo.services.thirdTable.TeacherCourseService;
import com.example.demo.services.thirdTable.TeacherStudentTimeOfTheWeekService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.demo.models.entities.Teacher;
import com.example.demo.models.programe.Lesson;

// AdminHomepageController handles web pages for the "administrators" module.
// The methods below accept parameters from URLs or forms, call project services, and return the required Thymeleaf templates or redirects.
@Controller
@RequestMapping("/admin")
public class AdminHomepageController {
    private TeacherCourseService teacherCourseService;
    private TeacherService teacherService;
    private EmptyTimesForTeacherService emptyTimesForTeacherService;

    private TimeOfTheWeekService theWeekService;
    private CourseService courseService;

    private CommentService commentService;
    private LessonService lessonService;
    private StudentService studentService;
    private LessonMapper lessonMapper;
    private TeacherStudentTimeOfTheWeekService tswService;
    private MailService mailService;
    private AdminService adminService;
    private AdminMapper adminMapper;
    private PasswordEncoder passwordEncoder;
    // Receives dependencies through Spring: TeacherCourseService, TeacherService, EmptyTimesForTeacherService, TimeOfTheWeekService, CourseService, StudentCourseService, CommentService, and others.
    // These services and mappers are required by the class methods to work with the "administrators" module without manual object creation.
    public AdminHomepageController(TeacherCourseService teacherCourseService, TeacherService teacherService, EmptyTimesForTeacherService emptyTimesForTeacherService, TimeOfTheWeekService theWeekService, CourseService courseService, CommentService commentService, LessonService lessonService, StudentService studentService, LessonMapper lessonMapper, TeacherStudentTimeOfTheWeekService tswService, MailService mailService, AdminService adminService, AdminMapper adminMapper, PasswordEncoder passwordEncoder) {
        this.teacherCourseService = teacherCourseService;
        this.teacherService = teacherService;
        this.emptyTimesForTeacherService = emptyTimesForTeacherService;
        this.theWeekService = theWeekService;
        this.courseService = courseService;

        this.commentService = commentService;
        this.lessonService = lessonService;
        this.studentService = studentService;
        this.lessonMapper = lessonMapper;
        this.tswService = tswService;
        this.mailService = mailService;
        this.adminService = adminService;
        this.adminMapper = adminMapper;
        this.passwordEncoder = passwordEncoder;
    }
    // Opens the GET /homepage route and prepares data for the "closedAdmin/adminHomepage/homepage" template.
    // Adds "admin", "teacherEarnings" to the Model; retrieves data via `adminService.getAdmin`, `adminService.update`, `teacherService.getAll`, `lessonService.findAllByTeachId`, `adminMapper.mapAdminToAdminDTO`.
    @GetMapping("/homepage")
    public String gotoHomepage(Model model) {

        Admin admin = adminService.getAdmin();
//        admin.getUser().setPassword(passwordEncoder.encode("yargoro2010"));
//        adminService.update(admin.getId(), admin);
        model.addAttribute("admin", adminMapper.mapAdminToAdminDTO(admin));


        model.addAttribute("teacherEarnings", lessonService.calculateTeachersEarnings());

        return "closedAdmin/adminHomepage/homepage";
    }
    // Opens the GET /homepage/{output} route and prepares data for the "closedAdmin/adminHomepage/homepage" template.
    // Adds "admin", "output", "teacherEarnings" to the Model; retrieves data via `adminService.getAdmin`, `adminService.update`, `teacherService.getAll`, `lessonService.findAllByTeachId`, `adminMapper.mapAdminToAdminDTO`.
    @GetMapping("/homepage/{output}")
    public String gotoHomepage(Model model, @PathVariable(value = "output", required = false) String output) {
        Admin admin = adminService.getAdmin();
//        admin.getUser().setPassword(passwordEncoder.encode("yargoro2010"));
//        adminService.update(admin.getId(), admin);
        model.addAttribute("admin", adminMapper.mapAdminToAdminDTO(admin));
        model.addAttribute("output", output);
        model.addAttribute("teacherEarnings", lessonService.calculateTeachersEarnings());

        return "closedAdmin/adminHomepage/homepage";
    }
}
