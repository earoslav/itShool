package com.example.demo.controlers.closedAdmin.adminTeacher;

import com.example.demo.dto.adminLessons.LessonAdminLessonsDTO;
import com.example.demo.mapper.entity.StudentMapper;
import com.example.demo.mapper.entity.TeacherMapper;
import com.example.demo.mapper.programe.CourseMapper;
import com.example.demo.mapper.programe.LessonMapper;
import com.example.demo.models.email.Mail;
import com.example.demo.models.entities.Student;
import com.example.demo.models.entities.Teacher;
import com.example.demo.models.programe.Course;
import com.example.demo.models.programe.Lesson;
import com.example.demo.models.thirdTables.TeacherStudentTimeOfTheWeek;
import com.example.demo.services.Statuses;
import com.example.demo.services.email.MailService;
import com.example.demo.services.entities.AdminService;
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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

// AdminTeacherLessonWithStudentController handles web pages for the "lessons" module.
// The methods below accept parameters from URLs or forms, call project services, and return the required Thymeleaf templates or redirects.
@Controller
@RequestMapping("/admin")
public class AdminTeacherLessonWithStudentController {
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
    private CourseMapper courseMapper;
    private StudentMapper studentMapper;
    private TeacherMapper teacherMapper;
    // Receives dependencies through Spring: TeacherCourseService, TeacherService, EmptyTimesForTeacherService, TimeOfTheWeekService, CourseService, StudentCourseService, CommentService, and others.
    // These services and mappers are required by the class methods to work with the "lessons" module without manual object creation.
    public AdminTeacherLessonWithStudentController(TeacherCourseService teacherCourseService, TeacherService teacherService, EmptyTimesForTeacherService emptyTimesForTeacherService, TimeOfTheWeekService theWeekService, CourseService courseService, CommentService commentService, LessonService lessonService, StudentService studentService, LessonMapper lessonMapper, TeacherStudentTimeOfTheWeekService tswService, MailService mailService, AdminService adminService, CourseMapper courseMapper, StudentMapper studentMapper, TeacherMapper teacherMapper) {
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
        this.courseMapper = courseMapper;

        this.studentMapper = studentMapper;
        this.teacherMapper = teacherMapper;
    }
    // Opens the GET /teacher/{teachId}/lessonsWithStudent/{stId} route and prepares data for the "closedAdmin/adminTeachers/teacherStudentLessons" template.
    // Adds "lessons", "courses", "weekDays", "teacher", "student", and others to the Model; retrieves data via `teacherService.getById`, `studentService.getById`, `lessonService.compileLessonsForStudentAndTeacher`, `courseMapper.mapCourseToCourseDTO`, `teacherMapper.mapTeacherToTeacherDTO`, and others; returns "redirect:/admin/teacher/" if necessary.
    @GetMapping("/teacher/{idTeach}/lessonsWithStudent/{idSt}")
    public String getLessonsFromTeacherForStudent(@PathVariable("idTeach") int idTeach, @PathVariable("idSt") int idSt, Model model) throws JsonProcessingException {
        if (idSt == 0) {
            return "redirect:/admin/teacher/" + idTeach + "/info/notPicked";
        }
        Teacher teacher = teacherService.getById(idTeach);
        Student student = studentService.getById(idSt);

        HashMap<String, LessonAdminLessonsDTO> lessonHashMap = lessonService.compileLessonsForStudentAndTeacher(idTeach, student.getId());
        List<String> weekDays = theWeekService.compileWeekDays();
        List<Course> courses = new ArrayList<>();
        teacher.getTeacherCourses().stream().forEach(course -> courses.add(course.getCourse()));

        model.addAttribute("lessons", lessonHashMap);
        model.addAttribute("courses", courses.stream().map(el -> courseMapper.mapCourseToCourseDTO(el)).collect(Collectors.toList()));
        model.addAttribute("weekDays", weekDays);
        model.addAttribute("teacher", teacherMapper.mapTeacherToTeacherDTO(teacher));
        model.addAttribute("student", studentMapper.mapStudentToStudentDTO(student));
        model.addAttribute("hours", Arrays.asList("8:00", "8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30"));
        return "closedAdmin/adminTeachers/teacherStudentLessons";
    }
    // Opens the GET /teacher/{teachId}/lessonsWithStudent/{stId}/{output} route and prepares data for the "closedAdmin/adminTeachers/teacherStudentLessons" template.
    // Adds "output", "lessons", "courses", "weekDays", "teacher", and others to the Model; retrieves data via `teacherService.getById`, `studentService.getById`, `lessonService.compileLessonsForStudentAndTeacher`, `courseMapper.mapCourseToCourseDTO`, `teacherMapper.mapTeacherToTeacherDTO`, and others.
    @GetMapping("/teacher/{idTeach}/lessonsWithStudent/{idSt}/{output}")
    public String getLessonsFromTeacherForStudent(@PathVariable("idTeach") int idTeach, @PathVariable("idSt") int idSt, @PathVariable("output") String output, Model model) throws JsonProcessingException {
        Teacher teacher = teacherService.getById(idTeach);
        Student student = studentService.getById(idSt);

        HashMap<String, LessonAdminLessonsDTO> lessonHashMap = lessonService.compileLessonsForStudentAndTeacher(idTeach, student.getId());
        List<String> weekDays = theWeekService.compileWeekDays();
        List<Course> courses = new ArrayList<>();
        teacher.getTeacherCourses().stream().forEach(course -> courses.add(course.getCourse()));

        model.addAttribute("output", output);
        model.addAttribute("lessons", lessonHashMap);
        model.addAttribute("courses", courses.stream().map(el -> courseMapper.mapCourseToCourseDTO(el)).collect(Collectors.toList()));
        model.addAttribute("weekDays", weekDays);
        model.addAttribute("teacher", teacherMapper.mapTeacherToTeacherDTO(teacher));
        model.addAttribute("student", studentMapper.mapStudentToStudentDTO(student));
        model.addAttribute("hours", Arrays.asList("8:00", "8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30"));
        return "closedAdmin/adminTeachers/teacherStudentLessons";
    }
    // Deletes or disconnects data at the POST /teacher/{teachId}/lessonsWithStudent/deleteLesson/{id} route in the "lessons" module.
    // Calls `lessonService.getById`, `teacherService.getById`, `mailService.sendEmailWithThymeleafToStudentAboutLessonRemoved`, `lessonService.deleteById`, `teacherMapper.mapTeacherToTeacherDTO`; returns "redirect:/admin/teacher/" upon completion.
    @PostMapping("/teacher/{teachId}/lessonsWithStudent/deleteLesson/{id}")
    @Transactional
    public String deleteLessonForStudent(@PathVariable("teachId") int idTeach, @PathVariable("id") int idLes) throws MessagingException {
        int studentId = lessonService.getById(idLes).getStudent().getId();
        lessonService.manageDeleteLessonByTeacher(idTeach, idLes);
        return "redirect:/admin/teacher/" + idTeach + "/lessonsWithStudent/" + studentId + "/LESSON_DELETED";
    }
    // Deletes or disconnects data at the POST /teacher/{idTeach}/lessonsWithStudent/deleteCourse/{id} route in the "lessons" module.
    // Calls `lessonService.getById`, `teacherService.getById`, `mailService.sendEmailWithThymeleafToStudentAboutCourseRemoved`, `lessonService.deleteAllByStIdAndTeachIdAndTswIdAndLesTimeAfterNow`, `tswService.removeAllByStIdAndTeachIdAndTswId`, and others; returns "redirect:/admin/teacher/" upon completion.
    @PostMapping("/teacher/{idTeach}/lessonsWithStudent/deleteCourse/{id}")
    @Transactional
    public String deleteCourseForStudent(@PathVariable("idTeach") int idTeach, @PathVariable("id") int idLes) throws MessagingException {
        int studentId = lessonService.getById(idLes).getStudent().getId();
        lessonService.manageDeleteCourseByTeacher(idTeach, idLes);
        return "redirect:/admin/teacher/" + idTeach + "/lessonsWithStudent/" + studentId + "/COURSE_DELETED";
    }

