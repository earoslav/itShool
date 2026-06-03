package com.example.demo.controlers.closedTeacher.teacher;

import com.example.demo.services.entities.TeacherService;
import com.example.demo.services.program.LessonService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// TeacherLessonsController handles web pages for the "lessons" module inside the teacher cabinet.
// The methods below accept parameters from URLs or forms, call project services, and return the required Thymeleaf templates or redirects.
@Controller
@RequestMapping("/teacher")
public class TeacherLessonsController {
    private final TeacherService teacherService;
    private final LessonService lessonService;

    // Receives dependencies through Spring: TeacherService and LessonService.
    public TeacherLessonsController(TeacherService teacherService, LessonService lessonService) {
        this.teacherService = teacherService;
        this.lessonService = lessonService;
    }

    // Opens the GET /{id}/lessons route and prepares data for the "closedTeacher/teacherLessons/lessons" template.
    @GetMapping("/{idTeach}/lessons")
    public String getLessonsForTeacher(@PathVariable("idTeach") int idTeach, @RequestParam(value = "days", required = false) Integer days, Model model) throws JsonProcessingException {
        lessonService.manageGoToTeacherLessonsPage(idTeach, "", model, days);
        return "closedTeacher/teacherLessons/lessons";
    }

    // Updates data at the POST /{idTeach}/lessons/editLesson/notPicked route in the "lessons" module.
    @PostMapping("/{idTeach}/lessons/editLesson/notPicked")
    public String editLessoneFromTeacherWithNotPicked(@PathVariable("idTeach") int idTeach) {
        return "redirect:/teacher/" + idTeach + "/lessons/ITEM_NOT_PICKED";
    }

    // Opens the GET /{id}/lessons/{output} route and prepares data for the "closedTeacher/teacherLessons/lessons" template.
    @GetMapping("/{idTeach}/lessons/{output}")
    public String getLessonsForTeacher(@PathVariable("idTeach") int idTeach, @PathVariable("output") String output, @RequestParam(value = "days", required = false) Integer days, Model model) throws JsonProcessingException {
        lessonService.manageGoToTeacherLessonsPage(idTeach, output, model, days);
        return "closedTeacher/teacherLessons/lessons";
    }

    // Deletes or disconnects data at the POST /{idTeach}/lessons/deleteLesson/{id} route in the "lessons" module.
    @PostMapping("/{idTeach}/lessons/deleteLesson/{id}")
    public String deleteLesson(@PathVariable("idTeach") int idTeach, @PathVariable("id") int idLes) throws MessagingException {
        lessonService.manageDeleteLessonByTeacher(idTeach, idLes);
        return "redirect:/teacher/" + idTeach + "/lessons/LESSON_DELETED";
    }

    // Deletes or disconnects data at the POST /{idTeach}/lessons/deleteCourse/{id} route in the "lessons" module.
    @PostMapping("/{idTeach}/lessons/deleteCourse/{id}")
    public String deleteCourse(@PathVariable("idTeach") int idTeach, @PathVariable("id") int idLes) throws MessagingException {
        lessonService.manageDeleteCourseByTeacher(idTeach, idLes);
        return "redirect:/teacher/" + idTeach + "/lessons/COURSE_DELETED";
    }

    // Updates data at the POST /{idTeach}/lessons/editLesson/{id}/{nTId}/{date} route in the "lessons" module.
    @PostMapping("/{idTeach}/lessons/editLesson/{id}/{nTId}/{date}")
    public String editLesson(@PathVariable("idTeach") int idTeach, @PathVariable("id") int idLes, @PathVariable("date") String date, Model model) throws MessagingException {
        String result = lessonService.manageEditLessonByTeacher(idLes, idTeach, date);
        return "redirect:/teacher/" + idTeach + "/lessons/" + result;
    }

    // Creates data at the POST /{teachId}/lessons/addLesson/{cId}/{stId}/{retDate}/{dur} route in the "lessons" module.
    @PostMapping("/{teachId}/lessons/addLesson/{cId}/{stId}/{retDate}/{dur}")
    public String addLesson(@PathVariable("teachId") int idTeach, @PathVariable("cId") int cId, @PathVariable("stId") int stId, @PathVariable("retDate") String date, @PathVariable("dur") float dur, Model model) throws MessagingException {
        String result = lessonService.manageAddLessonToTeacher(idTeach, stId, cId, dur, date);
        return "redirect:/teacher/" + idTeach + "/lessons/" + result;
    }

    // Creates data at the POST /{teachId}/lessons/addCourse/{cId}/{stId}/{retDate}/{dur} route in the "lessons" module.
    @PostMapping("/{teachId}/lessons/addCourse/{cId}/{stId}/{retDate}/{dur}")
    public String addCourse(@PathVariable("teachId") int idTeach, @PathVariable("cId") int cId, @PathVariable("stId") int stId, @PathVariable("retDate") String date, @PathVariable("dur") float dur, Model model) throws MessagingException {
        String result = lessonService.manageAddCourseToTeacher(idTeach, stId, cId, dur, date);
        return "redirect:/teacher/" + idTeach + "/lessons/" + result;
    }

    // Creates data at the POST /{idTeach}/addMoneyByLesson/{lesId} route in the "lessons" module.
    @PostMapping("/{idTeach}/addMoneyByLesson/{lesId}")
    public String addMoney(@PathVariable("idTeach") int teachId, @PathVariable("lesId") int lesId, Model model) {
        String result = lessonService.manageAddMoneyByTeacher(teachId, lesId);
        return "redirect:/teacher/" + teachId + "/lessons/" + result;
    }
}
