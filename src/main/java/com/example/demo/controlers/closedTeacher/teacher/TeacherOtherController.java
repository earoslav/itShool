package com.example.demo.controlers.closedTeacher.teacher;

import com.example.demo.services.entities.TeacherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

// TeacherOtherController handles other auxiliary web pages for the "teachers" module.
@Controller
@RequestMapping("/teacher")
public class TeacherOtherController {
    private final TeacherService teacherService;

    // Receives dependency through Spring: TeacherService.
    public TeacherOtherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping("/{teachId}/courses")
    public String gotoTeacherCourses(Model model, @PathVariable("teachId") int id){
        teacherService.manageGotoTeacherCourses(id, model);
        return "closedTeacher/teacherOther/courses";
    }
}