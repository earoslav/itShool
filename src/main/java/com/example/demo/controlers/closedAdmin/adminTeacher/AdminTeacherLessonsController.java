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

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

// AdminTeacherLessonsController handles web pages for the "lessons" module.
// The methods below accept parameters from URLs or forms, call project services, and return the required Thymeleaf templates or redirects.
@Controller
@RequestMapping("/admin")
public class AdminTeacherLessonsController {
    private TeacherCourseService teacherCourseService;
    private TeacherService teacherService;


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
    // Receives dependencies through Spring: TeacherCourseService, TeacherService, EmptyTimesForTeacherService, TimeOfTheWeekService, CourseService, StudentCourseService, CommentService, and others.
    // These services and mappers are required by the class methods to work with the "lessons" module without manual object creation.
    public AdminTeacherLessonsController(TeacherCourseService teacherCourseService, TeacherService teacherService, TimeOfTheWeekService theWeekService, CourseService courseService, CommentService commentService, LessonService lessonService, StudentService studentService, LessonMapper lessonMapper, TeacherStudentTimeOfTheWeekService tswService, MailService mailService, AdminService adminService, TeacherMapper teacherMapper, StudentMapper studentMapper, CourseMapper courseMapper) {
        this.teacherCourseService = teacherCourseService;
        this.teacherService = teacherService;

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
    }
    // Opens the GET /teacher/{id}/lessons route and prepares data for the "closedAdmin/adminTeachers/teacherLessons" template.
    // Adds "lessons", "students", "courses", "weekDays", "teacher", and others to the Model; retrieves data via `teacherService.getById`, `lessonService.compileLessonsForTeacher`, `studentService.getAll`, `studentMapper.mapStudentToStudentDTO`, `courseMapper.mapCourseToCourseDTO`, and others.
    @GetMapping("/teacher/{id}/lessons")
    public String getLessonsForTeacher(@PathVariable("id") int id, Model model) throws JsonProcessingException {
        Teacher teacher = teacherService.getById(id);
        HashMap<String, LessonAdminLessonsDTO> lessonHashMap = lessonService.compileLessonsForTeacher(teacher.getId());
        List<String> weekDays = theWeekService.compileWeekDays();
        List<Course> courses = new ArrayList<>();
        teacher.getTeacherCourses().stream().forEach(course -> courses.add(course.getCourse()));

        List<Student> students = studentService.getAll();

        model.addAttribute("lessons", lessonHashMap);
        model.addAttribute("students", students.stream().map(el -> studentMapper.mapStudentToStudentDTO(el)).collect(Collectors.toList()));
        model.addAttribute("courses", courses.stream().map(el -> courseMapper.mapCourseToCourseDTO(el)).collect(Collectors.toList()));
        model.addAttribute("weekDays", weekDays);
        model.addAttribute("teacher", teacherMapper.mapTeacherToTeacherDTO(teacher));
        model.addAttribute("hours", Arrays.asList("8:00", "8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30"));
        return "closedAdmin/adminTeachers/teacherLessons";
    }
    // Opens the GET /teacher/{id}/lessons/{output} route and prepares data for the "closedAdmin/adminTeachers/teacherLessons" template.
    // Adds "output", "lessons", "students", "courses", "weekDays", and others to the Model; retrieves data via `teacherService.getById`, `lessonService.compileLessonsForTeacher`, `studentService.getAll`, `studentMapper.mapStudentToStudentDTO`, `courseMapper.mapCourseToCourseDTO`, and others.
    @GetMapping("/teacher/{id}/lessons/{output}")
    public String getLessonsForTeacher(@PathVariable("id") int id, @PathVariable("output") String output, Model model) throws JsonProcessingException {
        Teacher teacher = teacherService.getById(id);

        HashMap<String, LessonAdminLessonsDTO> lessonHashMap = lessonService.compileLessonsForTeacher(teacher.getId());
        List<String> weekDays = theWeekService.compileWeekDays();
        List<Course> courses = new ArrayList<>();
        teacher.getTeacherCourses().stream().forEach(course -> courses.add(course.getCourse()));

        List<Student> students = studentService.getAll();

        model.addAttribute("output", output);
        model.addAttribute("lessons", lessonHashMap);
        model.addAttribute("students", students.stream().map(el -> studentMapper.mapStudentToStudentDTO(el)).collect(Collectors.toList()));
        model.addAttribute("courses", courses.stream().map(el -> courseMapper.mapCourseToCourseDTO(el)).collect(Collectors.toList()));
        model.addAttribute("weekDays", weekDays);
        model.addAttribute("teacher", teacherMapper.mapTeacherToTeacherDTO(teacher));
        model.addAttribute("hours", Arrays.asList("8:00", "8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30"));
        return "closedAdmin/adminTeachers/teacherLessons";
    }

