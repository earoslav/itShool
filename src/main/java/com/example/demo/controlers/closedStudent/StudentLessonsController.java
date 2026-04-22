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
import tools.jackson.databind.ObjectMapper;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

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
    @GetMapping("/{idSt}/lessons")
    public String gotoLessons(Model model, @PathVariable("idSt") int id) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Student student = studentService.getById(id);
        List<String> weekDays = (List<String>) lessonService.compileLessonsForStudent(student.getId()).get(0);
        HashMap<String, LessonAdminLessonsDTO> lessonHashMap = (HashMap<String, LessonAdminLessonsDTO>) lessonService.compileLessonsForStudent(student.getId()).get(1);
        model.addAttribute("lessons", lessonHashMap);
        model.addAttribute("weekDays", weekDays);
        model.addAttribute("student", studentMapper.mapStudentToStudentDTO(student));
        model.addAttribute("hours", Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19,20,21));
        return "closedStudent/studentLessons/lessons";
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
            mailService.sendRequestWithThymeleafTeacherAboutRequestLessonRemoved(mail, studentMapper.mapStudentToStudentDTO(student),lessonService.getById(idLes).getTeacher().getId(),idLes, lessonService.getById(idLes).getLessonTime().getDayOfMonth()+":"+lessonService.getById(idLes).getLessonTime().getMonth(), lessonService.getById(idLes).getLessonTime().getHour());

            return "redirect:/student/"+idSt+"/lessons";
        }else{
            Student student = studentService.getById(idSt);
            List<String> weekDays = (List<String>) lessonService.compileLessonsForStudent(student.getId()).get(0);
            HashMap<String, LessonAdminLessonsDTO> lessonHashMap = (HashMap<String, LessonAdminLessonsDTO>) lessonService.compileLessonsForStudent(student.getId()).get(1);
            model.addAttribute("lessons", lessonHashMap);
            model.addAttribute("weekDays", weekDays);
            model.addAttribute("student", studentMapper.mapStudentToStudentDTO(student));
            model.addAttribute("hours", Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19,20,21));
            model.addAttribute("error", "tooLate");
            return "closedStudent/studentLessons/lessons";
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
        mailService.sendEmailWithThymeleafTeacherAboutCourseRemoved(mail, studentMapper.mapStudentToStudentDTO(student), lesson.getLessonTime().getDayOfMonth()+":"+lesson.getLessonTime().getMonth(), lesson.getLessonTime().getHour());
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
                mailService.sendEmailWithThymeleafToTeacherAboutLessonTimeEdited(mail, studentMapper.mapStudentToStudentDTO(student), lesson.getLessonTime().getDayOfMonth()+":"+lesson.getLessonTime().getMonth(),lesson.getLessonTime().getHour(), newDate.getDayOfMonth()+":"+newDate.getMonth(),newDate.getHour());
                lesson.setLessonTime(newDate);
                lessonService.update(lesson.getId(), lesson);
                return "redirect:/student/"+idSt+"/lessons";
            }else{
                Student student = studentService.getById(idSt);
                List<String> weekDays = (List<String>) lessonService.compileLessonsForStudent(student.getId()).get(0);
                HashMap<String, LessonAdminLessonsDTO> lessonHashMap = (HashMap<String, LessonAdminLessonsDTO>) lessonService.compileLessonsForStudent(student.getId()).get(1);
                model.addAttribute("lessons", lessonHashMap);
                model.addAttribute("weekDays", weekDays);
                model.addAttribute("student", studentMapper.mapStudentToStudentDTO(student));
                model.addAttribute("hours", Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19,20,21));
                model.addAttribute("error", "dateInconsistency");
                return "closedStudent/studentLessons/lessons";
            }
        }else{
            Student student = studentService.getById(idSt);
            List<String> weekDays = (List<String>) lessonService.compileLessonsForStudent(student.getId()).get(0);
            HashMap<String, LessonAdminLessonsDTO> lessonHashMap = (HashMap<String, LessonAdminLessonsDTO>) lessonService.compileLessonsForStudent(student.getId()).get(1);
            model.addAttribute("lessons", lessonHashMap);
            model.addAttribute("weekDays", weekDays);
            model.addAttribute("student", studentMapper.mapStudentToStudentDTO(student));
            model.addAttribute("hours", Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19,20,21));
            model.addAttribute("error", "tooLate");
            return "closedStudent/studentLessons/lessons";
        }
    }
}
