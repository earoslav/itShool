package com.example.demo.controlers.closedTeacher.teacherStudent;

import com.example.demo.dto.adminLessons.LessonAdminLessonsDTO;
import com.example.demo.mapper.entity.StudentMapper;
import com.example.demo.mapper.entity.TeacherMapper;
import com.example.demo.mapper.programe.CourseMapper;
import com.example.demo.mapper.programe.LessonMapper;
import com.example.demo.models.email.Mail;
import com.example.demo.models.entities.Student;
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
@RequestMapping("/teacher")
public class TeacherLessonsWithStudentController {
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


    public TeacherLessonsWithStudentController(TeacherService teacherService, LessonMapper lessonMapper, TimeOfTheWeekService theWeekService, CourseService courseService, EmptyTimesForTeacherService emptyTimesForTeacherService, LessonService lessonService, TeacherCourseService teacherCourseService, MailService mailService, TeacherStudentTimeOfTheWeekService tswService, StudentService studentService, StudentMapper studentMapper, TeacherMapper teacherMapper, CourseMapper courseMapper) {
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

    @GetMapping("/{idTeach}/student/{idSt}/lessons")
    public String gotoStudentTeacherLessons(@PathVariable("idTeach") int idT, @PathVariable("idSt") int idSt, Model model) {
        Teacher teacher = teacherService.getById(idT);
        Student student = studentService.getById(idSt);
        List<Object> values = lessonService.compileLessonsForStudentAndTeacher(idT, idSt);
        List<String> weekDays = (List<String>) values.get(0);
        HashMap<String, LessonAdminLessonsDTO> lessonHashMap = (HashMap<String, LessonAdminLessonsDTO>) values.get(1);
        HashMap<String, LessonAdminLessonsDTO> lessonDurations = (HashMap<String, LessonAdminLessonsDTO>) values.get(2);


        List<Course> courses = new ArrayList<>();
        teacher.getTeacherCourses().stream().forEach(course -> courses.add(course.getCourse()));

        model.addAttribute("lessonDurations", lessonDurations);
        model.addAttribute("lessons", lessonHashMap);
        model.addAttribute("courses", courses.stream().map(el -> courseMapper.mapCourseToCourseDTO(el)).collect(Collectors.toList()));
        model.addAttribute("weekDays", weekDays);
        model.addAttribute("teacher", teacherMapper.mapTeacherToTeacherDTO(teacher));
        model.addAttribute("student", studentMapper.mapStudentToStudentDTO(student));
        model.addAttribute("hours", Arrays.asList("8:00", "8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30"));
        return "closedTeacher/teacherStudents/studentLessons";
    }

    @GetMapping("/{idTeach}/student/{idSt}/lessons/{output}")
    public String gotoStudentTeacherLessonsWithOutput(@PathVariable("output") String output, @PathVariable("idTeach") int idT, @PathVariable("idSt") int idSt, Model model) {
        Teacher teacher = teacherService.getById(idT);
        Student student = studentService.getById(idSt);
        List<Object> values = lessonService.compileLessonsForStudentAndTeacher(idT, idSt);
        List<String> weekDays = (List<String>) values.get(0);
        HashMap<String, LessonAdminLessonsDTO> lessonHashMap = (HashMap<String, LessonAdminLessonsDTO>) values.get(1);
        HashMap<String, LessonAdminLessonsDTO> lessonDurations = (HashMap<String, LessonAdminLessonsDTO>) values.get(2);
        List<Course> courses = new ArrayList<>();
        teacher.getTeacherCourses().stream().forEach(course -> courses.add(course.getCourse()));

        model.addAttribute("lessonDurations", lessonDurations);
        model.addAttribute("output", output);
        model.addAttribute("lessons", lessonHashMap);
        model.addAttribute("courses", courses.stream().map(el -> courseMapper.mapCourseToCourseDTO(el)).collect(Collectors.toList()));
        model.addAttribute("weekDays", weekDays);
        model.addAttribute("teacher", teacherMapper.mapTeacherToTeacherDTO(teacher));
        model.addAttribute("student", studentMapper.mapStudentToStudentDTO(student));
        model.addAttribute("hours", Arrays.asList("8:00", "8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30"));
        return "closedTeacher/teacherStudents/studentLessons";
    }

    @PostMapping("/{idTeach}/student/lessons/deleteLesson/{id}")
    @Transactional
    public String deleteLessonForStudent(@PathVariable("idTeach") int idTeach, @PathVariable("id") int idLes) throws MessagingException {
        Lesson lesson = lessonService.getById(idLes);
        System.out.println(lesson);
        Mail mail = new Mail();
        mail.setTo(Collections.singletonList(lessonService.getById(idLes).getStudent().getEmail()));
        mail.setSubject("Лист про відміну заняття");
        mail.setBody("");
        Teacher teacher = teacherService.getById(idTeach);
        mailService.sendEmailWithThymeleafToStudentAboutLessonRemoved(mail, teacherMapper.mapTeacherToTeacherDTO(teacher), lessonService.getById(idLes).getLessonTime(), lessonService.getById(idLes).getDuration());
        lessonService.deleteById(idLes);
        return "redirect:/teacher/" + idTeach + "/student/" + lesson.getStudent().getId() + "/lessons/LESSON_DELETED";
    }

    @PostMapping("/{idTeach}/student/{stId}/lessons/editLesson/notPicked")
    public String editLessoneFromTeacherWithNotPicked(@PathVariable("idTeach") int idTeach, @PathVariable("stId") int stId) {
        return "redirect:/teacher/" + idTeach + "/student/" + stId + "/lessons/ITEM_NOT_PICKED";
    }

    @PostMapping("/{idTeach}/student/lessons/deleteCourse/{id}")
    @Transactional
    public String deleteCourseForStudent(@PathVariable("idTeach") int idTeach, @PathVariable("id") int idLes) throws MessagingException {
        Lesson lesson = lessonService.getById(idLes);
        Mail mail = new Mail();
        mail.setTo(Collections.singletonList(lesson.getStudent().getEmail()));
        mail.setSubject("Лист про відміну заняття");
        mail.setBody("");
        Teacher teacher = teacherService.getById(idTeach);
        mailService.sendEmailWithThymeleafToStudentAboutCourseRemoved(mail, teacherMapper.mapTeacherToTeacherDTO(teacher), lesson.getLessonTime(), lesson.getDuration());
        lessonService.deleteAllByStIdAndTeachIdAndTswIdAndLesTimeAfterNow(lesson.getStudent().getId(), idTeach, lesson.getTimeOfTheWeek().getId(), LocalDateTime.now().minusMinutes(30));
        tswService.removeAllByStIdAndTeachIdAndTswId(lesson.getStudent().getId(), idTeach, lesson.getTimeOfTheWeek().getId());
        return "redirect:/teacher/" + idTeach + "/student/" + lesson.getStudent().getId() + "/lessons/COURSE_DELETED";
    }

    @PostMapping("/{idTeach}/student/lessons/editLesson/{id}/{nTId}/{date}")
    public String editLessonForStudent(@PathVariable("idTeach") int idTeach, @PathVariable("id") int idLes, @PathVariable("nTId") int newTimeId, @PathVariable("date") String date, Model model) throws MessagingException {
        Lesson lesson = lessonService.getById(idLes);
        List<Object> objs = lessonService.checkIfLessonValidWithReputitions(idTeach, lesson.getStudent().getId(), lesson.getCourse().getId(), idLes, lesson.getDuration(), date, 1);
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
            return "redirect:/teacher/" + idTeach + "/student/" + lesson.getStudent().getId() + "/lessons/LESSON_EDITED";
        } else {
            return "redirect:/teacher/" + idTeach + "/student/" + lesson.getStudent().getId() + "/lessons/" + status.name();
        }
    }