    // Deletes or disconnects data at the POST /teacher/{idTeach}/lessons/deleteLesson/{id} route in the "lessons" module.
    // Calls `lessonService.getById`, `teacherService.getById`, `mailService.sendEmailWithThymeleafToStudentAboutLessonRemoved`, `lessonService.deleteById`, `teacherMapper.mapTeacherToTeacherDTO`; returns "redirect:/admin/teacher/" upon completion.
    @PostMapping("/teacher/{idTeach}/lessons/deleteLesson/{id}")
    @Transactional
    public String deleteLesson(@PathVariable("idTeach") int idTeach, @PathVariable("id") int idLes) throws MessagingException {
        lessonService.manageDeleteLessonByTeacher(idTeach, idLes);
        return "redirect:/admin/teacher/" + idTeach + "/lessons/LESSON_DELETED";
    }
    // Deletes or disconnects data at the POST /teacher/{idTeach}/lessons/deleteCourse/{id} route in the "lessons" module.
    // Calls `lessonService.getById`, `teacherService.getById`, `mailService.sendEmailWithThymeleafToStudentAboutCourseRemoved`, `lessonService.deleteAllByStIdAndTeachIdAndTswIdAndLesTimeAfterNow`, `tswService.removeAllByStIdAndTeachIdAndTswId`, and others; returns "redirect:/admin/teacher/" upon completion.
    @PostMapping("/teacher/{idTeach}/lessons/deleteCourse/{id}")
    @Transactional
    public String deleteCourseFromTeacher(@PathVariable("idTeach") int idTeach, @PathVariable("id") int idLes) throws MessagingException {
        lessonService.manageDeleteCourseByTeacher(idTeach, idLes);
        return "redirect:/admin/teacher/" + idTeach + "/lessons/COURSE_DELETED";
    }
    // Updates data at the POST /teacher/{idTeach}/lessons/editLesson/notPicked route in the "lessons" module.
    // Returns "redirect:/admin/teacher/" upon completion.
    @PostMapping("/teacher/{idTeach}/lessons/editLesson/notPicked")
    public String editLessoneFromTeacherWithNotPicked(@PathVariable("idTeach") int idTeach) {
        return "redirect:/admin/teacher/" + idTeach + "/lessons/ITEM_NOT_PICKED";
    }

    // Updates data at the POST /teacher/{idTeach}/lessons/editLesson/{id}/{nTId}/{date} route in the "lessons" module.
    // Calls `lessonService.getById`, `lessonService.checkIfLessonValidWithReputitions`, `teacherService.getById`, `mailService.sendEmailWithThymeleafToStudentAboutLessonTimeEdited`, `lessonService.update`, and others; works with SUCCESS statuses; returns "redirect:/admin/teacher/" upon completion.
    @PostMapping("/teacher/{idTeach}/lessons/editLesson/{id}/{nTId}/{date}")
    public String editLessonFromTeacher(@PathVariable("idTeach") int idTeach, @PathVariable("id") int idLes, @PathVariable("date") String date, Model model) throws MessagingException {
        Lesson lesson = lessonService.getById(idLes);
        List<Object> objs = lessonService.checkIfLessonValidWithReputitions(idTeach, lesson.getStudent().getId(),lesson.getCourse().getId(), idLes, lesson.getDuration(), date, 1);
        Statuses status = (Statuses) objs.get(0);
        LocalDateTime newDate = (LocalDateTime) objs.get(1);
        if (status == Statuses.SUCCESS) {
            Teacher teacher = teacherService.getById(idTeach);
            mailService.sendEmailWithThymeleafToStudentAboutLessonTimeEdited(teacherMapper.mapTeacherToTeacherDTO(teacher), lesson.getLessonTime(), newDate, lesson.getDuration(), "Lesson time changed", Collections.singletonList(lesson.getStudent().getEmail()));
            lesson.setLessonTime(newDate);
            lessonService.update(lesson.getId(), lesson);
            return "redirect:/admin/teacher/" + idTeach + "/lessons/" + "LESSON_EDITED";
        } else {
            return "redirect:/admin/teacher/" + idTeach + "/lessons/" + status.name();
        }

    }

