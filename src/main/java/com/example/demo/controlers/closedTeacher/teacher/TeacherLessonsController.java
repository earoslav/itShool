package com.example.demo.controlers.closedTeacher.teacher;

import com.example.demo.dto.adminLessons.LessonAdminLessonsDTO;
import com.example.demo.mapper.entity.StudentMapper;
import com.example.demo.mapper.entity.TeacherMapper;
import com.example.demo.mapper.programe.CourseMapper;
import com.example.demo.mapper.programe.LessonMapper;
import com.example.demo.models.email.Mail;
import com.example.demo.models.entities.Teacher;
import com.example.demo.models.other.TimeOfTheWeek;
import com.example.demo.models.programe.Course;
import com.example.demo.models.programe.Lesson;
import com.example.demo.models.thirdTables.TeacherStudentTimeOfTheWeek;
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
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

// TeacherLessonsController handles web pages for the "lessons" module.
// The methods below accept parameters from URLs or forms, call project services, and return the required Thymeleaf templates or redirects.
@Controller
@RequestMapping("/teacher")
public class TeacherLessonsController {
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
    private StudentMapper studentMapper;
    private TeacherMapper teacherMapper;
    private CourseMapper courseMapper;
    // Receives dependencies through Spring: TeacherService, LessonMapper, TimeOfTheWeekService, CourseService, EmptyTimesForTeacherService, LessonService, TeacherCourseService, and others.
    // These services and mappers are required by the class methods to work with the "lessons" module without manual object creation.
    public TeacherLessonsController(TeacherService teacherService, LessonMapper lessonMapper, TimeOfTheWeekService theWeekService, CourseService courseService, EmptyTimesForTeacherService emptyTimesForTeacherService, LessonService lessonService, TeacherCourseService teacherCourseService, MailService mailService, TeacherStudentTimeOfTheWeekService tswService, StudentService studentService, StudentMapper studentMapper, TeacherMapper teacherMapper, CourseMapper courseMapper) {
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
        this.studentMapper = studentMapper;
        this.teacherMapper = teacherMapper;
        this.courseMapper = courseMapper;
    }
    // Opens the GET /{id}/lessons route and prepares data for the "closedTeacher/teacherLessons/lessons" template.
    // Adds "lessons", "students", "courses", "weekDays", "teacher", and others to the Model; retrieves data via `teacherService.getById`, `lessonService.compileLessonsForTeacher`, `studentService.getAll`, `studentMapper.mapStudentToStudentDTO`, `courseMapper.mapCourseToCourseDTO`, and others.
    @GetMapping("/{idTeach}/lessons")
    public String getLessonsForTeacher(@PathVariable("idTeach") int idTeach, Model model) throws JsonProcessingException {
        Teacher teacher = teacherService.getById(idTeach);

        HashMap<String, LessonAdminLessonsDTO> lessonHashMap = lessonService.compileLessonsForTeacher(teacher.getId());
        List<String> weekDays = theWeekService.compileWeekDays();
        List<Course> courses = new ArrayList<>();
        teacher.getTeacherCourses().stream().forEach(course -> courses.add(course.getCourse()));

        model.addAttribute("lessons", lessonHashMap);
        model.addAttribute("students", studentService.getAll().stream().map(el -> studentMapper.mapStudentToStudentDTO(el)).collect(Collectors.toList()));
        model.addAttribute("courses", courses.stream().map(el -> courseMapper.mapCourseToCourseDTO(el)).collect(Collectors.toList()));
        model.addAttribute("weekDays", weekDays);
        model.addAttribute("teacher", teacherMapper.mapTeacherToTeacherDTO(teacher));
        model.addAttribute("hours", Arrays.asList("8:00", "8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30"));
        return "closedTeacher/teacherLessons/lessons";
    }
    // Updates data at the POST /{idTeach}/lessons/editLesson/notPicked route in the "lessons" module.
    // Returns redirects to "/teacher/" upon completion.
    @PostMapping("/{idTeach}/lessons/editLesson/notPicked")
    public String editLessoneFromTeacherWithNotPicked(@PathVariable("idTeach") int idTeach) {
        return "redirect:/teacher/" + idTeach + "/lessons/ITEM_NOT_PICKED";
    }
    // Opens the GET /{id}/lessons/{output} route and prepares data for the "closedTeacher/teacherLessons/lessons" template.
    // Adds "output", "lessons", "students", "courses", "weekDays", and others to the Model; retrieves data via `teacherService.getById`, `lessonService.compileLessonsForTeacher`, `studentService.getAll`, `studentMapper.mapStudentToStudentDTO`, `courseMapper.mapCourseToCourseDTO`, and others.
    @GetMapping("/{idTeach}/lessons/{output}")
    public String getLessonsForTeacher(@PathVariable("idTeach") int idTeach, @PathVariable("output") String output, Model model) throws JsonProcessingException {
        Teacher teacher = teacherService.getById(idTeach);

        HashMap<String, LessonAdminLessonsDTO> lessonHashMap = lessonService.compileLessonsForTeacher(teacher.getId());
        List<String> weekDays = theWeekService.compileWeekDays();
        List<Course> courses = new ArrayList<>();
        teacher.getTeacherCourses().stream().forEach(course -> courses.add(course.getCourse()));

        model.addAttribute("output", output);
        model.addAttribute("lessons", lessonHashMap);
        model.addAttribute("students", studentService.getAll().stream().map(el -> studentMapper.mapStudentToStudentDTO(el)).collect(Collectors.toList()));
        model.addAttribute("courses", courses.stream().map(el -> courseMapper.mapCourseToCourseDTO(el)).collect(Collectors.toList()));
        model.addAttribute("weekDays", weekDays);
        model.addAttribute("teacher", teacherMapper.mapTeacherToTeacherDTO(teacher));
        model.addAttribute("hours", Arrays.asList("8:00", "8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30"));
        return "closedTeacher/teacherLessons/lessons";
    }
    // Deletes or disconnects data at the POST /{idTeach}/lessons/deleteLesson/{id} route in the "lessons" module.
    // Calls `lessonService.getById`, `teacherService.getById`, `mailService.sendEmailWithThymeleafToStudentAboutLessonRemoved`, `lessonService.deleteById`, and `teacherMapper.mapTeacherToTeacherDTO`; returns redirects to "/teacher/".
    @PostMapping("/{idTeach}/lessons/deleteLesson/{id}")
    @Transactional
    public String deleteLesson(@PathVariable("idTeach") int idTeach, @PathVariable("id") int idLes) throws MessagingException {
        lessonService.manageDeleteLessonByTeacher(idTeach, idLes);
        return "redirect:/teacher/" + idTeach + "/lessons/LESSON_DELETED";
    }
    // Deletes or disconnects data at the POST /{idTeach}/lessons/deleteCourse/{id} route in the "lessons" module.
    // Calls `lessonService.getById`, `teacherService.getById`, `mailService.sendEmailWithThymeleafToStudentAboutCourseRemoved`, `lessonService.deleteAllByStIdAndTeachIdAndTswIdAndLesTimeAfterNow`, `tswService.removeAllByStIdAndTeachIdAndTswId`, and others; returns redirects to "/teacher/".
    @PostMapping("/{idTeach}/lessons/deleteCourse/{id}")
    @Transactional
    public String deleteCourse(@PathVariable("idTeach") int idTeach, @PathVariable("id") int idLes) throws MessagingException {
        lessonService.manageDeleteCourseByTeacher(idTeach, idLes);
        return "redirect:/teacher/" + idTeach + "/lessons/COURSE_DELETED";
    }