    // Updates data at the POST /teacher/{idTeach}/lessonsWithStudent/editLesson/{id}/{nTId}/{date} route in the "lessons" module.
    // Calls `lessonService.getById`, `lessonService.checkIfLessonValidWithReputitions`, `teacherService.getById`, `mailService.sendEmailWithThymeleafToStudentAboutLessonTimeEdited`, `lessonService.update`, and others; works with SUCCESS statuses; returns "redirect:/admin/teacher/" upon completion.
    @PostMapping("/teacher/{idTeach}/lessonsWithStudent/editLesson/{id}/{nTId}/{date}")
    public String editLessonForStudent(@PathVariable("idTeach") int idTeach, @PathVariable("id") int idLes,  @PathVariable("date") String date, Model model) throws MessagingException {
        Lesson lesson = lessonService.getById(idLes);
        List<Object> objs = lessonService.checkIfLessonValidWithReputitions(idTeach, lesson.getStudent().getId(),lesson.getCourse().getId(), idLes, lesson.getDuration(), date, 1);
        Statuses status = (Statuses) objs.get(0);
        LocalDateTime newDate = (LocalDateTime) objs.get(1);
        if (status == Statuses.SUCCESS) {
            Teacher teacher = teacherService.getById(idTeach);
            mailService.sendEmailWithThymeleafToStudentAboutLessonTimeEdited(teacherMapper.mapTeacherToTeacherDTO(teacher), lesson.getLessonTime(), newDate, lesson.getDuration(), "Lesson time changed", Collections.singletonList(lesson.getStudent().getEmail()));
            lesson.setLessonTime(newDate);
            lessonService.update(lesson.getId(), lesson);
            return "redirect:/admin/teacher/" + idTeach + "/lessonsWithStudent/" + lesson.getStudent().getId() + "/LESSON_EDITED";
        } else {
            return "redirect:/admin/teacher/" + idTeach + "/lessonsWithStudent/" + lesson.getStudent().getId() + "/" + status.name();
        }

    }

