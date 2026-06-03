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
public class

AdminStudentOtherActionsController {
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
    private StudentMapper studentMapper;
    private PasswordEncoder passwordEncoder;
    private UserService userService;
    private AdminMapper adminMapper;

    // Constructor connects dependencies to AdminStudentOtherActionsController: TeacherCourseService, TeacherService, EmptyTimesForTeacherService, TimeOfTheWeekService, CourseService, StudentCourseService, and others.
    // These objects are used in methods for student management, so the class does not create them manually.
    public AdminStudentOtherActionsController(TeacherCourseService teacherCourseService, TeacherService teacherService, EmptyTimesForTeacherService emptyTimesForTeacherService, TimeOfTheWeekService theWeekService, CourseService courseService, CommentService commentService, LessonService lessonService, StudentService studentService, LessonMapper lessonMapper, TeacherStudentTimeOfTheWeekService tswService, MailService mailService, AdminService adminService, StudentMapper studentMapper, PasswordEncoder passwordEncoder, UserService userService, AdminMapper adminMapper) {
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
        this.studentMapper = studentMapper;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.adminMapper = adminMapper;
    }

    // Method opens the student addition form for the admin and places an empty StudentDTO in the Model.
    // "student" and "password" are recorded in the Model, after which the user sees the template or redirect "closedAdmin/adminStudents/addNewStudent".
    @GetMapping("/addStudent")
    public String gotoCreateStudent(Model model) {
        model.addAttribute("student", studentMapper.mapStudentToStudentDTO(new Student()));
        model.addAttribute("password", "");
        return "closedAdmin/adminStudents/addNewStudent";
    }

    // Method creates a student from the admin form, encodes the password using PasswordEncoder, and saves the Student along with the User account.
    // "student", "output", and "error" are recorded in the Model, after which the user sees the template or redirect "closedAdmin/adminStudents/addNewStudent", "redirect:/admin/homepage/STUDENT_ADDED".
    @PostMapping("/addStudent")
    public String addStudent(@ModelAttribute("student") StudentDTO student, @RequestParam("password") String password, Model model) {
        return studentService.manageAddStudent(student, password, model);
    }

    // Method displays the list of students: for the admin, all Student records; for a teacher, only students linked to them.
    // "students" and "output" are recorded in the Model, after which the user sees the template or redirect "closedAdmin/adminStudents/students".
    @GetMapping("/students/{output}")
    public String gotoStudents(@PathVariable("output") String output, @RequestParam(value = "limit", required = false) String limit, Model model) {
        studentService.manageGetAllStudents(model, output, limit);
        return "closedAdmin/adminStudents/students";
    }

    // Method displays the list of students: for the admin, all Student records; for a teacher, only students linked to them.
    // "students" is recorded in the Model, after which the user sees the template or redirect "closedAdmin/adminStudents/students".
    @GetMapping("/students")
    public String gotoStudents(@RequestParam(value = "limit", required = false) String limit, Model model) {
        studentService.manageGetAllStudents(model, "", limit);
        return "closedAdmin/adminStudents/students";
    }

    // Method opens the edit form for a specific student, finds the Student by id, and passes their DTO to the template.
    // "password" and "student" are recorded in the Model, after which the user sees the template or redirect "closedAdmin/adminStudents/editStudent".
    @GetMapping("/student/{id}/edit")
    public String gotoEditStudent(@PathVariable("id") int id, Model model) {
        studentService.manageGoToEditStudent(model, "", id);
        return "closedAdmin/adminStudents/editStudent";
    }

    // Method opens the edit form for a specific student, finds the Student by id, and passes their DTO to the template.
    // "output", "password", and "student" are recorded in the Model, after which the user sees the template or redirect "closedAdmin/adminStudents/editStudent".
    @GetMapping("/student/{id}/edit/{output}")
    public String gotoEditStudentWithOutput(@PathVariable("id") int id, @PathVariable("output") String output, Model model) {
        studentService.manageGoToEditStudent(model, output, id);
        return "closedAdmin/adminStudents/editStudent";
    }

    // Method updates student data, their User fields, and password if a new value is provided instead of OLDPASS/NOPASS.
    // The method calls `userService.checkIfExistsByEmail`, `studentService.getById`, `studentMapper.mapStudentDTOToStudent`, and `studentService.update`, ending with a "redirect:/admin/student/" transition.
    @PostMapping("/student/{id}/edit")
    public String editStudent(
            @PathVariable("id") int id,
            @ModelAttribute("student") StudentDTO student,
            @RequestParam("password") String password,
            Model model) {

        String returnUrl = studentService.manageEditStudent(student, password, id);
        return returnUrl;


    }

    // Method deletes a student or removes them from a specific teacher's list along with related lessons and courses.
    // The method calls `studentService.getById`, `tswService.removeAllTswByStudentId`, `lessonService.deleteAllLessonsByStudent`, `studentCourseService.deleteAllCoursesByStudent`, `commentService.deleteAllByStudent`, and others, ending with a "redirect:/admin/students/STUDENT_DELETED" transition.
    @PostMapping("/student/delete")
    @Transactional
    public String deleteStudent(@RequestParam("studentId") int id) {
        studentService.manageDeleteStudent(id);
        return "redirect:/admin/students/STUDENT_DELETED";
    }

}
