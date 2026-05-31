package com.example.demo.controlers.openSource;

import com.example.demo.dto.entities.StudentDTO;
import com.example.demo.dto.entities.TeacherDTO;
import com.example.demo.services.entities.StudentService;
import com.example.demo.services.entities.TeacherService;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// OpenSourceSignUps handles web pages for the "public pages and registration" module.
// The methods below accept parameters from URLs or forms, call project services, and return the required Thymeleaf templates or redirects.
@Controller
@RequestMapping("/openSource")
public class OpenSourceSignUps {
    private final StudentService studentService;
    private final TeacherService teacherService;

    // Receives dependencies through Spring: StudentService and TeacherService.
    public OpenSourceSignUps(StudentService studentService, TeacherService teacherService) {
        this.studentService = studentService;
        this.teacherService = teacherService;
    }

    // Opens the GET /signUpAsStudent route and prepares data for the "openSource/openSourceSignUps/signUpAsStudent" template.
    @GetMapping("/signUpAsStudent")
    public String gotoSignUpAsStudent(Model model){
        studentService.manageGoToStudentHomepage(0, "", model); // Student ID 0 creates new student DTO
        return "openSource/openSourceSignUps/signUpAsStudent";
    }

    // Opens the GET /signUpAsStudent/{output} route and prepares data for the "openSource/openSourceSignUps/signUpAsStudent" template.
    @GetMapping("/signUpAsStudent/{output}")
    public String gotoSignUpAsStudentWithOutput(@PathVariable("output") String output, Model model){
        studentService.manageGoToStudentHomepage(0, output, model);
        return "openSource/openSourceSignUps/signUpAsStudent";
    }

    // Creates data at the POST /signUpAsStudent/{password} route in the "public pages and registration" module.
    @PostMapping("/signUpAsStudent")
    public String signUpAsStudent(@ModelAttribute("student") StudentDTO student, @RequestParam("password") String password, Model model){
        return studentService.manageSignUpAsStudent(student, password);
    }

    // Opens the GET /signUpAsTeacher route and prepares data for the "openSource/openSourceSignUps/signUpAsTeacher" template.
    @GetMapping("/signUpAsTeacher")
    public String gotoSignUpAsTeacher(Model model){
        teacherService.manageGoToSignUpAsTeacher("", model);
        return "openSource/openSourceSignUps/signUpAsTeacher";
    }

    // Opens the GET /signUpAsTeacher/{output} route and prepares data for the "openSource/openSourceSignUps/signUpAsTeacher" template.
    @GetMapping("/signUpAsTeacher/{output}")
    public String gotoSignUpAsTeacherWithOutput(@PathVariable("output") String output, Model model){
        teacherService.manageGoToSignUpAsTeacher(output, model);
        return "openSource/openSourceSignUps/signUpAsTeacher";
    }

    // Creates data at the POST /signUpAsTeacher/{password} route in the "public pages and registration" module.
    @PostMapping("/signUpAsTeacher")
    public String signUpAsTeacher(@ModelAttribute("teacher") TeacherDTO teacher,
                                  @RequestParam("password") String password,
                                  @RequestParam(value = "courseIds", required = false) List<Integer> courses,
                                  @RequestParam(value = "freeTimeIds", required = false) List<Integer> freeTimes) throws MessagingException {
        String result = teacherService.manageSignUpAsTeacher(teacher, password, courses, freeTimes);
        if (result.equals("EXISTS")) {
            return "redirect:/openSource/signUpAsTeacher/EXISTS";
        }
        if (result.equals("NO_PASS")) {
            return "redirect:/openSource/signUpAsTeacher/NO_PASS";
        }
        return "redirect:/openSource/homepage/" + result;
    }
}
