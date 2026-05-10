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
public class

AdminStudentOtherActionsController {
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
    // Конструктор підключає до AdminStudentOtherActionsController залежності TeacherCourseService, TeacherService, EmptyTimesForTeacherService, TimeOfTheWeekService, CourseService, StudentCourseService та інші.
    // Ці об’єкти далі використовуються в методах для роботи з модулем «студенти», тому клас не створює їх вручну.
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
    // Метод відкриває адміну форму додавання студента і кладе в Model порожній StudentDTO.
    // У Model записуються "student", "password", після чого користувач бачить шаблон або redirect "closedAdmin/adminStudents/addNewStudent".
    @GetMapping("/addStudent")
    public String gotoCreateStudent(Model model) {
        model.addAttribute("student",studentMapper.mapStudentToStudentDTO(new Student()));
        model.addAttribute("password", "");
        return "closedAdmin/adminStudents/addNewStudent";
    }
    // Метод створює студента з адмінської форми, кодує пароль через PasswordEncoder і зберігає Student разом із User-акаунтом.
    // У Model записуються "student", "output", "error", після чого користувач бачить шаблон або redirect "closedAdmin/adminStudents/addNewStudent", "redirect:/admin/homepage/STUDENT_ADDED".
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
    // Метод показує список студентів: адміну всі записи Student, а викладачу лише тих учнів, які прив’язані до нього.
    // У Model записуються "students", "output", після чого користувач бачить шаблон або redirect "closedAdmin/adminStudents/students".
    @GetMapping("/students/{output}")
    public String gotoStudents(@PathVariable("output") String output, Model model) {
        List<StudentDTO> students = studentService.getAll().stream().map(student -> studentMapper.mapStudentToStudentDTO(student)).collect(Collectors.toList());
        model.addAttribute("students", students);
        model.addAttribute("output", output);
        return "closedAdmin/adminStudents/students";
    }
    // Метод показує список студентів: адміну всі записи Student, а викладачу лише тих учнів, які прив’язані до нього.
    // У Model записуються "students", після чого користувач бачить шаблон або redirect "closedAdmin/adminStudents/students".
    @GetMapping("/students")
    public String gotoStudents(Model model) {
        List<StudentDTO> students = studentService.getAll().stream().map(student -> studentMapper.mapStudentToStudentDTO(student)).collect(Collectors.toList());
        model.addAttribute("students", students);
        return "closedAdmin/adminStudents/students";
    }
    // Метод відкриває форму редагування конкретного студента, знаходить Student за id і передає його DTO у шаблон.
    // У Model записуються "password", "student", після чого користувач бачить шаблон або redirect "closedAdmin/adminStudents/editStudent".
    @GetMapping("/student/{id}/edit")
    public String gotoEditStudent(@PathVariable("id") int id, Model model) {

        model.addAttribute("password", "");
        model.addAttribute("student", studentMapper.mapStudentToStudentDTO(studentService.getById(id)));
        return "closedAdmin/adminStudents/editStudent";
    }
    // Метод відкриває форму редагування конкретного студента, знаходить Student за id і передає його DTO у шаблон.
    // У Model записуються "output", "password", "student", після чого користувач бачить шаблон або redirect "closedAdmin/adminStudents/editStudent".
    @GetMapping("/student/{id}/edit/{output}")
    public String gotoEditStudentWithOutput(@PathVariable("id") int id,@PathVariable("output") String output, Model model) {
        model.addAttribute("output", output);
        model.addAttribute("password", "");
        model.addAttribute("student", studentMapper.mapStudentToStudentDTO(studentService.getById(id)));
        return "closedAdmin/adminStudents/editStudent";
    }
    // Метод оновлює дані студента, його User-поля і пароль, якщо замість OLDPASS/NOPASS прийшло нове значення.
    // Для роботи метод викликає userService.checkIfExistsByEmail, studentService.getById, studentMapper.mapStudentDTOToStudent, studentService.update і завершується переходом "redirect:/admin/student/".
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
    // Метод видаляє студента або прибирає його зі списку конкретного викладача разом із пов’язаними уроками та курсами.
    // Для роботи метод викликає studentService.getById, tswService.removeAllTswByStudentId, lessonService.deleteAllLessonsByStudent, studentCourseService.deleteAllCoursesByStudent, commentService.deleteAllByStudent та інші і завершується переходом "redirect:/admin/students/STUDENT_DELETED".
    @PostMapping("/student/delete")
    @Transactional
    public String deleteStudent(@RequestParam("studentId") int id) {
        Student student = studentService.getById(id);
        tswService.removeAllTswByStudentId(student.getId());
        lessonService.deleteAllLessonsByStudent(student);
        studentCourseService.deleteAllCoursesByStudent(student);
        commentService.deleteAllByStudent(student);
        studentService.deleteById(id);
        return "redirect:/admin/students/STUDENT_DELETED";
    }

}