    @PostMapping("/{teachId}/student/{stId}/lessons/addLesson/{cId}/{retDate}/{dur}")
    public String addLessonToStudent(@PathVariable("teachId") int idTeach, @PathVariable("cId") int cId, @PathVariable("stId") int stId, @PathVariable("retDate") String date, @PathVariable("dur") float dur, Model model) throws MessagingException {
        List<Object> objs = lessonService.checkIfLessonValidWithReputitions(idTeach, stId, cId, dur, date, 1);
        Statuses status = (Statuses) objs.get(0);
        LocalDateTime time = (LocalDateTime) objs.get(1);

        if (status == Statuses.SUCCESS) {
            Lesson lesson = new Lesson(studentService.getById(stId), teacherService.getById(idTeach), courseService.getById(cId), time, dur, theWeekService.findByDayOfTheWeekAndTimeOfTheDayAndMinute(time.getDayOfWeek().getValue(), time.getHour(), time.getMinute()), "WILL");
            lessonService.create(lesson);
            Mail mail = new Mail();
            mail.setTo(Collections.singletonList(lesson.getStudent().getEmail()));
            mail.setSubject("Лист про додання одного заняття");
            mail.setBody("");
            Teacher teacher = teacherService.getById(idTeach);
            mailService.sendEmailWithThymeleafToStudentAboutLessonAdded(mail, teacherMapper.mapTeacherToTeacherDTO(teacher), lesson.getLessonTime(), dur);
            return "redirect:/teacher/" + idTeach + "/student/" + stId + "/lessons/LESSON_ADDED";
        } else {
            return "redirect:/teacher/" + idTeach + "/lessons/" + stId + "/lessons/" + status.name();
        }
    }