    //    @PostMapping("/{idTeach}/lessons/editLesson/{id}/{nTId}/{date}")
//    public String editLesson(@PathVariable("idTeach") int idTeach, @PathVariable("id") int idLes, @PathVariable("nTId") int newTimeId, @PathVariable("date") String date, Model model) throws MessagingException {
//        TimeOfTheWeek newTime = theWeekService.getById(newTimeId);
//        Lesson lesson = lessonService.getById(idLes);
//        float dur = lesson.getDuration();
//        if (newDate.getDayOfWeek().getValue() == newTime.getDayOfTheWeek()) {
//            if (dur == (long) dur) {
//                lessonFinish = lessonFinish.plusHours((long) dur);
//            } else {
//                lessonFinish = lessonFinish.plusHours((long) dur).plusMinutes(30);
//            }
//            if (lessonFinish.isBefore(time.withHour(22).withMinute(1))) {
//                AtomicBoolean overlap = new AtomicBoolean(false);
//                List<Lesson> teacherLessons = lessonService.findAllByTeachId(idTeach);
//                teacherLessons.stream().filter(les->les.getId()!=lesson.getId()).forEach(l -> {
//                    float lDur = l.getDuration();
//                    if (lDur == (long) lDur) {
//                        lFinish = lFinish.plusHours((long) lDur);
//                    } else {
//                        lFinish = lFinish.plusHours((long) lDur).plusMinutes(30);
//                    }
//                    if ((l.getLessonTime().isAfter(finalTime) && l.getLessonTime().isBefore(finalLessonFinish)) || (lFinish.isAfter(finalTime) && finalTime.isAfter(l.getLessonTime()))) {
//                        overlap.set(true);
//                    }
//                });
//
//
//                List<Lesson> studentLessons = lessonService.findAllByStudentId(lesson.getStudent().getId());
//                studentLessons.stream().filter(les->les.getId()!=lesson.getId()).forEach(l -> {
//                    float lDur = l.getDuration();
//                    if (lDur == (long) lDur) {
//                        lFinish = lFinish.plusHours((long) lDur);
//                    } else {
//                        lFinish = lFinish.plusHours((long) lDur).plusMinutes(30);
//                    }
//                    if ((l.getLessonTime().isAfter(finalTime) && l.getLessonTime().isBefore(finalLessonFinish)) || (lFinish.isAfter(finalTime) && finalTime.isAfter(l.getLessonTime()))) {
//                        overlap.set(true);
//                    }
//                });
//                if (!overlap.get()) {
//                    Mail mail = new Mail();
//                    mail.setTo(Collections.singletonList(lesson.getStudent().getEmail()));
//                    mail.setSubject("Лист про зміну часу одного заняття");
//                    mail.setBody("");
//                    Teacher teacher = teacherService.getById(idTeach);
//                    mailService.sendEmailWithThymeleafToStudentAboutLessonTimeEdited(mail, teacherMapper.mapTeacherToTeacherDTO(teacher), lesson.getLessonTime(), newDate, dur);
//                    lesson.setLessonTime(newDate);
//                    lessonService.update(lesson.getId(), lesson);
//                    return "redirect:/teacher/" + idTeach + "/lessons/LESSON_EDITED";
//                } else {
//                    return "redirect:/teacher/" + idTeach + "/lessons/LESSON_OVERLAP";
//                }
//            } else {
//                return "redirect:/teacher/" + idTeach + "/lessons/LESSON_AFTER_ACCEPTED_TIME";
//            }
//        } else {
//            return "redirect:/teacher/" + idTeach + "/lessons/DATE_INCONSISTENCY";
//        }
//
//    }
    // Updates data at the POST /{idTeach}/lessons/editLesson/{id}/{nTId}/{date} route in the "lessons" module.
    // Calls `lessonService.getById`, `lessonService.checkIfLessonValidWithReputitions`, `teacherService.getById`, `mailService.sendEmailWithThymeleafToStudentAboutLessonTimeEdited`, `lessonService.update`, and others; works with SUCCESS statuses; returns redirects to "/teacher/".
    @PostMapping("/{idTeach}/lessons/editLesson/{id}/{nTId}/{date}")
    public String editLesson(@PathVariable("idTeach") int idTeach, @PathVariable("id") int idLes, @PathVariable("date") String date, Model model) throws MessagingException {
        Lesson lesson = lessonService.getById(idLes);
        List<Object> objs = lessonService.checkIfLessonValidWithReputitions(idTeach, lesson.getStudent().getId(),lesson.getCourse().getId(), idLes, lesson.getDuration(), date, 1);
        Statuses status = (Statuses) objs.get(0);
        LocalDateTime newDate = (LocalDateTime) objs.get(1);
        if (status == Statuses.SUCCESS) {
            Teacher teacher = teacherService.getById(idTeach);
            mailService.sendEmailWithThymeleafToStudentAboutLessonTimeEdited(teacherMapper.mapTeacherToTeacherDTO(teacher), lesson.getLessonTime(), newDate, lesson.getDuration(), "Lesson time changed", Collections.singletonList(lesson.getStudent().getEmail()));
            lesson.setLessonTime(newDate);
            lessonService.update(lesson.getId(), lesson);
            return "redirect:/teacher/" + idTeach + "/lessons/LESSON_EDITED";
        } else {
            return "redirect:/teacher/" + idTeach + "/lessons/" + status.name();
        }
    }

