package com.example.demo.controlers.openSource;

import com.example.demo.dto.entities.StudentDTO;
import com.example.demo.dto.entities.TeacherDTO;
import com.example.demo.mapper.entity.StudentMapper;
import com.example.demo.mapper.other.CommentMapper;
import com.example.demo.mapper.other.TimeOfTheWeekMapper;
import com.example.demo.mapper.programe.CourseMapper;
import com.example.demo.mapper.entity.TeacherMapper;
import com.example.demo.models.email.Mail;
import com.example.demo.models.entities.Student;
import com.example.demo.models.entities.Teacher;
import com.example.demo.models.entities.User;
import com.example.demo.models.other.TimeOfTheWeek;
import com.example.demo.models.programe.Course;
import com.example.demo.services.email.MailService;
import com.example.demo.services.entities.AdminService;
import com.example.demo.services.entities.StudentService;
import com.example.demo.services.entities.TeacherService;
import com.example.demo.services.other.CommentService;
import com.example.demo.services.other.TimeOfTheWeekService;
import com.example.demo.services.program.CourseService;
import com.example.demo.services.security.UserService;
import com.example.demo.services.thirdTable.EmptyTimesForTeacherService;
import com.example.demo.services.thirdTable.TeacherCourseService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/openSource")
public class OpenSourceSignUps {
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

    private MailService mailService;
    private StudentMapper studentMapper;
    private TimeOfTheWeekMapper theWeekMapper;
    private UserService userService;
    private PasswordEncoder passwordEncoder;



