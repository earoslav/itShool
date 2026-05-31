package com.example.demo.controlers.closedTeacher.teacher;

import com.example.demo.dto.entities.TeacherDTO;
import com.example.demo.services.entities.TeacherService;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// TeacherHomepageController handles web pages for the "teachers" module.
// The methods below accept parameters from URLs or forms, call project services, and return the required Thymeleaf templates or redirects.
@Controller
@RequestMapping("/teacher")
public class TeacherHomepageController {
    private final TeacherService teacherService;

    // Receives dependency through Spring: TeacherService.
    public TeacherHomepageController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    // Opens the GET /{idTeach}/homepage route and prepares data for the "closedTeacher/teacherHomepage/homepage" template.
    @GetMapping("/{idTeach}/homepage")
    public String gotoTeacher(@PathVariable("idTeach") int id, Model model) {
        teacherService.manageGotoTeacherHomepage(id, "", model);
        return "closedTeacher/teacherHomepage/homepage";
    }

    // Opens the GET /{idTeach}/homepage/{output} route and prepares data for the "closedTeacher/teacherHomepage/homepage" template.
    @GetMapping("/{idTeach}/homepage/{output}")
    public String gotoTeacher(@PathVariable("idTeach") int id, @PathVariable("output") String output, Model model) {
        teacherService.manageGotoTeacherHomepage(id, output, model);
        return "closedTeacher/teacherHomepage/homepage";
    }

    // Updates data at the POST /{id}/homepage/{password} route in the "teachers" module.
    @PostMapping("/{id}/homepage")
    public String updateTeacher(@PathVariable("id") int id,
                                @ModelAttribute("teacher") TeacherDTO teacher,
                                @RequestParam("password") String password,
                                @RequestParam(value = "freeTimeIds", required = false) List<Integer> freeTimeIds,
                                @RequestParam(value = "newCourseIds", required = false) List<Integer> newCourseIds) throws MessagingException {
        String result = teacherService.manageUpdateTeacher(id, teacher, password, freeTimeIds, newCourseIds);
        return "redirect:/teacher/" + teacher.getId() + "/homepage/" + result;
    }

    @PostMapping("/{teachId}/deleteCourse/{courseId}")
    public String deleteCourse(@PathVariable("courseId") int courseId, @PathVariable("teachId") int teachId) {
        String result = teacherService.manageDeleteCourse(courseId, teachId);
        return "redirect:/teacher/" + teachId + "/homepage/" + result;
    }
}
