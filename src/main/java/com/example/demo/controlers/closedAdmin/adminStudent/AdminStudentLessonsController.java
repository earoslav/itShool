package com.example.demo.controlers.closedAdmin.adminStudent;

import com.example.demo.dto.adminLessons.LessonAdminLessonsDTO;
import com.example.demo.dto.entities.StudentDTO;
import com.example.demo.mapper.entity.StudentMapper;
import com.example.demo.mapper.entity.TeacherMapper;
import com.example.demo.mapper.other.TimeOfTheWeekMapper;
import com.example.demo.mapper.programe.CourseMapper;
import com.example.demo.mapper.programe.LessonMapper;
import com.example.demo.models.email.Mail;
import com.example.demo.models.entities.Student;
import com.example.demo.models.entities.Teacher;
import com.example.demo.models.programe.Course;
import com.example.demo.models.programe.Lesson;
import com.example.demo.services.Statuses;
import com.example.demo.services.email.MailService;
import com.example.demo.services.entities.AdminService;
import com.example.demo.services.entities.ModelService;
import com.example.demo.services.entities.StudentService;
import com.example.demo.services.entities.TeacherService;
import com.example.demo.services.other.CommentService;
import com.example.demo.services.other.TimeOfTheWeekService;
import com.example.demo.services.program.CourseService;
import com.example.demo.services.program.LessonService;
import com.example.demo.services.thirdTable.EmptyTimesForTeacherService;
import com.example.demo.services.thirdTable.TeacherCourseService;
import com.example.demo.services.thirdTable.TeacherStudentTimeOfTheWeekService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

