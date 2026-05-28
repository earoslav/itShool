package com.example.demo.controlers.closedTeacher.teacherStudent;

import com.example.demo.services.entities.TeacherService;
import com.example.demo.services.program.LessonService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

// TeacherLessonsWithStudentController handles web pages for the "lessons" module between a teacher and a student.
// The methods below accept parameters from URLs or forms, call project services, and return the required Thymeleaf templates or redirects.
@Controller
@RequestMapping("/teacher")
public class TeacherLessonsWithStudentController {
    private final TeacherService teacherService;
    private final LessonService lessonService;

    // Receives dependencies through Spring: TeacherService and LessonService.
    public TeacherLessonsWithStudentController(TeacherService teacherService, LessonService lessonService) {
        this.teacherService = teacherService;
        this.lessonService = lessonService;
    }

    // Opens the GET /{idTeach}/student/{idSt}/lessons route and prepares data for the "closedTeacher/teacherStudents/studentLessons" template.
    @GetMapping("/{idTeach}/student/{idSt}/lessons")
    public String getLessonsFromTeacherForStudent(@PathVariable("idTeach") int idTeach, @PathVariable("idSt") int idSt, @RequestParam(value = "days", required = false) Integer days, Model model) throws JsonProcessingException {
        lessonService.manageGoToTeacherStudentLessonsPage(idTeach, idSt, "", model, days);
        return "closedTeacher/teacherStudents/studentLessons";
    }

    // Opens the GET /{idTeach}/student/{idSt}/lessons/{output} route and prepares data for the "closedTeacher/teacherStudents/studentLessons" template.
    @GetMapping("/{idTeach}/student/{idSt}/lessons/{output}")
    public String getLessonsFromTeacherForStudent(@PathVariable("idTeach") int idTeach, @PathVariable("idSt") int idSt, @PathVariable("output") String output, @RequestParam(value = "days", required = false) Integer days, Model model) throws JsonProcessingException {
        lessonService.manageGoToTeacherStudentLessonsPage(idTeach, idSt, output, model, days);
        return "closedTeacher/teacherStudents/studentLessons";
    }

    // Deletes or disconnects data at the POST /{idTeach}/student/lessons/deleteLesson/{id} route in the "lessons" module.
    @PostMapping("/{idTeach}/student/lessons/deleteLesson/{id}")
    public String deleteLessonForStudent(@PathVariable("idTeach") int idTeach, @PathVariable("id") int idLes) throws MessagingException {
        int studentId = lessonService.getById(idLes).getStudent().getId();
        lessonService.manageDeleteLessonByTeacher(idTeach, idLes);
        return "redirect:/teacher/" + idTeach + "/student/" + studentId + "/lessons/LESSON_DELETED";
    }

    // Updates data at the POST /{idTeach}/student/{stId}/lessons/editLesson/notPicked route in the "lessons" module.
    @PostMapping("/{idTeach}/student/{stId}/lessons/editLesson/notPicked")
    public String editLessoneFromTeacherWithNotPicked(@PathVariable("idTeach") int idTeach, @PathVariable("stId") int stId) {
        return "redirect:/teacher/" + idTeach + "/student/" + stId + "/lessons/ITEM_NOT_PICKED";
    }

    // Deletes or disconnects data at the POST /{idTeach}/student/lessons/deleteCourse/{id} route in the "lessons" module.
    @PostMapping("/{idTeach}/student/lessons/deleteCourse/{id}")
    public String deleteCourseForStudent(@PathVariable("idTeach") int idTeach, @PathVariable("id") int idLes) throws MessagingException {
        int studentId = lessonService.getById(idLes).getStudent().getId();
        lessonService.manageDeleteCourseByTeacher(idTeach, idLes);
        return "redirect:/teacher/" + idTeach + "/student/" + studentId + "/lessons/COURSE_DELETED";
    }

    // Updates data at the POST /{idTeach}/student/lessons/editLesson/{id}/{nTId}/{date} route in the "lessons" module.
    @PostMapping("/{idTeach}/student/lessons/editLesson/{id}/{nTId}/{date}")
    public String editLessonForStudent(@PathVariable("idTeach") int idTeach, @PathVariable("id") int idLes, @PathVariable("date") String date, Model model) throws MessagingException {
        int studentId = lessonService.getById(idLes).getStudent().getId();
        String result = lessonService.manageEditLessonByTeacher(idLes, idTeach, date);
        return "redirect:/teacher/" + idTeach + "/student/" + studentId + "/lessons/" + result;
    }

    // Creates data at the POST /{teachId}/student/{stId}/lessons/addLesson/{cId}/{retDate}/{dur} route in the "lessons" module.
    @PostMapping("/{teachId}/student/{stId}/lessons/addLesson/{cId}/{retDate}/{dur}")
    public String addLessonToStudent(@PathVariable("teachId") int idTeach, @PathVariable("cId") int cId, @PathVariable("stId") int stId, @PathVariable("retDate") String date, @PathVariable("dur") float dur, Model model) throws MessagingException {
        String result = lessonService.manageAddLessonToTeacher(idTeach, stId, cId, dur, date);
        return "redirect:/teacher/" + idTeach + "/student/" + stId + "/lessons/" + result;
    }

    // Updates data at the POST /{teachId}/student/{stId}/lessons/addCourse/{cId}/{retDate}/{dur} route in the "lessons" module.
    @PostMapping("/{teachId}/student/{stId}/lessons/addCourse/{cId}/{retDate}/{dur}")
    public String addCourseToStudent(@PathVariable("teachId") int idTeach, @PathVariable("stId") int stId, @PathVariable("dur") float dur, @PathVariable("cId") int cId, @PathVariable("retDate") String date, Model model) throws MessagingException {
        String result = lessonService.manageAddCourseToTeacher(idTeach, stId, cId, dur, date);
        return "redirect:/teacher/" + idTeach + "/student/" + stId + "/lessons/" + result;
    }

    // Deletes or disconnects data at the POST /{teacherId}/acceptLessonDelete/{lessonId} route in the "lessons" module.
    @PostMapping("/{teacherId}/acceptLessonDelete/{lessonId}")
    public String acceptLessonDelete(@PathVariable("teacherId") int teacherId, @PathVariable("lessonId") int lessonId, Model model) throws MessagingException {
        lessonService.manageAcceptLessonDeleteByTeacher(teacherId, lessonId);
        return "redirect:/teacher/" + teacherId + "/lessons/LESSON_DELETED";
    }

    // Opens the GET /{teacherId}/denyLessonDelete/{lessonId} route and prepares data for the "redirect:/teacher/" template.
    @GetMapping("/{teacherId}/denyLessonDelete/{lessonId}")
    public String denyLessonDelete(@PathVariable("teacherId") int teacherId, @PathVariable("lessonId") int lessonId, Model model) throws MessagingException {
        lessonService.manageDenyLessonDeleteByTeacher(teacherId, lessonId);
        return "redirect:/teacher/" + teacherId + "/homepage/DELETE_DENYED";
    }

    // Creates data at the POST /{idTeach}/addMoneyByLessonWithStudent/{lesId}/{stId} route in the "lessons" module.
    @PostMapping("/{idTeach}/addMoneyByLessonWithStudent/{lesId}/{stId}")
    public String addMoney(@PathVariable("idTeach") int teachId, @PathVariable("lesId") int lesId, @PathVariable("stId") int stId, Model model) {
        String result = lessonService.manageAddMoneyByTeacherStudent(teachId, lesId);
        return "redirect:/teacher/" + teachId + "/student/" + stId + "/lessons/" + result;
    }
}