    // Creates data at the POST /{teachId}/lessons/addLesson/{cId}/{stId}/{retDate}/{dur} route in the "lessons" module.
    // Calls `lessonService.checkIfLessonValidWithReputitions`, `studentService.getById`, `teacherService.getById`, `courseService.getById`, `theWeekService.findByDayOfTheWeekAndTimeOfTheDayAndMinute`, and others; works with SUCCESS statuses; returns redirects to "/teacher/".
    @PostMapping("/{teachId}/lessons/addLesson/{cId}/{stId}/{retDate}/{dur}")
    public String addLesson(@PathVariable("teachId") int idTeach, @PathVariable("cId") int cId, @PathVariable("stId") int stId, @PathVariable("retDate") String date, @PathVariable("dur") float dur, Model model) throws MessagingException {

        List<Object> objs = lessonService.checkIfLessonValidWithReputitions(idTeach, stId, cId, dur, date, 1);
        Statuses status = (Statuses) objs.get(0);
        LocalDateTime time = (LocalDateTime) objs.get(1);

        if (status == Statuses.SUCCESS) {
            Lesson lesson = new Lesson(studentService.getById(stId), teacherService.getById(idTeach), courseService.getById(cId), time, dur, theWeekService.findByDayOfTheWeekAndTimeOfTheDayAndMinute(time.getDayOfWeek().getValue(), time.getHour(), time.getMinute()).getId(), "WILL");
            lessonService.create(lesson);
            Teacher teacher = teacherService.getById(idTeach);
            mailService.sendEmailWithThymeleafToStudentAboutLessonAdded(teacherMapper.mapTeacherToTeacherDTO(teacher), lesson.getLessonTime(), dur, "New lesson notification", Collections.singletonList(studentService.getById(stId).getEmail()));
            return "redirect:/teacher/" + idTeach + "/lessons/LESSON_ADDED";
        } else {
            return "redirect:/teacher/" + idTeach + "/lessons/" + status.name();
        }

    }