    // Creates data at the POST /teacher/{teachId}/lessonsWithStudent/{stId}/addLesson/{cId}/{retDate}/{dur} route in the "lessons" module.
    // Calls `lessonService.checkIfLessonValidWithReputitions`, `studentService.getById`, `teacherService.getById`, `courseService.getById`, `theWeekService.findByDayOfTheWeekAndTimeOfTheDayAndMinute`, and others; works with SUCCESS statuses; returns "redirect:/admin/teacher/" upon completion.
    @PostMapping("/teacher/{teachId}/lessonsWithStudent/{stId}/addLesson/{cId}/{retDate}/{dur}")
    public String addLessonToStudent(@PathVariable("teachId") int idTeach, @PathVariable("cId") int cId, @PathVariable("stId") int stId, @PathVariable("retDate") String date, @PathVariable("dur") float dur, Model model) throws MessagingException {
        List<Object> objs = lessonService.checkIfLessonValidWithReputitions(idTeach, stId, cId, dur, date, 1);
        Statuses status = (Statuses) objs.get(0);
        LocalDateTime time = (LocalDateTime) objs.get(1);

        if (status == Statuses.SUCCESS) {
            Lesson lesson = new Lesson(studentService.getById(stId), teacherService.getById(idTeach), courseService.getById(cId), time, dur, theWeekService.findByDayOfTheWeekAndTimeOfTheDayAndMinute(time.getDayOfWeek().getValue(), time.getHour(), time.getMinute()).getId(), "WILL");
            lessonService.create(lesson);
            Teacher teacher = teacherService.getById(idTeach);
            mailService.sendEmailWithThymeleafToStudentAboutLessonAdded(teacherMapper.mapTeacherToTeacherDTO(teacher), lesson.getLessonTime(), dur, "New lesson notification", Collections.singletonList(studentService.getById(stId).getEmail()));
            return "redirect:/admin/teacher/" + idTeach + "/lessonsWithStudent/" + stId + "/LESSON_ADDED";
        } else {
            return "redirect:/admin/teacher/" + idTeach + "/lessonsWithStudent/" + stId +"/"+ status.name();
        }
    }

