package com.example.demo.controlers.closedTeacher.teacherStudent;

import com.example.demo.services.entities.TeacherService;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

// TeacherStudentsController handles web pages for student management inside the "teachers" module.
@Controller
@RequestMapping("/teacher")
public class TeacherStudentsController {
    private final TeacherService teacherService;

    // Receives dependency through Spring: TeacherService.
    public TeacherStudentsController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    // Opens the GET /{idTeach}/students route and prepares data for the "closedTeacher/teacherStudents/students" template.
    @GetMapping("/{idTeach}/students")
    public String gotoMyStudents(@PathVariable("idTeach") int id, @RequestParam(value = "limit", required = false) String limit, Model model){
        teacherService.manageGotoMyStudents(id, "", model, limit);
        return "closedTeacher/teacherStudents/students";
    }

    // Opens the GET /{idTeach}/students/{output} route and prepares data for the "closedTeacher/teacherStudents/students" template.
    @GetMapping("/{idTeach}/students/{output}")
    public String gotoMyStudentsWithOutput(@PathVariable("idTeach") int id, @PathVariable("output") String output, @RequestParam(value = "limit", required = false) String limit, Model model) {
        teacherService.manageGotoMyStudents(id, output, model, limit);
        return "closedTeacher/teacherStudents/students";
    }

    // Deletes or disconnects data at the POST /{idTeach}/student/{idSt}/delete route in the "teachers" module.
    @PostMapping("/{idTeach}/student/{idSt}/delete")
    public String deleteStudent(@PathVariable("idTeach") int idT, @PathVariable("idSt") int idSt, Model model) throws MessagingException {
        teacherService.manageDeleteStudentByTeacher(idT, idSt, model);
        return "redirect:/teacher/" + idT + "/students/STUDENT_DELETED";
    }
}