    // Creates data at the POST /teacher/{teachId}/lessons/addLesson/{cId}/{stId}/{retDate}/{dur} route in the "lessons" module.
    // Calls `lessonService.checkIfLessonValidWithReputitions`, `studentService.getById`, `teacherService.getById`, `courseService.getById`, `theWeekService.findByDayOfTheWeekAndTimeOfTheDayAndMinute`, and others; works with SUCCESS statuses; returns "redirect:/admin/teacher/" upon completion.
    @PostMapping("/teacher/{teachId}/lessons/addLesson/{cId}/{stId}/{retDate}/{dur}")
    public String addLessoneToTeacher(@PathVariable("teachId") int idTeach, @PathVariable("cId") int cId, @PathVariable("stId") int stId, @PathVariable("retDate") String date, @PathVariable("dur") float dur, Model model) throws MessagingException {
        List<Object> objs = lessonService.checkIfLessonValidWithReputitions(idTeach, stId, cId, dur, date, 1);
        Statuses status = (Statuses) objs.get(0);
        LocalDateTime time = (LocalDateTime) objs.get(1);

        if (status == Statuses.SUCCESS) {
            Lesson lesson = new Lesson(studentService.getById(stId), teacherService.getById(idTeach), courseService.getById(cId), time, dur, theWeekService.findByDayOfTheWeekAndTimeOfTheDayAndMinute(time.getDayOfWeek().getValue(), time.getHour(), time.getMinute()).getId(), "WILL");
            lessonService.create(lesson);
            Teacher teacher = teacherService.getById(idTeach);
            mailService.sendEmailWithThymeleafToStudentAboutLessonAdded(teacherMapper.mapTeacherToTeacherDTO(teacher), lesson.getLessonTime(), dur, "New lesson notification", Collections.singletonList(studentService.getById(stId).getEmail()));
            return "redirect:/admin/teacher/" + idTeach + "/lessons/" + "LESSON_ADDED";
        } else {
            return "redirect:/admin/teacher/" + idTeach + "/lessons/" + status.name();
        }
    }

    // Creates data at the POST /teacher/{teachId}/lessons/addCourse/{cId}/{stId}/{retDate}/{dur} route in the "lessons" module.
    // Calls `lessonService.checkIfLessonValidWithReputitions`, `studentService.getById`, `teacherService.getById`, `courseService.getById`, `theWeekService.findByDayOfTheWeekAndTimeOfTheDayAndMinute`, and others; works with SUCCESS statuses; returns "redirect:/admin/teacher/" upon completion.
    @PostMapping("/teacher/{teachId}/lessons/addCourse/{cId}/{stId}/{retDate}/{dur}")
    public String addCourseeToTeacher(@PathVariable("teachId") int idTeach, @PathVariable("cId") int cId, @PathVariable("stId") int stId, @PathVariable("retDate") String date, @PathVariable("dur") float dur, Model model) throws MessagingException {
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
            return "redirect:/admin/teacher/" + idTeach + "/lessons/" + "COURSE_ADDED";
        } else {
            return "redirect:/admin/teacher/" + idTeach + "/lessons/" + status.name();
        }
    }
    // Creates data at the POST /teacher/{idTeach}/addMoneyByLesson/{lesId} route in the "lessons" module.
    // Calls `teacherService.getById`, `lessonService.getById`, `teacherService.update`, `lessonService.update`; returns "redirect:/admin/teacher/" upon completion.
    @PostMapping("/teacher/{idTeach}/addMoneyByLesson/{lesId}")
    public String addMoney(@PathVariable("idTeach") int teachId, @PathVariable("lesId") int lesId, Model model) {
        Teacher teacher = teacherService.getById(teachId);
        Lesson lesson = lessonService.getById(lesId);
        float sum = lesson.getDuration() * lesson.getCourse().getTeacherShare();
        teacher.setUnpaidMoney(teacher.getUnpaidMoney() + sum);
        teacherService.update(teachId, teacher);
        lesson.setStatus("WAS");
        lessonService.update(lesId, lesson);
        return "redirect:/admin/teacher/" + teachId + "/lessons/LESSON_STATUS_CHANGED";
    }


}
