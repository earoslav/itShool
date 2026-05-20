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
        model.addAttribute("student",studentMapper.mapStudentToStudentDTO(new Student()));
        model.addAttribute("password", "");
        return "closedAdmin/adminStudents/addNewStudent";
    }
    // Method creates a student from the admin form, encodes the password using PasswordEncoder, and saves the Student along with the User account.
    // "student", "output", and "error" are recorded in the Model, after which the user sees the template or redirect "closedAdmin/adminStudents/addNewStudent", "redirect:/admin/homepage/STUDENT_ADDED".
    @PostMapping("/addStudent/{password}")
    public String addStudent(@ModelAttribute("student") StudentDTO student,@PathVariable("password") String password, Model model) {
        try {
            if (studentService.checkIfExistsByEmail(student.getUser().getEmail())) {
                if(password.equals("NO_PASS")){
                    model.addAttribute("student");
                    model.addAttribute("output", "NO_PASS");
                    return "closedAdmin/adminStudents/addNewStudent";
                }
                model.addAttribute("output", "EXISTS");
                model.addAttribute("student", student);
                return "closedAdmin/adminStudents/addNewStudent";
            }
            User user = new User(student.getUser().getName(), student.getUser().getEmail(), passwordEncoder.encode(password), "STUDENT");
            userService.create(user);
            Student student1 = studentMapper.mapStudentDTOToStudent(student);
            student1.setUser(user);
            studentService.create(student1);
            return "redirect:/admin/homepage/STUDENT_ADDED";
        } catch (Exception e) {
            System.out.println(e);
            model.addAttribute("output");
            model.addAttribute("error", "GENERAL");
            return "closedAdmin/adminStudents/addNewStudent";
        }


    }
    // Method displays the list of students: for the admin, all Student records; for a teacher, only students linked to them.
    // "students" and "output" are recorded in the Model, after which the user sees the template or redirect "closedAdmin/adminStudents/students".
    @GetMapping("/students/{output}")
    public String gotoStudents(@PathVariable("output") String output, Model model) {
        List<StudentDTO> students = studentService.getAll().stream().map(student -> studentMapper.mapStudentToStudentDTO(student)).collect(Collectors.toList());
        model.addAttribute("students", students);
        model.addAttribute("output", output);
        return "closedAdmin/adminStudents/students";
    }
    // Method displays the list of students: for the admin, all Student records; for a teacher, only students linked to them.
    // "students" is recorded in the Model, after which the user sees the template or redirect "closedAdmin/adminStudents/students".
    @GetMapping("/students")
    public String gotoStudents(Model model) {
        List<StudentDTO> students = studentService.getAll().stream().map(student -> studentMapper.mapStudentToStudentDTO(student)).collect(Collectors.toList());
        model.addAttribute("students", students);
        return "closedAdmin/adminStudents/students";
    }
    // Method opens the edit form for a specific student, finds the Student by id, and passes their DTO to the template.
    // "password" and "student" are recorded in the Model, after which the user sees the template or redirect "closedAdmin/adminStudents/editStudent".
    @GetMapping("/student/{id}/edit")
    public String gotoEditStudent(@PathVariable("id") int id, Model model) {

        model.addAttribute("password", "");
        model.addAttribute("student", studentMapper.mapStudentToStudentDTO(studentService.getById(id)));
        return "closedAdmin/adminStudents/editStudent";
    }
    // Method opens the edit form for a specific student, finds the Student by id, and passes their DTO to the template.
    // "output", "password", and "student" are recorded in the Model, after which the user sees the template or redirect "closedAdmin/adminStudents/editStudent".
    @GetMapping("/student/{id}/edit/{output}")
    public String gotoEditStudentWithOutput(@PathVariable("id") int id,@PathVariable("output") String output, Model model) {
        model.addAttribute("output", output);
        model.addAttribute("password", "");
        model.addAttribute("student", studentMapper.mapStudentToStudentDTO(studentService.getById(id)));
        return "closedAdmin/adminStudents/editStudent";
    }
    // Method updates student data, their User fields, and password if a new value is provided instead of OLDPASS/NOPASS.
    // The method calls `userService.checkIfExistsByEmail`, `studentService.getById`, `studentMapper.mapStudentDTOToStudent`, and `studentService.update`, ending with a "redirect:/admin/student/" transition.
    @PostMapping("/student/{id}/edit/{password}")
    public String editStudent(
            @PathVariable("id") int id,
            @ModelAttribute("student") StudentDTO student,
            @PathVariable("password") String password,
            Model model) {

        try {
            if (userService.checkIfExistsByEmail(student.getUser().getEmail()) && !studentService.getById(id).getEmail().equals(student.getUser().getEmail())) {
                return "redirect:/admin/student/"+id+"/edit/EXISTS";
            }

            Student student1 = studentMapper.mapStudentDTOToStudent(student);
            if(password.equals("OLDPASS")){
                password = studentService.getById(student.getId()).getPassword();
                student1.setPassword(password);
                studentService.update(id, student1);
                return "redirect:/admin/student/"+id+"/edit/STUDENT_EDITED";
            }
            student1.setPassword(passwordEncoder.encode(password));
            studentService.update(id, student1);
            return "redirect:/admin/student/"+id+"/edit/STUDENT_EDITED";
        } catch (Exception e) {
            return "redirect:/admin/student/"+id+"/edit/GENERAL";
        }

    }
    // Method deletes a student or removes them from a specific teacher's list along with related lessons and courses.
    // The method calls `studentService.getById`, `tswService.removeAllTswByStudentId`, `lessonService.deleteAllLessonsByStudent`, `studentCourseService.deleteAllCoursesByStudent`, `commentService.deleteAllByStudent`, and others, ending with a "redirect:/admin/students/STUDENT_DELETED" transition.
    @PostMapping("/student/delete")
    @Transactional
    public String deleteStudent(@RequestParam("studentId") int id) {
        Student student = studentService.getById(id);
        tswService.removeAllTswByStudentId(student.getId());
        lessonService.deleteAllLessonsByStudent(student);

        commentService.deleteAllByStudent(student);
        studentService.deleteById(id);
        return "redirect:/admin/students/STUDENT_DELETED";
    }

}