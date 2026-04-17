package com.example.demo.controlers.openSource;

import com.example.demo.dto.CommentDTO;
import com.example.demo.dto.CourseDTO;
import com.example.demo.dto.TeacherDTO;
import com.example.demo.mapper.CommentMapper;
import com.example.demo.mapper.CourseMapper;
import com.example.demo.mapper.TeacherMapper;
import com.example.demo.models.*;
import com.example.demo.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/homepage")
public class HomePageController {
    public static final String Te = "teacher";
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
    @Autowired
    public HomePageController(CourseService courseService,
                              StudentService studentService,
                              CourseMapper courseMapper,
                              CommentMapper commentMapper,
                              CommentService commentService,
                              TeacherMapper teacherMapper,
                              TeacherService teacherService,
                              TimeOfTheWeekService timeWeekService,
                              TeacherCourseService teacherCourseService,
                              EmptyTimesForTeacherService emptyTimesForTeacherService, AdminService adminService,
                              MailService mailService) {

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
    }
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
        return "openSource/homepage";
    }

    @GetMapping("/courses")
    public String gotoCourses (Model model){
        List<CourseDTO> courses = new ArrayList<>();
        try {
            courses = courseService.getAll().stream().map(course -> courseMapper.mapCourseToCourseDTO(course)).collect(Collectors.toList());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        model.addAttribute("courses", courses);
        return "openSource/courses";
    }
    @GetMapping("/signUpAsStudent")
    public String gotoSignUpAsStudent(Model model){
        Student student = new Student();
        model.addAttribute("student", student);
        return "openSource/signUpAsStudent";
    }
    @PostMapping("/signUpAsStudent")
    public String signUpAsStudent(@ModelAttribute("student") Student student, Model model){
        try{
            if(!studentService.checkIfExistsByEmail(student.getEmail())){
                studentService.create(student);
            }else{
                model.addAttribute("error", "exists");

                return "openSource/signUpAsStudent";
            }
            return "redirect:/student/homepage";
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        model.addAttribute("error", "general");
        return "openSource/signUpAsStudent";

    }
    @GetMapping("/signUpAsTeacher")
    public String gotoSignUpAsTeacher(Model model){
        Teacher teacher = new Teacher();
        List<Course> courses = new ArrayList<>();
        List<TimeOfTheWeek> freeTimes = new ArrayList<>();
        List<Integer> days = List.of(7, 1, 2, 3, 4, 5, 6); // Sunday first
        List<String> dayNames = List.of("Неділя", "Понеділок", "Вівторок", "Середа", "Четвер", "П'ятниця", "Субота");
        List<Integer> hours = new ArrayList<>();
        for (int h = 8; h <= 21; h++) {
            hours.add(h);
        }
        try{
            courses = courseService.getAll();
            freeTimes = timeWeekService.getAll();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        model.addAttribute("teacher", teacher);
        model.addAttribute("courses", courses);
        model.addAttribute("freeTimes", freeTimes);
        model.addAttribute("days", days);
        model.addAttribute("dayNames", dayNames);
        model.addAttribute("hours", hours);
        return "openSource/signUpAsTeacher";
    }
    @PostMapping("/signUpAsTeacher")
    public String signUpAsTeacher(@ModelAttribute("teacher") Teacher teacher,
                                   @RequestParam(value = "courseIds",required = false) List<Integer> courses,
                                   @RequestParam(value = "freeTimeIds",required = false) List<Integer> freeTimes,
                                   Model model) {
        List<Integer> days = List.of(7, 1, 2, 3, 4, 5, 6); // Sunday first
        List<String> dayNames = List.of("Неділя", "Понеділок", "Вівторок", "Середа", "Четвер", "П'ятниця", "Субота");
        List<Integer> hours = new ArrayList<>();
        for (int h = 8; h <= 21; h++) {
            hours.add(h);
        }
        List<Course> coursesToReload = courseService.getAll();
        List<TimeOfTheWeek> freeTimesToReload = timeWeekService.getAll();
        try {
            if (!teacherService.checkIfExistsByEmail(teacher.getEmail())) {

                teacher.setApproved(false);

                List<Course> selectedCourses = new ArrayList<>();
                List<TimeOfTheWeek> selectedFreeTimes = new ArrayList<>();

                freeTimes.stream().forEach(freeTime->selectedFreeTimes.add(timeWeekService.getById(freeTime)));
                courses.stream().forEach(course->selectedCourses.add(courseService.getById(course)));

                HashMap<String, List<String>> compiledTimes = timeWeekService.compileTimes(selectedFreeTimes);

                List<Integer> timesIds = new ArrayList<>();
                selectedFreeTimes.stream().forEach(time->timesIds.add(time.getId()));

                List<Integer> coursesIds = new ArrayList<>();
                selectedCourses.stream().forEach(course->coursesIds.add(course.getId()));

                Mail mail = new Mail();
                mail.setTo(Collections.singletonList(adminService.getAdmin().getEmail()));
                mail.setSubject("Нова заявка викладача — IT Kids School");
                mail.setBody("");
                Teacher saved = teacher;
                mailService.sendEmailWithThymeleaf(mail, saved, selectedFreeTimes, selectedCourses, compiledTimes, timesIds, coursesIds);
                return "redirect:/teacher/homepage";
            }
            model.addAttribute("error", "exists");
            model.addAttribute("courses", coursesToReload);
            model.addAttribute("freeTimes", freeTimesToReload);
            model.addAttribute("days", days);
            model.addAttribute("dayNames", dayNames);
            model.addAttribute("hours", hours);
            return "openSource/signUpAsTeacher";
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        model.addAttribute("courses", coursesToReload);
        model.addAttribute("freeTimes", freeTimesToReload);
        model.addAttribute("days", days);
        model.addAttribute("dayNames", dayNames);
        model.addAttribute("hours", hours);
        model.addAttribute("error", "general");
        return "openSource/signUpAsTeacher";
    }
}

