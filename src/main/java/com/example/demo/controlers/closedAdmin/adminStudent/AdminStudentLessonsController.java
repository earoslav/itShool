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
import com.example.demo.models.other.TimeOfTheWeek;
import com.example.demo.models.programe.Course;
import com.example.demo.models.programe.Lesson;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminStudentLessonsController {
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
    private TimeOfTheWeekMapper theWeekMapper;

    public AdminStudentLessonsController(TeacherCourseService teacherCourseService, TeacherService teacherService, EmptyTimesForTeacherService emptyTimesForTeacherService, TimeOfTheWeekService theWeekService, CourseService courseService, StudentCourseService studentCourseService, CommentService commentService, LessonService lessonService, StudentService studentService, LessonMapper lessonMapper, TeacherStudentTimeOfTheWeekService tswService, MailService mailService, AdminService adminService, TeacherMapper teacherMapper, StudentMapper studentMapper, CourseMapper courseMapper, TimeOfTheWeekMapper theWeekMapper) {
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
        this.theWeekMapper = theWeekMapper;
    }
    @GetMapping("/student/{idSt}/lessons")
    public String gotoStudentLessons(@PathVariable("idSt") int idSt, Model model){
        List<Teacher> teachers = teacherService.getAll();
        Student student = studentService.getById(idSt);
        List<String> weekDays = (List<String>) lessonService.compileLessonsForStudentInAdmin(student.getId()).get(0);
        HashMap<String, LessonAdminLessonsDTO> lessonHashMap = (HashMap<String, LessonAdminLessonsDTO>) lessonService.compileLessonsForStudentInAdmin(student.getId()).get(1);
        List<Course> courses = courseService.getAll();
        model.addAttribute("lessons", lessonHashMap);
        model.addAttribute("courses", courses.stream().map(el->courseMapper.mapCourseToCourseDTO(el)).collect(Collectors.toList()));
        model.addAttribute("weekDays", weekDays);
        model.addAttribute("teachers", teachers.stream().map(el->teacherMapper.mapTeacherToTeacherDTO(el)).collect(Collectors.toList()));
        model.addAttribute("student", studentMapper.mapStudentToStudentDTO(student));
        model.addAttribute("hours", Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19,20,21));
        return "closedAdmin/adminStudents/studentLessons";
    }
    @PostMapping("/student/{idSt}/lessons/deleteLesson/{id}")
    @Transactional
    public String deleteLesson(@PathVariable("idSt") int idSt, @PathVariable("id") int idLes, Model model) throws MessagingException {

        Mail mail = new Mail();
        mail.setTo(Collections.singletonList((lessonService.getById(idLes).getTeacher().getEmail())));
        mail.setSubject("Лист про відміну заняття");
        mail.setBody("");
        StudentDTO student = studentMapper.mapStudentToStudentDTO(studentService.getById(idSt));
        mailService.sendEmailWithThymeleafTeacherAboutLessonRemoved(mail, student, lessonService.getById(idLes).getLessonTime().getDayOfMonth()+":"+lessonService.getById(idLes).getLessonTime().getMonth(), lessonService.getById(idLes).getLessonTime().getHour());
        lessonService.deleteById(idLes);
        return "redirect:/admin/student/"+idSt+"/lessons";
    }

    @PostMapping("/student/{idSt}/lessons/deleteCourse/{id}")
    @Transactional
    public String deleteCourse(@PathVariable("idSt") int idSt, @PathVariable("id") int idLes) throws MessagingException {
        Lesson lesson = lessonService.getById(idLes);
        Mail mail = new Mail();
        mail.setTo(Collections.singletonList(lesson.getTeacher().getEmail()));
        mail.setSubject("Лист про відміну заняття");
        mail.setBody("");
        StudentDTO student = studentMapper.mapStudentToStudentDTO(studentService.getById(idSt));
        mailService.sendEmailWithThymeleafTeacherAboutCourseRemoved(mail, student, lesson.getLessonTime().getDayOfMonth()+":"+lesson.getLessonTime().getMonth(), lesson.getLessonTime().getHour());
        lessonService.deleteAllByStIdAndTeachIdAndTswIdAndLesTimeAfterNow(idSt, lesson.getTeacher().getId(), lesson.getTimeOfTheWeek().getId(), LocalDateTime.now().minusMinutes(30));
        tswService.removeAllByStIdAndTeachIdAndTswId(idSt, lesson.getTeacher().getId(), lesson.getTimeOfTheWeek().getId());
        return "redirect:/admin/student/"+idSt+"/lessons";
    }
    @PostMapping("/student/{idSt}/lessons/editLesson/{id}/{nTId}/{date}")
    @Transactional
    public String deleteCourse(@PathVariable("idSt") int idSt, @PathVariable("id") int idLes, @PathVariable("nTId") int newTimeId,@PathVariable("date") String date, Model model) throws MessagingException {
        TimeOfTheWeek newTime = theWeekService.getById(newTimeId);
        Lesson lesson = lessonService.getById(idLes);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime newDate = LocalDateTime.now().withYear(Integer.parseInt(date.split("\\.")[2])).withMonth(Integer.parseInt(date.split("\\.")[1])).withDayOfMonth(Integer.parseInt(date.split("\\.")[0])).withHour(newTime.getTimeOfTheDay()).withMinute(0).withSecond(0);
        if(newDate.getDayOfWeek().getValue()==newTime.getDayOfTheWeek()){
            Mail mail = new Mail();
            mail.setTo(Collections.singletonList(lesson.getTeacher().getEmail()));
            mail.setSubject("Лист про зміну часу одного заняття");
            mail.setBody("");
            StudentDTO student = studentMapper.mapStudentToStudentDTO(studentService.getById(idSt));
            mailService.sendEmailWithThymeleafToTeacherAboutLessonTimeEdited(mail, student, lesson.getLessonTime().getDayOfMonth()+":"+lesson.getLessonTime().getMonth(),lesson.getLessonTime().getHour(), newDate.getDayOfMonth()+":"+newDate.getMonth(),newDate.getHour());
            lesson.setLessonTime(newDate);
            lessonService.update(lesson.getId(), lesson);
            return "redirect:/admin/student/"+idSt+"/lessons";
        }else{
            List<Teacher> teachers = teacherService.getAll();
            Student student = studentService.getById(idSt);
            List<String> weekDays = (List<String>) lessonService.compileLessonsForStudentInAdmin(student.getId()).get(0);
            HashMap<String, LessonAdminLessonsDTO> lessonHashMap = (HashMap<String, LessonAdminLessonsDTO>) lessonService.compileLessonsForStudentInAdmin(student.getId()).get(1);
            List<Course> courses = courseService.getAll();
            model.addAttribute("lessons", lessonHashMap);
            model.addAttribute("courses", courses.stream().map(el->courseMapper.mapCourseToCourseDTO(el)).collect(Collectors.toList()));
            model.addAttribute("weekDays", weekDays);
            model.addAttribute("teachers", teachers.stream().map(el->teacherMapper.mapTeacherToTeacherDTO(el)).collect(Collectors.toList()));
            model.addAttribute("student", studentMapper.mapStudentToStudentDTO(student));
            model.addAttribute("hours", Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19,20,21));
            return "closedAdmin/adminStudents/studentLessons";
        }
    }
}
