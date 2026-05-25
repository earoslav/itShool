package com.example.demo.controlers.closedStudent;

import com.example.demo.dto.adminLessons.LessonAdminLessonsDTO;
import com.example.demo.mapper.entity.StudentMapper;
import com.example.demo.mapper.entity.TeacherMapper;
import com.example.demo.mapper.programe.CourseMapper;
import com.example.demo.mapper.programe.LessonMapper;
import com.example.demo.models.email.Mail;
import com.example.demo.models.entities.Student;
import com.example.demo.models.other.TimeOfTheWeek;
import com.example.demo.models.programe.Lesson;
import com.example.demo.services.Statuses;
import com.example.demo.services.email.MailService;
import com.example.demo.services.entities.StudentService;
import com.example.demo.services.entities.TeacherService;
import com.example.demo.services.other.TimeOfTheWeekService;
import com.example.demo.services.program.CourseService;
import com.example.demo.services.program.LessonService;
import com.example.demo.services.thirdTable.EmptyTimesForTeacherService;
import com.example.demo.services.thirdTable.TeacherCourseService;
import com.example.demo.services.thirdTable.TeacherStudentTimeOfTheWeekService;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

// StudentLessonsController handles web pages for the "lessons" module.
// The methods below accept parameters from URLs or forms, call project services, and return the required Thymeleaf templates or redirects.
@Controller
@RequestMapping("/student")
public class StudentLessonsController {
    private TeacherService teacherService;
    private LessonMapper lessonMapper;
    private TimeOfTheWeekService theWeekService;
    private CourseService courseService;
    private EmptyTimesForTeacherService emptyTimesForTeacherService;
    private LessonService lessonService;
    private TeacherCourseService teacherCourseService;
    private MailService mailService;
    private TeacherStudentTimeOfTheWeekService tswService;
    private StudentService studentService;
    private ObjectMapper objectMapper;
    private StudentMapper studentMapper;
    private TeacherMapper teacherMapper;
    private CourseMapper courseMapper;
    // Receives dependencies through Spring: TeacherService, LessonMapper, TimeOfTheWeekService, CourseService, EmptyTimesForTeacherService, LessonService, TeacherCourseService, and others.
    // These services and mappers are required by the class methods to work with the "lessons" module without manual object creation.
    public StudentLessonsController(TeacherService teacherService, LessonMapper lessonMapper, TimeOfTheWeekService theWeekService, CourseService courseService, EmptyTimesForTeacherService emptyTimesForTeacherService, LessonService lessonService, TeacherCourseService teacherCourseService, MailService mailService, TeacherStudentTimeOfTheWeekService tswService, StudentService studentService, ObjectMapper objectMapper, StudentMapper studentMapper, TeacherMapper teacherMapper, CourseMapper courseMapper) {
        this.teacherService = teacherService;
        this.lessonMapper = lessonMapper;
        this.theWeekService = theWeekService;
        this.courseService = courseService;
        this.emptyTimesForTeacherService = emptyTimesForTeacherService;
        this.lessonService = lessonService;
        this.teacherCourseService = teacherCourseService;
        this.mailService = mailService;
        this.tswService = tswService;
        this.studentService = studentService;
        this.objectMapper = objectMapper;
        this.studentMapper = studentMapper;
        this.teacherMapper = teacherMapper;

        this.courseMapper = courseMapper;
    }
    // Opens the GET /{idSt}/lessons route and prepares data for the "closedStudent/studentLessons/lessons" template.
    // Adds "lessons", "weekDays", "student", and "hours" to the Model; retrieves data via `studentService.getById`, `lessonService.compileLessonsForStudent`, and `studentMapper.mapStudentToStudentDTO`.
    @GetMapping("/{idSt}/lessons")
    public String getLessonsForStudent(@PathVariable("idSt") int idSt, Model model) throws JsonProcessingException {
        Student student = studentService.getById(idSt);

        HashMap<String, LessonAdminLessonsDTO> lessonHashMap = lessonService.compileLessonsForStudent(student.getId());
        List<String> weekDays = theWeekService.compileWeekDays();


        model.addAttribute("lessons", lessonHashMap);
        model.addAttribute("weekDays", weekDays);
        model.addAttribute("student", studentMapper.mapStudentToStudentDTO(student));
        model.addAttribute("hours", Arrays.asList("8:00", "8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30"));
        return "closedStudent/studentLessons/lessons";
    }
    // Opens the GET /{idSt}/lessons/{output} route and prepares data for the "closedStudent/studentLessons/lessons" template.
    // Adds "lessons", "weekDays", "output", "student", and "hours" to the Model; retrieves data via `studentService.getById`, `lessonService.compileLessonsForStudent`, and `studentMapper.mapStudentToStudentDTO`.
    @GetMapping("/{idSt}/lessons/{output}")
    public String getLessonsForStudent(@PathVariable("idSt") int idSt, @PathVariable("output") String output, Model model) throws JsonProcessingException {
        Student student = studentService.getById(idSt);

        HashMap<String, LessonAdminLessonsDTO> lessonHashMap = lessonService.compileLessonsForStudent(student.getId());
        List<String> weekDays = theWeekService.compileWeekDays();


        model.addAttribute("lessons", lessonHashMap);
        model.addAttribute("weekDays", weekDays);
        model.addAttribute("output", output);
        model.addAttribute("student", studentMapper.mapStudentToStudentDTO(student));
        model.addAttribute("hours", Arrays.asList("8:00", "8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30"));
        return "closedStudent/studentLessons/lessons";
    }
    // Deletes or disconnects data at the POST /{idSt}/lessons/deleteLesson/{id} route in the "lessons" module.
    // Calls `lessonService.getById`, `studentService.getById`, `mailService.sendRequestWithThymeleafTeacherAboutRequestLessonRemoved`, and `studentMapper.mapStudentToStudentDTO`; returns redirects to "/student/".
    @PostMapping("/{idSt}/lessons/deleteLesson/{id}")
    @Transactional
    public String deleteLesson(@PathVariable("idSt") int idSt, @PathVariable("id") int idLes, Model model) throws MessagingException {
        if (lessonService.getById(idLes).getLessonTime().isAfter(LocalDateTime.now().plusHours(12))) {
            lessonService.manageRequestDeleteLessonByStudent(idSt, idLes);

            return "redirect:/student/" + idSt + "/lessons/LESSON_DELETED";
        } else {
            return "redirect:/student/" + idSt + "/lessons/TOO_LATE";
        }

    }
    // Updates data at the POST /{idSt}/lessons/editLesson/notPicked route in the "lessons" module.
    // Returns redirects to "/student/" upon completion.
    @PostMapping("/{idSt}/lessons/editLesson/notPicked")
    @Transactional
    public String manageNotPickedItemInEdit(@PathVariable("idSt") int idSt) {
        return "redirect:/student/" + idSt + "/lessons/ITEM_NOT_PICKED";
    }
    // Deletes or disconnects data at the POST /{idSt}/lessons/deleteCourse/{id} route in the "lessons" module.
    // Calls `lessonService.getById`, `studentService.getById`, `mailService.sendEmailWithThymeleafTeacherAboutCourseRemoved`, `lessonService.deleteAllByStIdAndTeachIdAndTswIdAndLesTimeAfterNow`, `tswService.removeAllByStIdAndTeachIdAndTswId`, and others; returns redirects to "/student/".
    @PostMapping("/{idSt}/lessons/deleteCourse/{id}")
    @Transactional
    public String deleteCourse(@PathVariable("idSt") int idSt, @PathVariable("id") int idLes) throws MessagingException {
        lessonService.manageDeleteCourseByStudent(idSt, idLes);
        return "redirect:/student/" + idSt + "/lessons/COURSE_DELETED";
    }

