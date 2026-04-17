package com.example.demo.controlers.closedStudent;

import com.example.demo.dto.adminLessons.LessonAdminLessonsDTO;
import com.example.demo.mapper.LessonMapper;
import com.example.demo.models.*;
import com.example.demo.services.*;
import jakarta.jws.WebParam;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.ObjectMapper;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/student")
public class StudentController {
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

    public StudentController(TeacherService teacherService, LessonMapper lessonMapper, TimeOfTheWeekService theWeekService, CourseService courseService, EmptyTimesForTeacherService emptyTimesForTeacherService, LessonService lessonService, TeacherCourseService teacherCourseService, MailService mailService, TeacherStudentTimeOfTheWeekService tswService, StudentService studentService, ObjectMapper objectMapper) {
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
    }
    @GetMapping("/{idSt}/homepage")
    public String gotoTeacher(@PathVariable("idSt") int id, Model model){
        Student student = studentService.getById(id);
        student.setPassword("");
        model.addAttribute("student", student);
        return "closedStudent/homepage";
    }
    @PostMapping("/{idSt}/homepage")
    public String updateTeacher(@ModelAttribute("student")Student student,@PathVariable("idSt") int id,
                                Model model) {
        if(studentService.checkIfExistsByEmail(student.getEmail()) && !studentService.getById(student.getId()).getEmail().equals(student.getEmail())){
            model.addAttribute("error", "exists");

            model.addAttribute("student", student);
            return "closedStudent/homepage";
        }
        else{
            if(student.getPassword().isEmpty()){
                student.setPassword(studentService.getById(student.getId()).getPassword());
            }

            studentService.update(student.getId(), student);

        }
        return "redirect:/student/"+student.getId()+"/homepage";
    }
    @GetMapping("/{idSt}/lessons")
    public String gotoLessons(Model model, @PathVariable("idSt") int id) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Student student = studentService.getById(id);
        List<String> weekDays = (List<String>) lessonService.compileLessonsForStudent(student).get(0);
        HashMap<String, LessonAdminLessonsDTO> lessonHashMap = (HashMap<String, LessonAdminLessonsDTO>) lessonService.compileLessonsForStudent(student).get(1);
        model.addAttribute("lessons", lessonHashMap);
        model.addAttribute("weekDays", weekDays);
        model.addAttribute("student", student);
        model.addAttribute("hours", Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19,20,21));
        return "closedStudent/lessons";
    }
    @PostMapping("/{idSt}/lessons/deleteLesson/{id}")
    @Transactional
    public String deleteLesson(@PathVariable("idSt") int idSt, @PathVariable("id") int idLes, Model model) throws MessagingException {
        if(lessonService.getById(idLes).getLessonTime().isAfter(LocalDateTime.now().plusHours(12))){
            Mail mail = new Mail();
            mail.setTo(Collections.singletonList((lessonService.getById(idLes).getTeacher().getEmail())));
            mail.setSubject("Лист про відміну заняття");
            mail.setBody("");
            Student student = studentService.getById(idSt);
            mailService.sendEmailWithThymeleafTeacherAboutLessonRemoved(mail, student, lessonService.getById(idLes).getLessonTime().getDayOfMonth()+":"+lessonService.getById(idLes).getLessonTime().getMonth(), lessonService.getById(idLes).getLessonTime().getHour());
            lessonService.deleteById(idLes);
            return "redirect:/student/"+idSt+"/lessons";
        }else{
            Student student = studentService.getById(idSt);
            List<String> weekDays = (List<String>) lessonService.compileLessonsForStudent(student).get(0);
            HashMap<String, LessonAdminLessonsDTO> lessonHashMap = (HashMap<String, LessonAdminLessonsDTO>) lessonService.compileLessonsForStudent(student).get(1);
            model.addAttribute("lessons", lessonHashMap);
            model.addAttribute("weekDays", weekDays);
            model.addAttribute("student", student);
            model.addAttribute("hours", Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19,20,21));
            model.addAttribute("error", "tooLate");
            return "closedStudent/lessons";
        }

    }

    @PostMapping("/{idSt}/lessons/deleteCourse/{id}")
    @Transactional
    public String deleteCourse(@PathVariable("idSt") int idSt, @PathVariable("id") int idLes) throws MessagingException {
        Lesson lesson = lessonService.getById(idLes);
        Mail mail = new Mail();
        mail.setTo(Collections.singletonList(lesson.getTeacher().getEmail()));
        mail.setSubject("Лист про відміну заняття");
        mail.setBody("");
        Student student = studentService.getById(idSt);
        mailService.sendEmailWithThymeleafTeacherAboutCourseRemoved(mail, student, lesson.getLessonTime().getDayOfMonth()+":"+lesson.getLessonTime().getMonth(), lesson.getLessonTime().getHour());
        lessonService.deleteAllByStIdAndTeachIdAndTswIdAndLesTimeAfterNow(idSt, lesson.getTeacher().getId(), lesson.getTimeOfTheWeek().getId(), LocalDateTime.now().minusMinutes(30));
        tswService.removeAllByStIdAndTeachIdAndTswId(idSt, lesson.getTeacher().getId(), lesson.getTimeOfTheWeek().getId());
        return "redirect:/student/"+idSt+"/lessons";
    }
    @PostMapping("/{idSt}/lessons/editLesson/{id}/{nTId}/{date}")
    public String deleteCourse(@PathVariable("idSt") int idSt, @PathVariable("id") int idLes, @PathVariable("nTId") int newTimeId,@PathVariable("date") String date, Model model) throws MessagingException {
        TimeOfTheWeek newTime = theWeekService.getById(newTimeId);
        Lesson lesson = lessonService.getById(idLes);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime newDate = LocalDateTime.now().withYear(Integer.parseInt(date.split("\\.")[2])).withMonth(Integer.parseInt(date.split("\\.")[1])).withDayOfMonth(Integer.parseInt(date.split("\\.")[0])).withHour(newTime.getTimeOfTheDay()).withMinute(0).withSecond(0);
        if(newDate.isAfter(now.plusHours(12))){
            if(newDate.getDayOfWeek().getValue()==newTime.getDayOfTheWeek()){

                Mail mail = new Mail();
                mail.setTo(Collections.singletonList(lesson.getTeacher().getEmail()));
                mail.setSubject("Лист про зміну часу одного заняття");
                mail.setBody("");
                Student student = studentService.getById(idSt);
                mailService.sendEmailWithThymeleafToTeacherAboutLessonTimeEdited(mail, student, lesson.getLessonTime().getDayOfMonth()+":"+lesson.getLessonTime().getMonth(),lesson.getLessonTime().getHour(), newDate.getDayOfMonth()+":"+newDate.getMonth(),newDate.getHour());
                lesson.setLessonTime(newDate);
                lessonService.update(lesson.getId(), lesson);
                return "redirect:/student/"+idSt+"/lessons";
            }else{
                Student student = studentService.getById(idSt);
                List<String> weekDays = (List<String>) lessonService.compileLessonsForStudent(student).get(0);
                HashMap<String, LessonAdminLessonsDTO> lessonHashMap = (HashMap<String, LessonAdminLessonsDTO>) lessonService.compileLessonsForStudent(student).get(1);
                model.addAttribute("lessons", lessonHashMap);
                model.addAttribute("weekDays", weekDays);
                model.addAttribute("student", student);
                model.addAttribute("hours", Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19,20,21));
                model.addAttribute("error", "dateInconsistency");
                return "closedStudent/lessons";
            }
        }else{
            Student student = studentService.getById(idSt);
            List<String> weekDays = (List<String>) lessonService.compileLessonsForStudent(student).get(0);
            HashMap<String, LessonAdminLessonsDTO> lessonHashMap = (HashMap<String, LessonAdminLessonsDTO>) lessonService.compileLessonsForStudent(student).get(1);
            model.addAttribute("lessons", lessonHashMap);
            model.addAttribute("weekDays", weekDays);
            model.addAttribute("student", student);
            model.addAttribute("hours", Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19,20,21));
            model.addAttribute("error", "tooLate");
            return "closedStudent/lessons";
        }
    }
}
