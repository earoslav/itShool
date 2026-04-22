package com.example.demo.controlers.closedAdmin.adminStudent;

import com.example.demo.dto.entities.StudentDTO;
import com.example.demo.mapper.entity.StudentMapper;
import com.example.demo.mapper.programe.LessonMapper;
import com.example.demo.models.entities.Student;
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
import org.springframework.stereotype.Controller;
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

    public AdminStudentOtherActionsController(TeacherCourseService teacherCourseService, TeacherService teacherService, EmptyTimesForTeacherService emptyTimesForTeacherService, TimeOfTheWeekService theWeekService, CourseService courseService, StudentCourseService studentCourseService, CommentService commentService, LessonService lessonService, StudentService studentService, LessonMapper lessonMapper, TeacherStudentTimeOfTheWeekService tswService, MailService mailService, AdminService adminService, StudentMapper studentMapper) {
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
    }
    @GetMapping("/addStudent")
    private String gotoCreateStudent(Model model) {
        model.addAttribute("student",new Student());
        return "closedAdmin/adminStudents/addNewStudent";
    }

    @PostMapping("/addStudent")
    private String addStudent(@ModelAttribute("student") Student student, Model model) {
        try {
            if (studentService.checkIfExistsByEmail(student.getEmail())) {
                if(student.getPassword().equals("")){
                    model.addAttribute("student");
                    model.addAttribute("error", "NOPASS");
                    return "closedAdmin/adminStudents/addNewStudent";
                }
                model.addAttribute("error", "exists");
                model.addAttribute("student", student);
                return "closedAdmin/adminStudents/addNewStudent";
            }
            studentService.create(student);
            return "redirect:/admin/homepage";
        } catch (Exception e) {
            model.addAttribute("student");
            model.addAttribute("error", "general");
            return "closedAdmin/adminStudents/addNewStudent";
        }


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

    @PostMapping("/student/{id}/edit/{password}")
    public String editStudent(
            @PathVariable("id") int id,
            @ModelAttribute("student") StudentDTO student,
            @PathVariable("password") String password,
            Model model) {

        try {
            System.out.println(password+" :new pass");
            if (studentService.checkIfExistsByEmail(student.getEmail()) && !studentService.getById(id).getEmail().equals(student.getEmail())) {
                model.addAttribute(student);
                model.addAttribute("error", "exists");
                return "closedAdmin/adminStudents/editStudent";
            }

            Student student1 = studentService.getById(student.getId());
            if(password.equals("OLDPASS")){
                password = studentService.getById(student.getId()).getPassword();
            }
            student1.setPassword(password);
            studentService.update(id, student1);
            model.addAttribute("student", student);
            return "redirect:/admin/students";
        } catch (Exception e) {
            model.addAttribute(student);
            model.addAttribute("error", "general");
            return "closedAdmin/adminStudents/editStudent";
        }

    }

    @PostMapping("/student/delete")

    public String deleteStudent(@RequestParam("studentId") int id) {
        Student student = studentService.getById(id);
        lessonService.deleteAllLessonsByStudent(student);
        studentCourseService.deleteAllCoursesByStudent(student);
        commentService.deleteAllByStudent(student);
        studentService.deleteById(id);
        return "redirect:/admin/students";
    }

}
