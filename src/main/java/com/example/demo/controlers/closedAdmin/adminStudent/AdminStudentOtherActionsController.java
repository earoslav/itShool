package com.example.demo.controlers.closedAdmin.adminStudent;

import com.example.demo.dto.entities.StudentDTO;
import com.example.demo.mapper.entity.AdminMapper;
import com.example.demo.mapper.entity.StudentMapper;
import com.example.demo.mapper.programe.LessonMapper;
import com.example.demo.models.entities.Student;
import com.example.demo.models.entities.User;
import com.example.demo.services.email.MailService;
import com.example.demo.services.entities.AdminService;
import com.example.demo.services.entities.StudentService;
import com.example.demo.services.entities.TeacherService;
import com.example.demo.services.other.CommentService;
import com.example.demo.services.other.TimeOfTheWeekService;
import com.example.demo.services.program.CourseService;
import com.example.demo.services.program.LessonService;
import com.example.demo.services.security.UserService;
import com.example.demo.services.thirdTable.EmptyTimesForTeacherService;
import com.example.demo.services.thirdTable.StudentCourseService;
import com.example.demo.services.thirdTable.TeacherCourseService;
import com.example.demo.services.thirdTable.TeacherStudentTimeOfTheWeekService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminStudentOtherActionsController {
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
    private StudentMapper studentMapper;
    private PasswordEncoder passwordEncoder;
    private UserService userService;
    private AdminMapper adminMapper;

    public AdminStudentOtherActionsController(TeacherCourseService teacherCourseService, TeacherService teacherService, EmptyTimesForTeacherService emptyTimesForTeacherService, TimeOfTheWeekService theWeekService, CourseService courseService, StudentCourseService studentCourseService, CommentService commentService, LessonService lessonService, StudentService studentService, LessonMapper lessonMapper, TeacherStudentTimeOfTheWeekService tswService, MailService mailService, AdminService adminService, StudentMapper studentMapper, PasswordEncoder passwordEncoder, UserService userService, AdminMapper adminMapper) {
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
        this.studentMapper = studentMapper;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.adminMapper = adminMapper;
    }
    @GetMapping("/addStudent")
    public String gotoCreateStudent(Model model) {
        model.addAttribute("student",new Student());
        return "closedAdmin/adminStudents/addNewStudent";
    }

    @PostMapping("/addStudent")
    public String addStudent(@ModelAttribute("student") Student student, Model model) {
        try {
            if (studentService.checkIfExistsByEmail(student.getEmail())) {
                if(student.getPassword().equals("")){
                    model.addAttribute("student");
                    model.addAttribute("output", "NOPASS");
                    return "closedAdmin/adminStudents/addNewStudent";
                }
                model.addAttribute("output", "exists");
                model.addAttribute("student", student);
                return "closedAdmin/adminStudents/addNewStudent";
            }
            User user = new User(student.getName(), student.getEmail(), passwordEncoder.encode(student.getPassword()), "STUDENT");
            userService.create(user);
            student.setPassword(passwordEncoder.encode(student.getPassword()));
            student.setUser(user);
            studentService.create(student);
            return "redirect:/admin/homepage/studentAdded";
        } catch (Exception e) {
            System.out.println(e);
            model.addAttribute("output");
            model.addAttribute("error", "general");
            return "closedAdmin/adminStudents/addNewStudent";
        }


    }


    @GetMapping("/students/{output}")
    public String gotoStudents(@PathVariable("output") String output, Model model) {
        List<StudentDTO> students = studentService.getAll().stream().map(student -> studentMapper.mapStudentToStudentDTO(student)).collect(Collectors.toList());
        model.addAttribute("students", students);
        model.addAttribute("output", output);
        return "closedAdmin/adminStudents/students";
    }
    @GetMapping("/students")
    public String gotoStudents(Model model) {
        List<StudentDTO> students = studentService.getAll().stream().map(student -> studentMapper.mapStudentToStudentDTO(student)).collect(Collectors.toList());
        model.addAttribute("students", students);
        return "closedAdmin/adminStudents/students";
    }

    @GetMapping("/student/{id}/edit")
    public String gotoEditStudent(@PathVariable("id") int id, Model model) {

        model.addAttribute("password", "");
        model.addAttribute("student", studentMapper.mapStudentToStudentDTO(studentService.getById(id)));
        return "closedAdmin/adminStudents/editStudent";
    }
    @GetMapping("/student/{id}/edit/{output}")
    public String gotoEditStudentWithOutput(@PathVariable("id") int id,@PathVariable("output") String output, Model model) {
        model.addAttribute("output", output);
        model.addAttribute("password", "");
        model.addAttribute("student", studentMapper.mapStudentToStudentDTO(studentService.getById(id)));
        return "closedAdmin/adminStudents/editStudent";
    }

    @PostMapping("/student/{id}/edit/{password}")
    public String editStudent(
            @PathVariable("id") int id,
            @ModelAttribute("student") StudentDTO student,
            @PathVariable("password") String password,
            Model model) {

        try {
            if (studentService.checkIfExistsByEmail(student.getUser().getEmail()) && !studentService.getById(id).getEmail().equals(student.getUser().getEmail())) {
                return "redirect:/admin/student/"+id+"/edit/exists";
            }

            Student student1 = studentService.getById(student.getId());
            if(password.equals("OLDPASS")){
                password = studentService.getById(student.getId()).getPassword();
            }
            student1.setPassword(passwordEncoder.encode(password));
            studentService.update(id, student1);
            return "redirect:/admin/student/"+id+"/edit/studentEdited";
        } catch (Exception e) {
            return "redirect:/admin/student/"+id+"/edit/general";
        }

    }

    @PostMapping("/student/delete")
    @Transactional
    public String deleteStudent(@RequestParam("studentId") int id) {
        Student student = studentService.getById(id);
        tswService.removeAllTswByStudentId(student.getId());
        lessonService.deleteAllLessonsByStudent(student);
        studentCourseService.deleteAllCoursesByStudent(student);
        commentService.deleteAllByStudent(student);
        studentService.deleteById(id);
        return "redirect:/admin/students/studentDeleted";
    }

}
