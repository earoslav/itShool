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
import org.springframework.boot.jackson.autoconfigure.JacksonProperties;
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
    public String gotoStudentTeacherLessons(@PathVariable("idTeach") int idT, @PathVariable("idSt") int idSt, Model model){
        Teacher teacher = teacherService.getById(idT);
        Student student = studentService.getById(idSt);
        List<String> weekDays = (List<String>) lessonService.compileLessonsForStudentAndTeacher(idT, idSt).get(0);
        HashMap<String, LessonAdminLessonsDTO> lessonHashMap = (HashMap<String, LessonAdminLessonsDTO>) lessonService.compileLessonsForStudentAndTeacher(idT, idSt).get(1);
        List<Course> courses = new ArrayList<>();
        teacher.getTeacherCourses().stream().forEach(course->courses.add(course.getCourse()));


        model.addAttribute("lessons", lessonHashMap);
        model.addAttribute("courses", courses.stream().map(el->courseMapper.mapCourseToCourseDTO(el)).collect(Collectors.toList()));
        model.addAttribute("weekDays", weekDays);
        model.addAttribute("teacher", teacherMapper.mapTeacherToTeacherDTO(teacher));
        model.addAttribute("student", studentMapper.mapStudentToStudentDTO(student));
        model.addAttribute("hours", Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19,20,21));
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
        mailService.sendEmailWithThymeleafToStudentAboutLessonRemoved(mail, teacherMapper.mapTeacherToTeacherDTO(teacher), lessonService.getById(idLes).getLessonTime().getDayOfMonth()+":"+lessonService.getById(idLes).getLessonTime().getMonth(), lessonService.getById(idLes).getLessonTime().getHour());
        lessonService.deleteById(idLes);
        return "redirect:/teacher/"+idTeach+"/student/"+lesson.getStudent().getId()+"/lessons";
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
        mailService.sendEmailWithThymeleafToStudentAboutCourseRemoved(mail, teacherMapper.mapTeacherToTeacherDTO(teacher), lesson.getLessonTime().getDayOfMonth()+":"+lesson.getLessonTime().getMonth(), lesson.getLessonTime().getHour());
        lessonService.deleteAllByStIdAndTeachIdAndTswIdAndLesTimeAfterNow(lesson.getStudent().getId(), idTeach, lesson.getTimeOfTheWeek().getId(), LocalDateTime.now().minusMinutes(30));
        tswService.removeAllByStIdAndTeachIdAndTswId(lesson.getStudent().getId(), idTeach, lesson.getTimeOfTheWeek().getId());
        return "redirect:/teacher/"+idTeach+"/student/"+lesson.getStudent().getId()+"/lessons";
    }
    @PostMapping("/{idTeach}/student/lessons/editLesson/{id}/{nTId}/{date}")
    public String editLessonForStudent(@PathVariable("idTeach") int idTeach, @PathVariable("id") int idLes, @PathVariable("nTId") int newTimeId,@PathVariable("date") String date, Model model) throws MessagingException {
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
            return "redirect:/teacher/"+idTeach+"/student/"+lesson.getStudent().getId()+"/lessons";
        }else{
            Teacher teacher = teacherService.getById(idTeach);
            List<String> weekDays = (List<String>) lessonService.compileLessonsForStudentAndTeacher(idTeach, lesson.getStudent().getId()).get(0);
            HashMap<String, LessonAdminLessonsDTO> lessonHashMap = (HashMap<String, LessonAdminLessonsDTO>) lessonService.compileLessonsForStudentAndTeacher(idTeach, lesson.getStudent().getId()).get(1);
            model.addAttribute("lessons", lessonHashMap);
            model.addAttribute("weekDays", weekDays);
            model.addAttribute("teacher", teacherMapper.mapTeacherToTeacherDTO(teacher));
            model.addAttribute("hours", Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19,20,21));
            model.addAttribute("error", "dateInconsistency");
            model.addAttribute("student", studentMapper.mapStudentToStudentDTO(lesson.getStudent()));
            return "closedTeacher/teacherStudents/studentLessons";
        }


    }
    @PostMapping("/{teachId}/student/{stId}/lessons/addLesson/{cId}/{retDate}")
    public String addLessonToStudent(@PathVariable("teachId") int idTeach, @PathVariable("cId") int cId, @PathVariable("stId") int stId,@PathVariable("retDate") String date, Model model) throws MessagingException {
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
                    return "redirect:/teacher/"+idTeach+"/student/"+stId+"/lessons";
                }else{
                    Teacher teacher = teacherService.getById(idTeach);
                    List<Course> courses = new ArrayList<>();
                    teacher.getTeacherCourses().stream().forEach(tc->courses.add(tc.getCourse()));
                    List<String> weekDays = (List<String>) lessonService.compileLessonsForStudentAndTeacher(idTeach, stId).get(0);
                    HashMap<String, LessonAdminLessonsDTO> lessonHashMap = (HashMap<String, LessonAdminLessonsDTO>) lessonService.compileLessonsForStudentAndTeacher(idTeach, stId).get(1);
                    model.addAttribute("lessons", lessonHashMap);
                    model.addAttribute("student", studentMapper.mapStudentToStudentDTO(studentService.getById(stId)));
                    model.addAttribute("courses", courses.stream().map(el->courseMapper.mapCourseToCourseDTO(el)).collect(Collectors.toList()));
                    model.addAttribute("weekDays", weekDays);
                    model.addAttribute("teacher", teacherMapper.mapTeacherToTeacherDTO(teacher));
                    model.addAttribute("hours", Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19,20,21));
                    model.addAttribute("error", "studentNotFound");
                    return "closedTeacher/teacherStudents/studentLessons";
                }
            }else{
                Teacher teacher = teacherService.getById(idTeach);

                List<Course> courses = new ArrayList<>();
                teacher.getTeacherCourses().stream().forEach(tc->courses.add(tc.getCourse()));
                List<String> weekDays = (List<String>) lessonService.compileLessonsForStudentAndTeacher(idTeach, stId).get(0);
                HashMap<String, LessonAdminLessonsDTO> lessonHashMap = (HashMap<String, LessonAdminLessonsDTO>) lessonService.compileLessonsForStudentAndTeacher(idTeach, stId).get(1);
                model.addAttribute("lessons", lessonHashMap);
                model.addAttribute("student", studentMapper.mapStudentToStudentDTO(studentService.getById(stId)));
                model.addAttribute("courses", courses.stream().map(el->courseMapper.mapCourseToCourseDTO(el)).collect(Collectors.toList()));
                model.addAttribute("weekDays", weekDays);
                model.addAttribute("teacher", teacherMapper.mapTeacherToTeacherDTO(teacher));
                model.addAttribute("hours", Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19,20,21));
                model.addAttribute("error", "courseNotFound");
                return "closedTeacher/teacherStudents/studentLessons";
            }

        }else{
            Teacher teacher = teacherService.getById(idTeach);
            List<Course> courses = new ArrayList<>();
            teacher.getTeacherCourses().stream().forEach(tc->courses.add(tc.getCourse()));
            List<String> weekDays = (List<String>) lessonService.compileLessonsForStudentAndTeacher(idTeach, stId).get(0);
            HashMap<String, LessonAdminLessonsDTO> lessonHashMap = (HashMap<String, LessonAdminLessonsDTO>) lessonService.compileLessonsForStudentAndTeacher(idTeach, stId).get(1);
            model.addAttribute("lessons", lessonHashMap);
            model.addAttribute("student", studentMapper.mapStudentToStudentDTO(studentService.getById(stId)));
            model.addAttribute("courses", courses.stream().map(el->courseMapper.mapCourseToCourseDTO(el)).collect(Collectors.toList()));
            model.addAttribute("weekDays", weekDays);
            model.addAttribute("teacher", teacherMapper.mapTeacherToTeacherDTO(teacher));
            model.addAttribute("hours", Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19,20,21));
            model.addAttribute("error", "lessonBeforeNow");
            return "closedTeacher/teacherStudents/studentLessons";
        }
    }
    @PostMapping("/{teachId}/student/{stId}/lessons/addCourse/{cId}/{retDate}")
    public String addCourseToStudent(@PathVariable("teachId") int idTeach,  @PathVariable("stId") int stId,@PathVariable("cId") int cId,@PathVariable("retDate") String date, Model model) throws MessagingException {
        int dayOfMonth = Integer.parseInt(date.split(" ")[1].split("\\.")[0]);
        int month = Integer.parseInt(date.split(" ")[1].split("\\.")[1]);
        int hour = Integer.parseInt(date.split(" ")[2]);
        LocalDateTime time = LocalDateTime.now();
        if(time.getMonth().getValue()==12 && month==1){
            time = time.withYear(time.getYear()+1).withMonth(month).withDayOfMonth(dayOfMonth).withHour(hour).withMinute(0).withSecond(0);
        }else{
            time = time.withMonth(month).withDayOfMonth(dayOfMonth).withHour(hour).withMinute(0).withSecond(0);
        }
        boolean hasCourse = false;
        for(TeacherStudentTimeOfTheWeek tsw : tswService.findAllByTeachId(idTeach)){
            if(tsw.getTimeOfTheWeek().getDayOfTheWeek()==time.getDayOfWeek().getValue() && tsw.getTimeOfTheWeek().getTimeOfTheDay()==time.getHour()){
                hasCourse = true;
            }
        }


        if(time.isAfter(LocalDateTime.now())){
            if(cId!=0){
                if(stId!=0){
                    AtomicBoolean exists = new AtomicBoolean(false);
                    Lesson lesson = new Lesson();
                    LocalDateTime checkingTime = time;
                    for(int i = 1; i<=4; i++){
                        for(Lesson les : teacherService.getById(idTeach).getLessons()){
                            if(les.getLessonTime().getMonth().getValue()==checkingTime.getMonth().getValue()&&les.getLessonTime().getDayOfMonth()==checkingTime.getDayOfMonth()&&les.getLessonTime().getHour()==checkingTime.getHour()){
                                exists.set(true);
                            }
                        }
                        checkingTime = checkingTime.plusWeeks(1);
                    }
                    if(exists.get() || hasCourse){
                        Teacher teacher = teacherService.getById(idTeach);
                        List<Course> courses = new ArrayList<>();
                        teacher.getTeacherCourses().stream().forEach(tc->courses.add(tc.getCourse()));
                        List<String> weekDays = (List<String>) lessonService.compileLessonsForStudentAndTeacher(idTeach, stId).get(0);
                        HashMap<String, LessonAdminLessonsDTO> lessonHashMap = (HashMap<String, LessonAdminLessonsDTO>) lessonService.compileLessonsForStudentAndTeacher(idTeach, stId).get(1);
                        model.addAttribute("lessons", lessonHashMap);
                        model.addAttribute("student", studentMapper.mapStudentToStudentDTO(studentService.getById(stId)));
                        model.addAttribute("courses", courses.stream().map(el->courseMapper.mapCourseToCourseDTO(el)).collect(Collectors.toList()));
                        model.addAttribute("weekDays", weekDays);
                        model.addAttribute("teacher", teacherMapper.mapTeacherToTeacherDTO(teacher));
                        model.addAttribute("hours", Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19,20,21));
                        model.addAttribute("error", "timeNotEmpty");
                        return "closedTeacher/teacherStudents/studentLessons";
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
                        return "redirect:/teacher/"+idTeach+"/student/"+stId+"/lessons";
                    }
                }else{
                    Teacher teacher = teacherService.getById(idTeach);
                    List<Course> courses = new ArrayList<>();
                    teacher.getTeacherCourses().stream().forEach(tc->courses.add(tc.getCourse()));
                    List<String> weekDays = (List<String>) lessonService.compileLessonsForStudentAndTeacher(idTeach, stId).get(0);
                    HashMap<String, LessonAdminLessonsDTO> lessonHashMap = (HashMap<String, LessonAdminLessonsDTO>) lessonService.compileLessonsForStudentAndTeacher(idTeach, stId).get(1);
                    model.addAttribute("lessons", lessonHashMap);
                    model.addAttribute("student", studentMapper.mapStudentToStudentDTO(studentService.getById(stId)));
                    model.addAttribute("courses", courses.stream().map(el->courseMapper.mapCourseToCourseDTO(el)).collect(Collectors.toList()));
                    model.addAttribute("weekDays", weekDays);
                    model.addAttribute("teacher", teacherMapper.mapTeacherToTeacherDTO(teacher));
                    model.addAttribute("hours", Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19,20,21));
                    model.addAttribute("error", "studentNotFound");
                    return "closedTeacher/teacherStudents/studentLessons";
                }
            }else{
                Teacher teacher = teacherService.getById(idTeach);

                List<Course> courses = new ArrayList<>();
                teacher.getTeacherCourses().stream().forEach(tc->courses.add(tc.getCourse()));
                List<String> weekDays = (List<String>) lessonService.compileLessonsForStudentAndTeacher(idTeach, stId).get(0);
                HashMap<String, LessonAdminLessonsDTO> lessonHashMap = (HashMap<String, LessonAdminLessonsDTO>) lessonService.compileLessonsForStudentAndTeacher(idTeach, stId).get(1);
                model.addAttribute("lessons", lessonHashMap);
                model.addAttribute("student", studentMapper.mapStudentToStudentDTO(studentService.getById(stId)));
                model.addAttribute("courses", courses.stream().map(el->courseMapper.mapCourseToCourseDTO(el)).collect(Collectors.toList()));
                model.addAttribute("weekDays", weekDays);
                model.addAttribute("teacher", teacherMapper.mapTeacherToTeacherDTO(teacher));
                model.addAttribute("hours", Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19,20,21));
                model.addAttribute("error", "courseNotFound");
                return "closedTeacher/teacherStudents/studentLessons";
            }

        }else{
            Teacher teacher = teacherService.getById(idTeach);
            List<Course> courses = new ArrayList<>();
            teacher.getTeacherCourses().stream().forEach(tc->courses.add(tc.getCourse()));
            List<String> weekDays = (List<String>) lessonService.compileLessonsForStudentAndTeacher(idTeach, stId).get(0);
            HashMap<String, LessonAdminLessonsDTO> lessonHashMap = (HashMap<String, LessonAdminLessonsDTO>) lessonService.compileLessonsForStudentAndTeacher(idTeach, stId).get(1);
            model.addAttribute("lessons", lessonHashMap);
            model.addAttribute("student", studentMapper.mapStudentToStudentDTO(studentService.getById(stId)));
            model.addAttribute("courses", courses.stream().map(el->courseMapper.mapCourseToCourseDTO(el)).collect(Collectors.toList()));
            model.addAttribute("weekDays", weekDays);
            model.addAttribute("teacher", teacherMapper.mapTeacherToTeacherDTO(teacher));
            model.addAttribute("hours", Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19,20,21));
            model.addAttribute("error", "lessonBeforeNow");
            return "closedTeacher/teacherStudents/studentLessons";
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
        mailService.sendEmailWithThymeleafToStudentAboutDeleteLessonExcepted(mail, teacherMapper.mapTeacherToTeacherDTO(teacherService.getById(teacherId)), lessonService.getById(lessonId).getLessonTime().getDayOfMonth()+":"+lessonService.getById(lessonId).getLessonTime().getMonth().getValue(), lessonService.getById(lessonId).getLessonTime().getHour());
        lessonService.deleteById(lessonId);
        return "redirect:/teacher/"+teacherId+"/homepage";
    }
    @GetMapping("/{teacherId}/denyLessonDelete/{lessonId}")
    public String denyLessonDelete(@PathVariable("teacherId") int teacherId, @PathVariable("lessonId") int lessonId, Model model) throws MessagingException {
        System.out.println("DENY");
        Mail mail = new Mail();
        mail.setTo(Collections.singletonList(lessonService.getById(lessonId).getStudent().getEmail()));
        mail.setSubject("Лист про відміну заняття");
        mail.setBody("");
        mailService.sendEmailWithThymeleafToStudentAboutDeleteLessonDenyed(mail, teacherMapper.mapTeacherToTeacherDTO(teacherService.getById(teacherId)), lessonService.getById(lessonId).getLessonTime().getDayOfMonth()+":"+lessonService.getById(lessonId).getLessonTime().getMonth().getValue(), lessonService.getById(lessonId).getLessonTime().getHour());
        return "redirect:/teacher/"+teacherId+"/homepage";
    }
    @PostMapping("/{idTeach}/addMoneyByLessonWithStudent/{lesId}/{stId}")
    public String addMoney(@PathVariable("idTeach") int teachId, @PathVariable("lesId") int lesId,@PathVariable("stId") int stId, Model model){
        Teacher teacher = teacherService.getById(teachId);
        Lesson lesson = lessonService.getById(lesId);
        if(LocalDateTime.now().isAfter(lesson.getLessonTime())){
            float sum = lesson.getDuration()*lesson.getCourse().getTeacherShare();
            teacher.setUnpaidMoney(teacher.getUnpaidMoney()+sum);
            teacherService.update(teachId, teacher);
            lesson.setStatus("was");
            lessonService.update(lesId, lesson);
            return "redirect:/teacher/"+teachId+"/student/"+stId+"/lessons";
        }
        else {
            List<Course> courses = new ArrayList<>();
            teacher.getTeacherCourses().stream().forEach(tc->courses.add(tc.getCourse()));
            List<String> weekDays = (List<String>) lessonService.compileLessonsForStudentAndTeacher(teachId, stId).get(0);
            HashMap<String, LessonAdminLessonsDTO> lessonHashMap = (HashMap<String, LessonAdminLessonsDTO>) lessonService.compileLessonsForStudentAndTeacher(teachId, stId).get(1);
            model.addAttribute("lessons", lessonHashMap);
            model.addAttribute("student", studentMapper.mapStudentToStudentDTO(studentService.getById(stId)));
            model.addAttribute("courses", courses.stream().map(el->courseMapper.mapCourseToCourseDTO(el)).collect(Collectors.toList()));
            model.addAttribute("weekDays", weekDays);
            model.addAttribute("teacher", teacherMapper.mapTeacherToTeacherDTO(teacher));
            model.addAttribute("hours", Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19,20,21));
            model.addAttribute("error", "lessonBeforeNow");
            return "closedTeacher/teacherStudents/studentLessons";
        }
    }
}