    public OpenSourceSignUps(CourseService courseService, StudentService studentService, CourseMapper courseMapper, CommentMapper commentMapper, CommentService commentService, TeacherMapper teacherMapper, TeacherService teacherService, TimeOfTheWeekService timeWeekService, TeacherCourseService teacherCourseService, EmptyTimesForTeacherService emptyTimesForTeacherService, AdminService adminService, MailService mailService, StudentMapper studentMapper, TimeOfTheWeekMapper theWeekMapper, UserService userService, PasswordEncoder passwordEncoder) {
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
        this.theWeekMapper = theWeekMapper;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }
    @GetMapping("/signUpAsStudent")
    public String gotoSignUpAsStudent(Model model){
        Student student = new Student();
        model.addAttribute("student", studentMapper.mapStudentToStudentDTO(student));
        model.addAttribute("password", "");
        return "openSource/openSourceSignUps/signUpAsStudent";
    }
    @GetMapping("/signUpAsStudent/{output}")
    public String gotoSignUpAsStudentWithOutput(@PathVariable("output") String output, Model model){
        Student student = new Student();
        model.addAttribute("output", output);
        model.addAttribute("student", studentMapper.mapStudentToStudentDTO(student));
        model.addAttribute("password", "");
        return "openSource/openSourceSignUps/signUpAsStudent";
    }
    @PostMapping("/signUpAsStudent/{password}")
    public String signUpAsStudent(@ModelAttribute("student") StudentDTO student,@PathVariable("password") String password, Model model){
        try{
            if(password.equals("NOPASS")){
            return "redirect:/openSource/signUpAsStudent/NOPASS";
            }
            if(!studentService.checkIfExistsByEmail(student.getUser().getEmail())){
                Student student1 = studentMapper.mapStudentDTOToStudent(student);

                User user = new User(student.getUser().getName(), student.getUser().getEmail(), passwordEncoder.encode(password), "STUDENT");
                userService.create(user);
                student1.setUser(user);
                studentService.create(student1);
                return "redirect:/openSource/homepage/studentAdded";
            }else{
                return "redirect:/openSource/signUpAsStudent/exists";
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return "redirect:/openSource/signUpAsStudent/general";

    }
    @GetMapping("/signUpAsTeacher")
    public String gotoSignUpAsTeacher(Model model){
        Teacher teacher = new Teacher();
        List<Course> courses = new ArrayList<>();
        List<TimeOfTheWeek> freeTimes = new ArrayList<>();
        List<Integer> days = List.of(7, 1, 2, 3, 4, 5, 6); // Sunday first
        List<String> dayNames = List.of("Неділя", "Понеділок", "Вівторок", "Середа", "Четвер", "П'ятниця", "Субота");

        try{
            courses = courseService.getAll();
            freeTimes = timeWeekService.getAll();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        model.addAttribute("teacher", teacherMapper.mapTeacherToTeacherDTO(teacher));
        model.addAttribute("courses", courses.stream().map(el->courseMapper.mapCourseToCourseDTO(el)).collect(Collectors.toList()));
        model.addAttribute("freeTimes", freeTimes.stream().map(el->theWeekMapper.mapTTheWeekToTTheWeekDTO(el)).collect(Collectors.toList()));
        model.addAttribute("password", "");
        model.addAttribute("days", days);
        model.addAttribute("dayNames", dayNames);
        model.addAttribute("hours", Arrays.asList("8:00", "8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30"));
        return "openSource/openSourceSignUps/signUpAsTeacher";
    }
    @GetMapping("/signUpAsTeacher/{output}")
    public String gotoSignUpAsTeacherWithOutput(@PathVariable("output") String output, Model model){
        Teacher teacher = new Teacher();
        List<Course> courses = new ArrayList<>();
        List<TimeOfTheWeek> freeTimes = new ArrayList<>();
        List<Integer> days = List.of(7, 1, 2, 3, 4, 5, 6); // Sunday first
        List<String> dayNames = List.of("Неділя", "Понеділок", "Вівторок", "Середа", "Четвер", "П'ятниця", "Субота");

        try{
            courses = courseService.getAll();
            freeTimes = timeWeekService.getAll();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        model.addAttribute("output", output);
        model.addAttribute("teacher", teacherMapper.mapTeacherToTeacherDTO(teacher));
        model.addAttribute("courses", courses.stream().map(el->courseMapper.mapCourseToCourseDTO(el)).collect(Collectors.toList()));
        model.addAttribute("freeTimes", freeTimes.stream().map(el->theWeekMapper.mapTTheWeekToTTheWeekDTO(el)).collect(Collectors.toList()));
        model.addAttribute("password", "");
        model.addAttribute("days", days);
        model.addAttribute("dayNames", dayNames);
        model.addAttribute("hours", Arrays.asList("8:00", "8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30"));
        return "openSource/openSourceSignUps/signUpAsTeacher";
    }
    @PostMapping("/signUpAsTeacher/{password}")
    public String signUpAsTeacher(@ModelAttribute("teacher") TeacherDTO teacher,
                                  @PathVariable("password") String password,
                                  @RequestParam(value = "courseIds",required = false) List<Integer> courses,
                                  @RequestParam(value = "freeTimeIds",required = false) List<Integer> freeTimes,
                                  Model model) {
        List<Integer> hours = new ArrayList<>();
        for (int h = 8; h <= 21; h++) {
            hours.add(h);
        }
        List<Course> coursesToReload = courseService.getAll();
        List<TimeOfTheWeek> freeTimesToReload = timeWeekService.getAll();
        try {
            if (!teacherService.checkIfExistsByEmail(teacher.getUser().getEmail())) {
                if(password.equals("NOPASS")){
                    return "redirect:/openSource/signUpAsTeacher/NOPASS";
                }
                teacher.setApproved(0);

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


                mailService.sendEmailWithThymeleaf(mail, teacher, password, selectedFreeTimes.stream().map(time -> theWeekMapper.mapTTheWeekToTTheWeekDTO(time)).collect(Collectors.toList()), selectedCourses.stream().map(course -> courseMapper.mapCourseToCourseDTO(course)).collect(Collectors.toList()), compiledTimes, timesIds.toString(), coursesIds.toString());
                return "redirect:/openSource/homepage/teacherAdded";
            }
            return "redirect:/openSource/signUpAsTeacher/exists";
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/openSource/signUpAsTeacher/general";
    }
}
