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
    public String gotoLessons(@PathVariable("id") int id, Model model){
        Teacher teacher = teacherService.getById(id);
        List<String> weekDays = (List<String>) lessonService.compileLessonsForTeacher(teacher.getId()).get(0);
        HashMap<String, LessonAdminLessonsDTO> lessonHashMap = (HashMap<String, LessonAdminLessonsDTO>) lessonService.compileLessonsForTeacher(teacher.getId()).get(1);
        List<Course> courses = new ArrayList<>();
        teacher.getTeacherCourses().stream().forEach(course->courses.add(course.getCourse()));

        model.addAttribute("lessons", lessonHashMap);
        model.addAttribute("students", studentService.getAll().stream().map(el->studentMapper.mapStudentToStudentDTO(el)).collect(Collectors.toList()));
        model.addAttribute("courses", courses.stream().map(el->courseMapper.mapCourseToCourseDTO(el)).collect(Collectors.toList()));
        model.addAttribute("weekDays", weekDays);
        model.addAttribute("teacher", teacherMapper.mapTeacherToTeacherDTO(teacher));
        model.addAttribute("hours", Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19,20,21));
        return "closedAdmin/adminTeachers/teacherLessons";
    }

    @GetMapping("/teacher/{id}/lessons/{output}")
    public String gotoLessons(@PathVariable("id") int id,@PathVariable("output") String output, Model model){
        Teacher teacher = teacherService.getById(id);
        List<String> weekDays = (List<String>) lessonService.compileLessonsForTeacher(teacher.getId()).get(0);
        HashMap<String, LessonAdminLessonsDTO> lessonHashMap = (HashMap<String, LessonAdminLessonsDTO>) lessonService.compileLessonsForTeacher(teacher.getId()).get(1);
        List<Course> courses = new ArrayList<>();
        teacher.getTeacherCourses().stream().forEach(course->courses.add(course.getCourse()));

        model.addAttribute("output", output);
        model.addAttribute("lessons", lessonHashMap);
        model.addAttribute("students", studentService.getAll().stream().map(el->studentMapper.mapStudentToStudentDTO(el)).collect(Collectors.toList()));
        model.addAttribute("courses", courses.stream().map(el->courseMapper.mapCourseToCourseDTO(el)).collect(Collectors.toList()));
        model.addAttribute("weekDays", weekDays);
        model.addAttribute("teacher", teacherMapper.mapTeacherToTeacherDTO(teacher));
        model.addAttribute("hours", Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19,20,21));
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
        mailService.sendEmailWithThymeleafToStudentAboutLessonRemoved(mail, teacherMapper.mapTeacherToTeacherDTO(teacher), lessonService.getById(idLes).getLessonTime().getDayOfMonth()+":"+lessonService.getById(idLes).getLessonTime().getMonth(), lessonService.getById(idLes).getLessonTime().getHour());
        lessonService.deleteById(idLes);
        return "redirect:/admin/teacher/"+idTeach+"/lessons/lessonDeleted";
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
        mailService.sendEmailWithThymeleafToStudentAboutCourseRemoved(mail, teacherMapper.mapTeacherToTeacherDTO(teacher), lesson.getLessonTime().getDayOfMonth()+":"+lesson.getLessonTime().getMonth(), lesson.getLessonTime().getHour());
        lessonService.deleteAllByStIdAndTeachIdAndTswIdAndLesTimeAfterNow(lesson.getStudent().getId(), idTeach, lesson.getTimeOfTheWeek().getId(), LocalDateTime.now().minusMinutes(30));
        tswService.removeAllByStIdAndTeachIdAndTswId(lesson.getStudent().getId(), idTeach, lesson.getTimeOfTheWeek().getId());
        return "redirect:/admin/teacher/"+idTeach+"/lessons/courseDeleted";
    }
    @PostMapping("/teacher/{idTeach}/lessons/editLesson/{id}/{nTId}/{date}")
    public String editLessoneFromTeacher(@PathVariable("idTeach") int idTeach, @PathVariable("id") int idLes, @PathVariable("nTId") int newTimeId,@PathVariable("date") String date, Model model) throws MessagingException {
        TimeOfTheWeek newTime = theWeekService.getById(newTimeId);
        Lesson lesson = lessonService.getById(idLes);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime newDate = LocalDateTime.now().withYear(Integer.parseInt(date.split("\\.")[2])).withMonth(Integer.parseInt(date.split("\\.")[1])).withDayOfMonth(Integer.parseInt(date.split("\\.")[0])).withHour(newTime.getTimeOfTheDay()).withMinute(0).withSecond(0);
        if(newDate.getDayOfWeek().getValue()==newTime.getDayOfTheWeek()){
            Mail mail = new Mail();
            mail.setTo(Collections.singletonList(lesson.getStudent().getEmail()));
            mail.setSubject("Лист про зміну часу одного заняття");
            mail.setBody("");
            Teacher teacher = teacherService.getById(idTeach);
            mailService.sendEmailWithThymeleafToStudentAboutLessonTimeEdited(mail, teacherMapper.mapTeacherToTeacherDTO(teacher), lesson.getLessonTime().getDayOfMonth()+":"+lesson.getLessonTime().getMonth(),lesson.getLessonTime().getHour(), newDate.getDayOfMonth()+":"+newDate.getMonth(),newDate.getHour());
            lesson.setLessonTime(newDate);
            lessonService.update(lesson.getId(), lesson);
            return "redirect:/admin/teacher/"+idTeach+"/lessons/lessonEdited";
        }else{
            return "redirect:/admin/teacher/"+idTeach+"/lessons/dateInconsistency";
        }

    }
    @PostMapping("/teacher/{teachId}/lessons/addLesson/{cId}/{stId}/{retDate}")
    public String addLessoneToTeacher(@PathVariable("teachId") int idTeach, @PathVariable("cId") int cId, @PathVariable("stId") int stId,@PathVariable("retDate") String date, Model model) throws MessagingException {
        int dayOfMonth = Integer.parseInt(date.split(" ")[1].split("\\.")[0]);
        int month = Integer.parseInt(date.split(" ")[1].split("\\.")[1]);
        int hour = Integer.parseInt(date.split(" ")[2]);
        LocalDateTime time = LocalDateTime.now();
        if(time.getMonth().getValue()==12 && month==1){
            time = time.withYear(time.getYear()+1).withMonth(month).withDayOfMonth(dayOfMonth).withHour(hour).withMinute(0).withSecond(0);
        }else{
            time = time.withMonth(month).withDayOfMonth(dayOfMonth).withHour(hour).withMinute(0).withSecond(0);
        }
        if(time.isAfter(LocalDateTime.now())){
            if(cId!=0){
                if(stId!=0){
                    Lesson lesson = new Lesson(studentService.getById(stId), teacherService.getById(idTeach), courseService.getById(cId), time, 1, theWeekService.findByDayOfTheWeekAndTimeOfTheDay(time.getDayOfWeek().getValue(),time.getHour()), "will");
                    lessonService.create(lesson);
                    Mail mail = new Mail();
                    mail.setTo(Collections.singletonList(lesson.getStudent().getEmail()));
                    mail.setSubject("Лист про додання одного заняття");
                    mail.setBody("");
                    Teacher teacher = teacherService.getById(idTeach);
                    mailService.sendEmailWithThymeleafToStudentAboutLessonAdded(mail, teacherMapper.mapTeacherToTeacherDTO(teacher), lesson.getLessonTime().getDayOfMonth()+":"+lesson.getLessonTime().getMonth(),lesson.getLessonTime().getHour());
                    return "redirect:/admin/teacher/"+idTeach+"/lessons/lessonAdded";
                }else{
                    return "redirect:/admin/teacher/"+idTeach+"/lessons/studentNotFound";
                }
            }else{
                return "redirect:/admin/teacher/"+idTeach+"/lessons/courseNotFound";
            }

        }else{
            return "redirect:/admin/teacher/"+idTeach+"/lessons/lessonBeforeNow";
        }
    }
    @PostMapping("/teacher/{teachId}/lessons/addCourse/{cId}/{stId}/{retDate}")
    public String addCourseeToTeacher(@PathVariable("teachId") int idTeach, @PathVariable("cId") int cId, @PathVariable("stId") int stId,@PathVariable("retDate") String date, Model model) throws MessagingException {
        int dayOfMonth = Integer.parseInt(date.split(" ")[1].split("\\.")[0]);
        int month = Integer.parseInt(date.split(" ")[1].split("\\.")[1]);
        int hour = Integer.parseInt(date.split(" ")[2]);
        LocalDateTime time = LocalDateTime.now();
        if(time.getMonth().getValue()==12 && month==1){
            time = time.withYear(time.getYear()+1).withMonth(month).withDayOfMonth(dayOfMonth).withHour(hour).withMinute(0).withSecond(0).withNano(0);
        }else{
            time = time.withMonth(month).withDayOfMonth(dayOfMonth).withHour(hour).withMinute(0).withSecond(0).withNano(0);
        }
        boolean hasCourse = false;
        for(TeacherStudentTimeOfTheWeek tsw : tswService.findAllByTeachId(idTeach)){
            if(tsw.getTimeOfTheWeek().getDayOfTheWeek()==time.getDayOfWeek().getValue() && tsw.getTimeOfTheWeek().getTimeOfTheDay()==time.getHour()){
                hasCourse = true;
            }
        }
        if(time.isAfter(LocalDateTime.now()) && !hasCourse){
            if(cId!=0){
                if(stId!=0){
                    AtomicBoolean exists = new AtomicBoolean(false);
                    Lesson lesson = new Lesson();
                    LocalDateTime checkingTime = time;
                    for(int i = 1; i<=5; i++){
                        for(Lesson les : teacherService.getById(idTeach).getLessons()){
                            if(les.getLessonTime().getMonth().getValue()==checkingTime.getMonth().getValue()&&les.getLessonTime().getDayOfMonth()==checkingTime.getDayOfMonth()&&les.getLessonTime().getHour()==checkingTime.getHour()){
                                exists.set(true);
                            }
                        }
                        checkingTime = checkingTime.plusWeeks(1);
                    }
                    if(exists.get()){
                        return "redirect:/admin/teacher/"+idTeach+"/lessons/timeNotEmpty";
                    }else {
                        for(int i = 1; i<=4; i++){
                            lesson = new Lesson(studentService.getById(stId), teacherService.getById(idTeach), courseService.getById(cId), time, 1, theWeekService.findByDayOfTheWeekAndTimeOfTheDay(time.getDayOfWeek().getValue(),time.getHour()), "will");
                            lessonService.create(lesson);

                            time = time.plusWeeks(1);
                        }
                        TeacherStudentTimeOfTheWeek tsw = new TeacherStudentTimeOfTheWeek(teacherService.getById(idTeach), studentService.getById(stId), theWeekService.findByDayOfTheWeekAndTimeOfTheDay(time.getDayOfWeek().getValue(),time.getHour()), courseService.getById(cId));
                        tswService.create(tsw);


                        Mail mail = new Mail();
                        mail.setTo(Collections.singletonList(studentService.getById(stId).getEmail()));
                        mail.setSubject("Лист про додання курсу занять");
                        mail.setBody("");
                        Teacher teacher = teacherService.getById(idTeach);
                        mailService.sendEmailWithThymeleafToStudentAboutCourseAdded(mail, teacherMapper.mapTeacherToTeacherDTO(teacher), String.valueOf(lesson.getLessonTime().getDayOfWeek()),lesson.getLessonTime().getHour());
                        return "redirect:/admin/teacher/"+idTeach+"/lessons/courseAdded";
                    }
                }else{
                    return "redirect:/admin/teacher/"+idTeach+"/lessons/studentNotFound";
                }
            }else{
                return "redirect:/admin/teacher/"+idTeach+"/lessons/courseNotFound";
            }

        }else{
            return "redirect:/admin/teacher/"+idTeach+"/lessons/lessonBeforeNow";
        }
    }
    @PostMapping("/teacher/{idTeach}/addMoneyByLesson/{lesId}")
    public String addMoney(@PathVariable("idTeach") int teachId, @PathVariable("lesId") int lesId, Model model){
        Teacher teacher = teacherService.getById(teachId);
        Lesson lesson = lessonService.getById(lesId);
        float sum = lesson.getDuration()*lesson.getCourse().getTeacherShare();
        teacher.setUnpaidMoney(teacher.getUnpaidMoney()+sum);
        teacherService.update(teachId, teacher);
        lesson.setStatus("was");
        lessonService.update(lesId, lesson);
        return "redirect:/admin/teacher/"+teachId+"/lessons/lessonStatusChanged";
    }


}

