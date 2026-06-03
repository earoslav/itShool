package com.example.demo.controlers.closedStudent;

import com.example.demo.services.entities.StudentService;
import com.example.demo.services.program.LessonService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// StudentLessonsController handles web pages for the "lessons" module.
// The methods below accept parameters from URLs or forms, call project services, and return the required Thymeleaf templates or redirects.
@Controller
@RequestMapping("/student")
public class StudentLessonsController {
    private final StudentService studentService;
    private final LessonService lessonService;

    // Receives dependencies through Spring: StudentService and LessonService.
    public StudentLessonsController(StudentService studentService, LessonService lessonService) {
        this.studentService = studentService;
        this.lessonService = lessonService;
    }

    // Opens the GET /{idSt}/lessons route and prepares data for the "closedStudent/studentLessons/lessons" template.
    @GetMapping("/{idSt}/lessons")
    public String getLessonsForStudent(@PathVariable("idSt") int idSt, @RequestParam(value = "days", required = false) Integer days, Model model) throws JsonProcessingException {
        lessonService.manageGoToStudentLessons(idSt, "", model, days);
        return "closedStudent/studentLessons/lessons";
    }

    // Opens the GET /{idSt}/lessons/{output} route and prepares data for the "closedStudent/studentLessons/lessons" template.
    @GetMapping("/{idSt}/lessons/{output}")
    public String getLessonsForStudent(@PathVariable("idSt") int idSt, @PathVariable("output") String output, @RequestParam(value = "days", required = false) Integer days, Model model) throws JsonProcessingException {
        lessonService.manageGoToStudentLessons(idSt, output, model, days);
        return "closedStudent/studentLessons/lessons";
    }

    // Deletes or disconnects data at the POST /{idSt}/lessons/deleteLesson/{id} route in the "lessons" module.
    @PostMapping("/{idSt}/lessons/deleteLesson/{id}")
    public String deleteLesson(@PathVariable("idSt") int idSt, @PathVariable("id") int idLes, Model model) throws MessagingException {
        String result = lessonService.manageDeleteLessonByStudentCabinet(idSt, idLes);
        return "redirect:/student/" + idSt + "/lessons/" + result;
    }

    // Updates data at the POST /{idSt}/lessons/editLesson/notPicked route in the "lessons" module.
    @PostMapping("/{idSt}/lessons/editLesson/notPicked")
    public String manageNotPickedItemInEdit(@PathVariable("idSt") int idSt) {
        return "redirect:/student/" + idSt + "/lessons/ITEM_NOT_PICKED";
    }

    // Deletes or disconnects data at the POST /{idSt}/lessons/deleteCourse/{id} route in the "lessons" module.
    @PostMapping("/{idSt}/lessons/deleteCourse/{id}")
    public String deleteCourse(@PathVariable("idSt") int idSt, @PathVariable("id") int idLes) throws MessagingException {
        lessonService.manageDeleteCourseByStudent(idSt, idLes);
        return "redirect:/student/" + idSt + "/lessons/COURSE_DELETED";
    }

    // Deletes or disconnects data at the POST /{idSt}/lessons/editLesson/{id}/{nTId}/{date} route in the "lessons" module.
    @PostMapping("/{idSt}/lessons/editLesson/{id}/{nTId}/{date}")
    public String editLesson(@PathVariable("idSt") int idSt, @PathVariable("id") int idLes, @PathVariable("date") String date, Model model) throws MessagingException, JsonProcessingException {
        String result = lessonService.manageEditLessonByStudent(idLes, idSt, date);
        return "redirect:/student/" + idSt + "/lessons/" + result;
    }
}