    @PostMapping("/{teachId}/student/{stId}/lessons/addCourse/{cId}/{retDate}/{dur}")
    public String addCourseToStudent(@PathVariable("teachId") int idTeach, @PathVariable("stId") int stId, @PathVariable("dur") float dur, @PathVariable("cId") int cId, @PathVariable("retDate") String date, Model model) throws MessagingException {
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
            return "redirect:/teacher/" + idTeach + "/student/" + stId + "/lessons/COURSE_ADDED";
        } else {
            return "redirect:/teacher/" + idTeach + "/student/" + stId + "/lessons/" + status.name();
        }
    }

    @PostMapping("/{teacherId}/acceptLessonDelete/{lessonId}")
    @Transactional
    public String acceptLessonDelete(@PathVariable("teacherId") int teacherId, @PathVariable("lessonId") int lessonId, Model model) throws MessagingException {
        System.out.println("EXCEPT");
        Mail mail = new Mail();
        mail.setTo(Collections.singletonList(lessonService.getById(lessonId).getStudent().getEmail()));
        mail.setSubject("Лист про відміну заняття");
        mail.setBody("");
        mailService.sendEmailWithThymeleafToStudentAboutDeleteLessonExcepted(mail, teacherMapper.mapTeacherToTeacherDTO(teacherService.getById(teacherId)), lessonService.getById(lessonId).getLessonTime(), lessonService.getById(lessonId).getDuration());
        lessonService.deleteById(lessonId);
        return "redirect:/teacher/" + teacherId + "/homepage/DELETE_ACCEPTED";
    }

    @GetMapping("/{teacherId}/denyLessonDelete/{lessonId}")
    public String denyLessonDelete(@PathVariable("teacherId") int teacherId, @PathVariable("lessonId") int lessonId, Model model) throws MessagingException {
        System.out.println("DENY");
        Mail mail = new Mail();
        mail.setTo(Collections.singletonList(lessonService.getById(lessonId).getStudent().getEmail()));
        mail.setSubject("Лист про відміну заняття");
        mail.setBody("");
        mailService.sendEmailWithThymeleafToStudentAboutDeleteLessonDenyed(mail, teacherMapper.mapTeacherToTeacherDTO(teacherService.getById(teacherId)), lessonService.getById(lessonId).getLessonTime(), lessonService.getById(lessonId).getDuration());
        return "redirect:/teacher/" + teacherId + "/homepage/DELETE_DENYED";
    }

    @PostMapping("/{idTeach}/addMoneyByLessonWithStudent/{lesId}/{stId}")
    public String addMoney(@PathVariable("idTeach") int teachId, @PathVariable("lesId") int lesId, @PathVariable("stId") int stId, Model model) {
        Teacher teacher = teacherService.getById(teachId);
        Lesson lesson = lessonService.getById(lesId);
        if (LocalDateTime.now().isAfter(lesson.getLessonTime())) {
            float sum = lesson.getDuration() * lesson.getCourse().getTeacherShare();
            teacher.setUnpaidMoney(teacher.getUnpaidMoney() + sum);
            teacherService.update(teachId, teacher);
            lesson.setStatus("WAS");
            lessonService.update(lesId, lesson);
            return "redirect:/teacher/" + teachId + "/student/" + stId + "/lessons/LESSON_STATUS_CHANGED";
        } else {
            return "redirect:/teacher/" + teachId + "/student/" + stId + "/lessons/LESSON_BEFORE_NOW";
        }
    }
}