    // Updates data at the POST /teacher/{teachId}/lessonsWithStudent/{stId}/addCourse/{cId}/{retDate}/{dur} route in the "lessons" module.
    // Calls `lessonService.checkIfLessonValidWithReputitions`, `studentService.getById`, `teacherService.getById`, `courseService.getById`, `theWeekService.findByDayOfTheWeekAndTimeOfTheDayAndMinute`, and others; works with SUCCESS statuses; returns "redirect:/admin/teacher/" upon completion.
    @PostMapping("/teacher/{teachId}/lessonsWithStudent/{stId}/addCourse/{cId}/{retDate}/{dur}")
    public String addCourseToStudent(@PathVariable("teachId") int idTeach, @PathVariable("stId") int stId, @PathVariable("cId") int cId, @PathVariable("retDate") String date, @PathVariable("dur") float dur, Model model) throws MessagingException {
        List<Object> objs = lessonService.checkIfLessonValidWithReputitions(idTeach, stId, cId, dur, date, 5);
        Statuses status = (Statuses) objs.get(0);
        LocalDateTime time = (LocalDateTime) objs.get(1);

        if (status == Statuses.SUCCESS) {
            for (int i = 1; i <= 5; i++) {
                Lesson lesson = new Lesson(studentService.getById(stId), teacherService.getById(idTeach), courseService.getById(cId), time, dur, theWeekService.findByDayOfTheWeekAndTimeOfTheDayAndMinute(time.getDayOfWeek().getValue(), time.getHour(), time.getMinute()).getId(), "WILL");
                lessonService.create(lesson);

                time = time.plusWeeks(1);
            }
            TeacherStudentTimeOfTheWeek tsw = new TeacherStudentTimeOfTheWeek(teacherService.getById(idTeach), studentService.getById(stId), theWeekService.findByDayOfTheWeekAndTimeOfTheDayAndMinute(time.getDayOfWeek().getValue(), time.getHour(), time.getMinute()).getId(), courseService.getById(cId));
            tswService.create(tsw);

            Teacher teacher = teacherService.getById(idTeach);
            mailService.sendEmailWithThymeleafToStudentAboutCourseAdded(teacherMapper.mapTeacherToTeacherDTO(teacher), time, dur, "Course added notification", Collections.singletonList(studentService.getById(stId).getEmail()));
            return "redirect:/admin/teacher/" + idTeach + "/lessonsWithStudent/" + stId + "/COURSE_ADDED";
        }
        else {
            return "redirect:/admin/teacher/" + idTeach + "/lessonsWithStudent/" + stId + "/" +status.name();
        }
    }
    // Creates data at the POST /teacher/{idTeach}/addMoneyByLessonAndStudent/{lesId}/{stId} route in the "lessons" module.
    // Calls `teacherService.getById`, `lessonService.getById`, `teacherService.update`, `lessonService.update`; returns "redirect:/admin/teacher/" upon completion.
    @PostMapping("/teacher/{idTeach}/addMoneyByLessonAndStudent/{lesId}/{stId}")
    public String addMoney(@PathVariable("idTeach") int teachId, @PathVariable("lesId") int lesId, @PathVariable("stId") int stId, Model model) {
        Teacher teacher = teacherService.getById(teachId);
        Lesson lesson = lessonService.getById(lesId);
        float sum = lesson.getDuration() * lesson.getCourse().getTeacherShare();
        teacher.setUnpaidMoney(teacher.getUnpaidMoney() + sum);
        teacherService.update(teachId, teacher);
        lesson.setStatus("WAS");
        lessonService.update(lesId, lesson);

        return "redirect:/admin/teacher/" + teachId + "/lessonsWithStudent/" + stId + "/LESSON_STATUS_CHANGED";
    }
}