    // Deletes or disconnects data at the POST /{idSt}/lessons/editLesson/{id}/{nTId}/{date} route in the "lessons" module.
    // Calls `lessonService.getById`, `lessonService.checkIfLessonValidWithReputitions`, `studentService.getById`, `mailService.sendEmailWithThymeleafToTeacherAboutLessonTimeEdited`, `lessonService.update`, and others; works with SUCCESS statuses; returns redirects to "/student/".
    @PostMapping("/{idSt}/lessons/editLesson/{id}/{nTId}/{date}")
    public String deleteCourse(@PathVariable("idSt") int idSt, @PathVariable("id") int idLes, @PathVariable("date") String date, Model model) throws MessagingException, JsonProcessingException {
        Lesson lesson = lessonService.getById(idLes);
        List<Object> objs = lessonService.checkIfLessonValidWithReputitions(lesson.getTeacher().getId(), lesson.getStudent().getId(), lesson.getCourse().getId(), idLes, lesson.getDuration(), date, 1);
        Statuses status = (Statuses) objs.get(0);
        LocalDateTime newDate = (LocalDateTime) objs.get(1);
        if (status == Statuses.SUCCESS) {
            Student student = studentService.getById(idSt);
            mailService.sendEmailWithThymeleafToTeacherAboutLessonTimeEdited(studentMapper.mapStudentToStudentDTO(student), lesson.getLessonTime(), newDate, lesson.getDuration(), "Lesson time changed", Collections.singletonList(lesson.getTeacher().getEmail()));
            lesson.setLessonTime(newDate);
            lessonService.update(lesson.getId(), lesson);
            return "redirect:/student/" + idSt + "/lessons/LESSON_EDITED";
        } else {
            return "redirect:/student/" + idSt + "/lessons/" + status.name();
        }
    }
}