    // Creates data at the POST /{teachId}/lessons/addCourse/{cId}/{stId}/{retDate}/{dur} route in the "lessons" module.
    // Calls `lessonService.checkIfLessonValidWithReputitions`, `studentService.getById`, `teacherService.getById`, `courseService.getById`, `theWeekService.findByDayOfTheWeekAndTimeOfTheDayAndMinute`, and others; works with SUCCESS statuses; returns redirects to "/teacher/".
    @PostMapping("/{teachId}/lessons/addCourse/{cId}/{stId}/{retDate}/{dur}")
    public String addCourse(@PathVariable("teachId") int idTeach, @PathVariable("cId") int cId, @PathVariable("stId") int stId, @PathVariable("retDate") String date, @PathVariable("dur") float dur, Model model) throws MessagingException {
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
            return "redirect:/teacher/" + idTeach + "/lessons/COURSE_ADDED";
        } else {
            return "redirect:/teacher/" + idTeach + "/lessons/" + status.name();

        }

    }
    // Creates data at the POST /{idTeach}/addMoneyByLesson/{lesId} route in the "lessons" module.
    // Calls `teacherService.getById`, `lessonService.getById`, `teacherService.update`, and `lessonService.update`; returns redirects to "/teacher/".
    @PostMapping("/{idTeach}/addMoneyByLesson/{lesId}")
    public String addMoney(@PathVariable("idTeach") int teachId, @PathVariable("lesId") int lesId, Model model) {
        Teacher teacher = teacherService.getById(teachId);
        Lesson lesson = lessonService.getById(lesId);
        if (LocalDateTime.now().isAfter(lesson.getLessonTime())) {
            float sum = lesson.getDuration() * lesson.getCourse().getTeacherShare();
            teacher.setUnpaidMoney(teacher.getUnpaidMoney() + sum);
            teacherService.update(teachId, teacher);
            lesson.setStatus("WAS");
            lessonService.update(lesId, lesson);
            return "redirect:/teacher/" + teachId + "/lessons/LESSON_STATUS_CHANGED";
        } else {
            return "redirect:/teacher/" + teachId + "/lessons/TOO_EARLY";
        }
    }

}
