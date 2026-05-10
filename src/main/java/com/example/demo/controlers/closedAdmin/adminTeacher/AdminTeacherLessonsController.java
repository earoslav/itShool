package com.example.demo.controlers.closedAdmin.adminTeacher;

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
import com.example.demo.services.entities.AdminService;
import com.example.demo.services.entities.StudentService;
import com.example.demo.services.entities.TeacherService;
import com.example.demo.services.other.CommentService;
import com.example.demo.services.other.TimeOfTheWeekService;
import com.example.demo.services.program.CourseService;
import com.example.demo.services.program.LessonService;
import com.example.demo.services.thirdTable.EmptyTimesForTeacherService;
import com.example.demo.services.thirdTable.StudentCourseService;
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

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminTeacherLessonsController {
    private TeacherCourseService teacherCourseService;
    private TeacherService teacherService;
    private EmptyTimesForTeacherService emptyTimesForTeacherService;

    private TimeOfTheWeekService theWeekService;
    private CourseService courseService;
    private StudentCourseService studentCourseService;
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

    public AdminTeacherLessonsController(TeacherCourseService teacherCourseService, TeacherService teacherService, EmptyTimesForTeacherService emptyTimesForTeacherService, TimeOfTheWeekService theWeekService, CourseService courseService, StudentCourseService studentCourseService, CommentService commentService, LessonService lessonService, StudentService studentService, LessonMapper lessonMapper, TeacherStudentTimeOfTheWeekService tswService, MailService mailService, AdminService adminService, TeacherMapper teacherMapper, StudentMapper studentMapper, CourseMapper courseMapper) {
        this.teacherCourseService = teacherCourseService;
        this.teacherService = teacherService;
        this.emptyTimesForTeacherService = emptyTimesForTeacherService;
        this.theWeekService = theWeekService;
        this.courseService = courseService;
        this.studentCourseService = studentCourseService;
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

    @GetMapping("/teacher/{id}/lessons")
    public String gotoLessons(@PathVariable("id") int id, Model model) {
        Teacher teacher = teacherService.getById(id);
        List<Object> values = lessonService.compileLessonsForTeacher(teacher.getId());
        List<String> weekDays = (List<String>) values.get(0);
        HashMap<String, LessonAdminLessonsDTO> lessonHashMap = (HashMap<String, LessonAdminLessonsDTO>) values.get(1);
        HashMap<String, LessonAdminLessonsDTO> lessonDurations = (HashMap<String, LessonAdminLessonsDTO>) values.get(2);

        List<Course> courses = new ArrayList<>();
        teacher.getTeacherCourses().stream().forEach(course -> courses.add(course.getCourse()));

        model.addAttribute("lessonDurations", lessonDurations);
        model.addAttribute("lessons", lessonHashMap);
        model.addAttribute("students", studentService.getAll().stream().map(el -> studentMapper.mapStudentToStudentDTO(el)).collect(Collectors.toList()));
        model.addAttribute("courses", courses.stream().map(el -> courseMapper.mapCourseToCourseDTO(el)).collect(Collectors.toList()));
        model.addAttribute("weekDays", weekDays);
        model.addAttribute("teacher", teacherMapper.mapTeacherToTeacherDTO(teacher));
        model.addAttribute("hours", Arrays.asList("8:00", "8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30"));
        return "closedAdmin/adminTeachers/teacherLessons";
    }

    @GetMapping("/teacher/{id}/lessons/{output}")
    public String gotoLessons(@PathVariable("id") int id, @PathVariable("output") String output, Model model) {
        Teacher teacher = teacherService.getById(id);

        List<Object> values = lessonService.compileLessonsForTeacher(teacher.getId());
        List<String> weekDays = (List<String>) values.get(0);
        HashMap<String, LessonAdminLessonsDTO> lessonHashMap = (HashMap<String, LessonAdminLessonsDTO>) values.get(1);
        HashMap<String, LessonAdminLessonsDTO> lessonDurations = (HashMap<String, LessonAdminLessonsDTO>) values.get(2);

        List<Course> courses = new ArrayList<>();
        teacher.getTeacherCourses().stream().forEach(course -> courses.add(course.getCourse()));

        model.addAttribute("lessonDurations", lessonDurations);
        model.addAttribute("output", output);
        model.addAttribute("lessons", lessonHashMap);
        model.addAttribute("students", studentService.getAll().stream().map(el -> studentMapper.mapStudentToStudentDTO(el)).collect(Collectors.toList()));
        model.addAttribute("courses", courses.stream().map(el -> courseMapper.mapCourseToCourseDTO(el)).collect(Collectors.toList()));
        model.addAttribute("weekDays", weekDays);
        model.addAttribute("teacher", teacherMapper.mapTeacherToTeacherDTO(teacher));
        model.addAttribute("hours", Arrays.asList("8:00", "8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30"));
        return "closedAdmin/adminTeachers/teacherLessons";
    }


    @PostMapping("/teacher/{idTeach}/lessons/deleteLesson/{id}")
    @Transactional
    public String deleteLesson(@PathVariable("idTeach") int idTeach, @PathVariable("id") int idLes) throws MessagingException {
        Mail mail = new Mail();
        mail.setTo(Collections.singletonList(lessonService.getById(idLes).getStudent().getEmail()));
        mail.setSubject("Лист про відміну заняття");
        mail.setBody("");
        Teacher teacher = teacherService.getById(idTeach);
        mailService.sendEmailWithThymeleafToStudentAboutLessonRemoved(mail, teacherMapper.mapTeacherToTeacherDTO(teacher), lessonService.getById(idLes).getLessonTime(), lessonService.getById(idLes).getDuration());
        lessonService.deleteById(idLes);
        return "redirect:/admin/teacher/" + idTeach + "/lessons/LESSON_DELETED";
    }

    @PostMapping("/teacher/{idTeach}/lessons/deleteCourse/{id}")
    @Transactional
    public String deleteCourseFromTeacher(@PathVariable("idTeach") int idTeach, @PathVariable("id") int idLes) throws MessagingException {
        Lesson lesson = lessonService.getById(idLes);
        Mail mail = new Mail();
        mail.setTo(Collections.singletonList(lesson.getStudent().getEmail()));
        mail.setSubject("Лист про відміну заняття");
        mail.setBody("");
        Teacher teacher = teacherService.getById(idTeach);
        mailService.sendEmailWithThymeleafToStudentAboutCourseRemoved(mail, teacherMapper.mapTeacherToTeacherDTO(teacher), lesson.getLessonTime(), lesson.getDuration());
        lessonService.deleteAllByStIdAndTeachIdAndTswIdAndLesTimeAfterNow(lesson.getStudent().getId(), idTeach, lesson.getTimeOfTheWeek().getId(), LocalDateTime.now().minusMinutes(30));
        tswService.removeAllByStIdAndTeachIdAndTswId(lesson.getStudent().getId(), idTeach, lesson.getTimeOfTheWeek().getId());
        return "redirect:/admin/teacher/" + idTeach + "/lessons/COURSE_DELETED";
    }

    @PostMapping("/teacher/{idTeach}/lessons/editLesson/notPicked")
    public String editLessoneFromTeacherWithNotPicked(@PathVariable("idTeach") int idTeach) {
        return "redirect:/admin/teacher/" + idTeach + "/lessons/ITEM_NOT_PICKED";
    }

    @PostMapping("/teacher/{idTeach}/lessons/editLesson/{id}/{nTId}/{date}")
    public String editLessonFromTeacher(@PathVariable("idTeach") int idTeach, @PathVariable("id") int idLes, @PathVariable("nTId") int newTimeId, @PathVariable("date") String date, Model model) throws MessagingException {
        Lesson lesson = lessonService.getById(idLes);
        List<Object> objs = lessonService.checkIfLessonValidWithReputitions(idTeach, lesson.getStudent().getId(),lesson.getCourse().getId(), idLes, lesson.getDuration(), date, 1);
        Statuses status = (Statuses) objs.get(0);
        LocalDateTime newDate = (LocalDateTime) objs.get(1);
        if (status == Statuses.SUCCESS) {
                    Mail mail = new Mail();
                    mail.setTo(Collections.singletonList(lesson.getStudent().getEmail()));
                    mail.setSubject("Лист про зміну часу одного заняття");
                    mail.setBody("");
                    Teacher teacher = teacherService.getById(idTeach);
                    mailService.sendEmailWithThymeleafToStudentAboutLessonTimeEdited(mail, teacherMapper.mapTeacherToTeacherDTO(teacher), lesson.getLessonTime(), newDate, lesson.getDuration());
                    lesson.setLessonTime(newDate);
                    lessonService.update(lesson.getId(), lesson);
                    return "redirect:/admin/teacher/" + idTeach + "/lessons/LESSON_EDITED";
                } else {
            return "redirect:/admin/teacher/" + idTeach + "/lessons/" + status.name();
        }

    }

    @PostMapping("/teacher/{teachId}/lessons/addLesson/{cId}/{stId}/{retDate}/{dur}")
    public String addLessoneToTeacher(@PathVariable("teachId") int idTeach, @PathVariable("cId") int cId, @PathVariable("stId") int stId, @PathVariable("retDate") String date, @PathVariable("dur") float dur, Model model) throws MessagingException {
        List<Object> objs = lessonService.checkIfLessonValidWithReputitions(idTeach, stId, cId, dur, date, 1);
        Statuses status = (Statuses) objs.get(0);
        LocalDateTime time = (LocalDateTime) objs.get(1);

        if (status == Statuses.SUCCESS) {
                            Lesson lesson = new Lesson(studentService.getById(stId), teacherService.getById(idTeach), courseService.getById(cId), time, dur,theWeekService.findByDayOfTheWeekAndTimeOfTheDayAndMinute(time.getDayOfWeek().getValue(), time.getHour(), time.getMinute()), "WILL");
                            lessonService.create(lesson);
                            Mail mail = new Mail();
                            mail.setTo(Collections.singletonList(lesson.getStudent().getEmail()));
                            mail.setSubject("Лист про додання одного заняття");
                            mail.setBody("");
                            Teacher teacher = teacherService.getById(idTeach);
                            mailService.sendEmailWithThymeleafToStudentAboutLessonAdded(mail, teacherMapper.mapTeacherToTeacherDTO(teacher), lesson.getLessonTime(), dur);
                            return "redirect:/admin/teacher/" + idTeach + "/lessons/LESSON_ADDED";
                        } else {
            return "redirect:/admin/teacher/" + idTeach + "/lessons/" + status.name();
        }
    }

    @PostMapping("/teacher/{teachId}/lessons/addCourse/{cId}/{stId}/{retDate}/{dur}")
    public String addCourseeToTeacher(@PathVariable("teachId") int idTeach, @PathVariable("cId") int cId, @PathVariable("stId") int stId, @PathVariable("retDate") String date, @PathVariable("dur") float dur, Model model) throws MessagingException {
        List<Object> objs = lessonService.checkIfLessonValidWithReputitions(idTeach, stId, cId, dur, date, 5);
        Statuses status = (Statuses) objs.get(0);
        LocalDateTime time = (LocalDateTime) objs.get(1);

        if (status == Statuses.SUCCESS) {
                        for (int i = 1; i <= 4; i++) {
                            Lesson lesson = new Lesson(studentService.getById(stId), teacherService.getById(idTeach), courseService.getById(cId), time, dur, theWeekService.findByDayOfTheWeekAndTimeOfTheDayAndMinute(time.getDayOfWeek().getValue(), time.getHour(), time.getMinute()), "WILL");
                            lessonService.create(lesson);

                            time = time.plusWeeks(1);
                        }
                        TeacherStudentTimeOfTheWeek tsw = new TeacherStudentTimeOfTheWeek(teacherService.getById(idTeach), studentService.getById(stId), theWeekService.findByDayOfTheWeekAndTimeOfTheDayAndMinute(time.getDayOfWeek().getValue(), time.getHour(), time.getMinute()), courseService.getById(cId));
                        tswService.create(tsw);


                        Mail mail = new Mail();
                        mail.setTo(Collections.singletonList(studentService.getById(stId).getEmail()));
                        mail.setSubject("Лист про додання курсу занять");
                        mail.setBody("");
                        Teacher teacher = teacherService.getById(idTeach);
                        mailService.sendEmailWithThymeleafToStudentAboutCourseAdded(mail, teacherMapper.mapTeacherToTeacherDTO(teacher), time, dur);
                        return "redirect:/admin/teacher/" + idTeach + "/lessons/COURSE_ADDED";
                }else {
        return "redirect:/admin/teacher/" + idTeach + "/lessons/" + status.name();
        }
    }

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