// AdminStudentLessonsController handles web pages for the "lessons" module.
// The methods below accept parameters from URLs or forms, call project services, and return the required Thymeleaf templates or redirects.
@Controller
@RequestMapping("/admin")
public class AdminStudentLessonsController {
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
    private TeacherMapper teacherMapper;
    private StudentMapper studentMapper;
    private CourseMapper courseMapper;
    private TimeOfTheWeekMapper theWeekMapper;
    private ModelService modelService;
    // Receives dependencies through Spring: TeacherCourseService, TeacherService, EmptyTimesForTeacherService, TimeOfTheWeekService, CourseService, StudentCourseService, CommentService, and others.
    // These services and mappers are required by the class methods to work with the "lessons" module without manual object creation.
    public AdminStudentLessonsController(TeacherCourseService teacherCourseService, TeacherService teacherService, EmptyTimesForTeacherService emptyTimesForTeacherService, TimeOfTheWeekService theWeekService, CourseService courseService, CommentService commentService, LessonService lessonService, StudentService studentService, LessonMapper lessonMapper, TeacherStudentTimeOfTheWeekService tswService, MailService mailService, AdminService adminService, TeacherMapper teacherMapper, StudentMapper studentMapper, CourseMapper courseMapper, TimeOfTheWeekMapper theWeekMapper, ModelService modelService) {
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
        this.teacherMapper = teacherMapper;
        this.studentMapper = studentMapper;
        this.courseMapper = courseMapper;
        this.theWeekMapper = theWeekMapper;
        this.modelService = modelService;
    }
    // Opens the GET /student/{id}/lessons route and prepares data for the "closedAdmin/adminStudents/studentLessons" template.
    // Adds "lessons", "courses", "weekDays", "teachers", "student", and others to the Model; retrieves data via `teacherService.getAll`, `studentService.getById`, `lessonService.compileLessonsForStudentInAdmin`, `courseService.getAll`, `courseMapper.mapCourseToCourseDTO`, and others.
    @GetMapping("/student/{id}/lessons")
    public String getLessonsForStudent(@PathVariable("id") int id, @RequestParam(value = "days", required = false) Integer days, Model model) throws JsonProcessingException {
       lessonService.manageGetLessonsForStudent(id, model, days);
        return "closedAdmin/adminStudents/studentLessons";
    }
    // Opens the GET /student/{idSt}/lessons/{output} route and prepares data for the "closedAdmin/adminStudents/studentLessons" template.
    // Adds "lessonDurations", "lessons", "courses", "weekDays", "output", "teachers", and others to the Model; retrieves data via `teacherService.getAll`, `studentService.getById`, `lessonService.compileLessonsForStudentInAdmin`, `courseService.getAll`, `courseMapper.mapCourseToCourseDTO`, and others.
    @GetMapping("/student/{idSt}/lessons/{output}")
    public String gotoStudentLessonsWithOutput(@PathVariable("idSt") int idSt, @PathVariable(value = "output", required = false) String output, @RequestParam(value = "days", required = false) Integer days, Model model) throws JsonProcessingException {
        modelService.compileModelForStudentLessonsInAdmin(model, idSt, output, days);
        return "closedAdmin/adminStudents/studentLessons";
    }
    // Deletes or disconnects data at the POST /student/{idSt}/lessons/deleteLesson/{id} route in the "lessons" module.
    // Calls `lessonService.getById`, `studentService.getById`, `mailService.sendEmailWithThymeleafTeacherAboutLessonRemoved`, `lessonService.deleteById`, `studentMapper.mapStudentToStudentDTO`; returns "redirect:/admin/student/" upon completion.
    @PostMapping("/student/{idSt}/lessons/deleteLesson/{id}")
    @Transactional
    public String deleteLesson(@PathVariable("idSt") int idSt, @PathVariable("id") int idLes) throws MessagingException {
        lessonService.manageDeleteLessonByStudentInAdmin(idSt, idLes);
        return "redirect:/admin/student/" + idSt + "/lessons/LESSON_DELETED";
    }
    // Deletes or disconnects data at the POST /student/{idSt}/lessons/deleteCourse/{id} route in the "lessons" module.
    // Calls `lessonService.getById`, `studentService.getById`, `mailService.sendEmailWithThymeleafTeacherAboutCourseRemoved`, `lessonService.deleteAllByStIdAndTeachIdAndTswIdAndLesTimeAfterNow`, `tswService.removeAllByStIdAndTeachIdAndTswId`, and others; returns "redirect:/admin/student/" upon completion.
    @PostMapping("/student/{idSt}/lessons/deleteCourse/{id}")
    @Transactional
    public String deleteCourse(@PathVariable("idSt") int idSt, @PathVariable("id") int idLes) throws MessagingException {
        lessonService.manageDeleteCourseByStudent(idSt, idLes);
        return "redirect:/admin/student/" + idSt + "/lessons/COURSE_DELETED";
    }
    // Updates data at the POST /student/{idSt}/lessons/editLesson/notPicked route in the "lessons" module.
    // Returns "redirect:/admin/student/" upon completion.
    @PostMapping("/student/{idSt}/lessons/editLesson/notPicked")
    @Transactional
    public String manageNotPickedItemInEdit(@PathVariable("idSt") int idSt) {
        return "redirect:/admin/student/" + idSt + "/lessons/ITEM_NOT_PICKED";
    }

    // Updates data at the POST /student/{idSt}/lessons/editLesson/{id}/{nTId}/{date} route in the "lessons" module.
    // Calls `lessonService.getById`, `lessonService.checkIfLessonValidWithReputitions`, `studentService.getById`, `mailService.sendEmailWithThymeleafToTeacherAboutLessonTimeEdited`, `lessonService.update`, and others; works with SUCCESS statuses; returns "redirect:/admin/student/" upon completion.
    @PostMapping("/student/{idSt}/lessons/editLesson/{id}/{nTId}/{date}")
    @Transactional
    public String editLesson(@PathVariable("idSt") int idSt, @PathVariable("id") int idLes, @PathVariable("date") String date, Model model) throws MessagingException {
        String returnUrl = "redirect:/admin/student/" + idSt + "/lessons/" + lessonService.manageEditLessonByStudent(idLes, idSt, date);
        return returnUrl;
    }
}
