package com.example.demo.controlers.closedStudent;

import com.example.demo.dto.entities.StudentDTO;
import com.example.demo.services.entities.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// StudentHomepageController handles web pages for the "students" module.
// The methods below accept parameters from URLs or forms, call project services, and return the required Thymeleaf templates or redirects.
@Controller
@RequestMapping("/student")
public class StudentHomepageController {
    private final StudentService studentService;

    // Receives dependencies through Spring: StudentService.
    public StudentHomepageController(StudentService studentService) {
        this.studentService = studentService;
    }

    // Opens the GET /{idSt}/homepage route and prepares data for the "closedStudent/studentHomepage/homepage" template.
    @GetMapping("/{idSt}/homepage")
    public String gotoStudent(@PathVariable("idSt") int id, Model model){
        studentService.manageGoToStudentHomepage(id, "", model);
        return "closedStudent/studentHomepage/homepage";
    }

    // Opens the GET /{idSt}/homepage/{output} route and prepares data for the "closedStudent/studentHomepage/homepage" template.
    @GetMapping("/{idSt}/homepage/{output}")
    public String gotoStudentWithOutput(@PathVariable("idSt") int id, @PathVariable("output") String output, Model model){
        studentService.manageGoToStudentHomepage(id, output, model);
        return "closedStudent/studentHomepage/homepage";
    }

    // Updates data at the POST /{idSt}/homepage/{password} route in the "students" module.
    @PostMapping("/{idSt}/homepage/{password}")
    public String updateStudent(@ModelAttribute("student") StudentDTO student, @PathVariable("password") String password, @PathVariable("idSt") int id, Model model) {
        return studentService.manageUpdateStudentHomepage(student, password, id);
    }
}
