package com.example.demo.controlers.closedAdmin;

import com.example.demo.dto.LessonDTO;
import com.example.demo.dto.adminLessons.LessonAdminLessonsDTO;
import com.example.demo.mapper.LessonMapper;
import com.example.demo.models.*;
import com.example.demo.services.*;
import jakarta.jws.WebParam;
import jakarta.mail.MessagingException;
import jakarta.persistence.Column;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {
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

    public AdminController(TeacherCourseService teacherCourseService, TeacherService teacherService, EmptyTimesForTeacherService emptyTimesForTeacherService, TimeOfTheWeekService theWeekService, CourseService courseService, StudentCourseService studentCourseService, CommentService commentService, LessonService lessonService, StudentService studentService, LessonMapper lessonMapper, TeacherStudentTimeOfTheWeekService tswService, MailService mailService, AdminService adminService) {
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
    }

    @GetMapping("/homepage")
    private String gotoHomepage(Model model) {
        Admin admin = adminService.getAdmin();
        model.addAttribute("admin", admin);
        return "closedAdmin/homepage";
    }

    @GetMapping("/addTeacher")
    private String gotoCreateTeacher(

            Model model) {

        List<Integer> days = List.of(7, 1, 2, 3, 4, 5, 6); // Sunday first
        List<String> dayNames = List.of("Неділя", "Понеділок", "Вівторок", "Середа", "Четвер", "П'ятниця", "Субота");
        List<Integer> hours = new ArrayList<>();
        for (int h = 8; h <= 21; h++) {
            hours.add(h);
        }
        List<TimeOfTheWeek> freeTimes = theWeekService.getAll();
        List<Course> courses = courseService.getAll();
        model.addAttribute("teacher", new Teacher());
        model.addAttribute("days", days);
        model.addAttribute("dayNames", dayNames);
        model.addAttribute("hours", hours);
        model.addAttribute("courses", courses);
        model.addAttribute("freeTimes", freeTimes);


        return "closedAdmin/addNewTeacher";
    }

    @PostMapping("/addTeacher")
    private String addTeacher(
            @ModelAttribute("teacher") Teacher teacher,
            @RequestParam("courseIds") List<Integer> coursesIds,
            @RequestParam("freeTimeIds") List<Integer> freeTimesIds, Model model) {
        List<Course> coursesToReload = courseService.getAll();
        List<TimeOfTheWeek> freeTimesToReload = theWeekService.getAll();
        List<Integer> daysToReload = List.of(7, 1, 2, 3, 4, 5, 6); // Sunday first
        List<String> dayNamesToReload = List.of("Неділя", "Понеділок", "Вівторок", "Середа", "Четвер", "П'ятниця", "Субота");
        List<Integer> hoursToReload = new ArrayList<>();
        for (int h = 8; h <= 21; h++) {
            hoursToReload.add(h);
        }
        try {


            if (teacherService.checkIfExistsByEmail(teacher.getEmail())) {
                model.addAttribute("error", "exists");
                model.addAttribute("days", daysToReload);
                model.addAttribute("dayNames", dayNamesToReload);
                model.addAttribute("hours", hoursToReload);
                model.addAttribute("courses", coursesToReload);
                model.addAttribute("freeTimes", freeTimesToReload);
                return "closedAdmin/addNewTeacher";
            }
            teacher.setApproved(true);
            teacherService.create(teacher);
            List<Course> courses = courseService.getCoursesThroughIds(coursesIds);
            List<TimeOfTheWeek> freeTimes = theWeekService.getTimesOfTheWeekThroughIds(freeTimesIds);

            List<TeacherCourse> teacherCourses = new ArrayList<>();
            for (Course course : courses) {
                TeacherCourse teacherCourse = new TeacherCourse(course, teacher);
                teacherCourseService.create(teacherCourse);
                teacherCourses.add(teacherCourse);
            }
            teacher.setTeacherCourses(teacherCourses);
            List<EmptyTimesForTeacher> freeTimesForTeacher = new ArrayList<>();
            for (TimeOfTheWeek timeOfTheWeek : freeTimes) {
                EmptyTimesForTeacher emptyTimesForTeacher = new EmptyTimesForTeacher(teacher, timeOfTheWeek);
                emptyTimesForTeacherService.create(emptyTimesForTeacher);
                freeTimesForTeacher.add(emptyTimesForTeacher);
            }
            teacher.setEmptyTimesForTeachers(freeTimesForTeacher);

            return "redirect:/admin/homepage";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            model.addAttribute("days", daysToReload);
            model.addAttribute("dayNames", dayNamesToReload);
            model.addAttribute("hours", hoursToReload);
            model.addAttribute("courses", coursesToReload);
            model.addAttribute("freeTimes", freeTimesToReload);
            model.addAttribute("error", "general");
            return "closedAdmin/addNewTeacher";
        }


    }


    @GetMapping("/addStudent")
    private String gotoCreateStudent(Model model) {
        model.addAttribute("student", new Student());
        return "closedAdmin/addNewStudent";
    }

    @PostMapping("/addStudent")
    private String addStudent(@ModelAttribute("student") Student student, Model model) {
        try {
            if (studentService.checkIfExistsByEmail(student.getEmail())) {
                model.addAttribute("error", "exists");
                model.addAttribute("student", student);
                return "closedAdmin/addNewStudent";
            }
            studentService.create(student);
            return "redirect:/admin/homepage";
        } catch (Exception e) {
            model.addAttribute("student");
            model.addAttribute("error", "general");
            return "closedAdmin/addNewStudent";
        }


    }


    @GetMapping("/students")
    public String gotoStudents(Model model) {
        model.addAttribute("students", studentService.getAll());
        return "closedAdmin/students";
    }
    @GetMapping("/teachers")
    public String gotoTeachers(Model model) {
        model.addAttribute("teachers", teacherService.getAll());
        return "closedAdmin/teachers";
    }

    @GetMapping("/student/edit/{id}")
    public String gotoEditStudent(@PathVariable("id") int id, Model model) {


        model.addAttribute("student", studentService.getById(id));
        return "closedAdmin/editStudent";
    }

    @PostMapping("/student/{id}/edit")
    public String editStudent(
            @PathVariable("id") int id,
            @ModelAttribute("student") Student student,
            Model model) {

        try {
            if (studentService.checkIfExistsByEmail(student.getEmail()) && !studentService.getById(id).getEmail().equals(student.getEmail())) {
                model.addAttribute(student);
                model.addAttribute("error", "exists");
                return "closedAdmin/editStudent";
            }


            studentService.update(id, student);
            model.addAttribute("student", studentService.getById(id));
            return "redirect:/admin/students";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            model.addAttribute(student);
            model.addAttribute("error", "general");
            return "closedAdmin/editStudent";
        }

    }

    @PostMapping("/student/delete")

    public String deleteStudent(@RequestParam("studentId") int id) {
        Student student = studentService.getById(id);
        lessonService.deleteAllLessonsByStudent(student);
        studentCourseService.deleteAllCoursesByStudent(student);
        commentService.deleteAllByStudent(student);
        studentService.deleteById(id);
        return "redirect:/admin/students";
    }


    @GetMapping("/student/{idSt}/lessons")
    public String gotoStudentLessons(@PathVariable("idSt") int idSt,Model model){
        List<Teacher> teachers = teacherService.getAll();
        Student student = studentService.getById(idSt);
        List<String> weekDays = (List<String>) lessonService.compileLessonsForStudentInAdmin(student).get(0);
        HashMap<String, LessonAdminLessonsDTO> lessonHashMap = (HashMap<String, LessonAdminLessonsDTO>) lessonService.compileLessonsForStudentInAdmin(student).get(1);
        List<Course> courses = courseService.getAll();
        model.addAttribute("lessons", lessonHashMap);
        model.addAttribute("courses", courses);
        model.addAttribute("weekDays", weekDays);
        model.addAttribute("teachers", teachers);
        model.addAttribute("student", student);
        model.addAttribute("hours", Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19,20,21));
        return "closedAdmin/studentLessons";
    }
    @PostMapping("/student/{idSt}/lessons/deleteLesson/{id}")
    @Transactional
    public String deleteLesson(@PathVariable("idSt") int idSt, @PathVariable("id") int idLes, Model model) throws MessagingException {

        Mail mail = new Mail();
        mail.setTo(Collections.singletonList((lessonService.getById(idLes).getTeacher().getEmail())));
        mail.setSubject("Лист про відміну заняття");
        mail.setBody("");
        Student student = studentService.getById(idSt);
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
        Student student = studentService.getById(idSt);
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
                Student student = studentService.getById(idSt);
                mailService.sendEmailWithThymeleafToTeacherAboutLessonTimeEdited(mail, student, lesson.getLessonTime().getDayOfMonth()+":"+lesson.getLessonTime().getMonth(),lesson.getLessonTime().getHour(), newDate.getDayOfMonth()+":"+newDate.getMonth(),newDate.getHour());
                lesson.setLessonTime(newDate);
                lessonService.update(lesson.getId(), lesson);
                return "redirect:/admin/student/"+idSt+"/lessons";
            }else{
                List<Teacher> teachers = teacherService.getAll();
                Student student = studentService.getById(idSt);
                List<String> weekDays = (List<String>) lessonService.compileLessonsForStudentInAdmin(student).get(0);
                HashMap<String, LessonAdminLessonsDTO> lessonHashMap = (HashMap<String, LessonAdminLessonsDTO>) lessonService.compileLessonsForStudentInAdmin(student).get(1);
                List<Course> courses = courseService.getAll();
                model.addAttribute("lessons", lessonHashMap);
                model.addAttribute("courses", courses);
                model.addAttribute("weekDays", weekDays);
                model.addAttribute("teachers", teachers);
                model.addAttribute("student", student);
                model.addAttribute("hours", Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19,20,21));
                return "closedAdmin/studentLessons";
            }
    }


    @GetMapping("/teacher/{id}/lessons")
    public String gotoLessons(@PathVariable("id") int id, Model model){
        Teacher teacher = teacherService.getById(id);
        List<String> weekDays = (List<String>) lessonService.compileLessonsForTeacher(teacher).get(0);
        HashMap<String, LessonAdminLessonsDTO> lessonHashMap = (HashMap<String, LessonAdminLessonsDTO>) lessonService.compileLessonsForTeacher(teacher).get(1);
        List<Course> courses = new ArrayList<>();
        teacher.getTeacherCourses().stream().forEach(course->courses.add(course.getCourse()));

        model.addAttribute("lessons", lessonHashMap);
        model.addAttribute("students", studentService.getAll());
        model.addAttribute("courses", courses);
        model.addAttribute("weekDays", weekDays);
        model.addAttribute("teacher", teacher);
        model.addAttribute("hours", Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19,20,21));
        return "closedTeacher/lessons";
    }



    @PostMapping("/teacher/{idTeach}/lessons/deleteLesson/{id}")
    @Transactional
    public String deleteLesson(@PathVariable("idTeach") int idTeach, @PathVariable("id") int idLes) throws MessagingException {
        Mail mail = new Mail();
        mail.setTo(Collections.singletonList(lessonService.getById(idLes).getStudent().getEmail()));
        mail.setSubject("Лист про відміну заняття");
        mail.setBody("");
        Teacher teacher = teacherService.getById(idTeach);
        mailService.sendEmailWithThymeleafToStudentAboutLessonRemoved(mail, teacher, lessonService.getById(idLes).getLessonTime().getDayOfMonth()+":"+lessonService.getById(idLes).getLessonTime().getMonth(), lessonService.getById(idLes).getLessonTime().getHour());
        lessonService.deleteById(idLes);
        return "redirect:/teacher/"+idTeach+"/lessons";
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
        mailService.sendEmailWithThymeleafToStudentAboutCourseRemoved(mail, teacher, lesson.getLessonTime().getDayOfMonth()+":"+lesson.getLessonTime().getMonth(), lesson.getLessonTime().getHour());
        lessonService.deleteAllByStIdAndTeachIdAndTswIdAndLesTimeAfterNow(lesson.getStudent().getId(), idTeach, lesson.getTimeOfTheWeek().getId(), LocalDateTime.now().minusMinutes(30));
        tswService.removeAllByStIdAndTeachIdAndTswId(lesson.getStudent().getId(), idTeach, lesson.getTimeOfTheWeek().getId());
        return "redirect:/admin/teacher/"+idTeach+"/lessons";
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
            mailService.sendEmailWithThymeleafToStudentAboutLessonTimeEdited(mail, teacher, lesson.getLessonTime().getDayOfMonth()+":"+lesson.getLessonTime().getMonth(),lesson.getLessonTime().getHour(), newDate.getDayOfMonth()+":"+newDate.getMonth(),newDate.getHour());
            lesson.setLessonTime(newDate);
            lessonService.update(lesson.getId(), lesson);
            return "redirect:/admin/teacher/"+idTeach+"/lessons";
        }else{
            Teacher teacher = teacherService.getById(idTeach);
            List<String> weekDays = (List<String>) lessonService.compileLessonsForTeacher(teacher).get(0);
            HashMap<String, LessonAdminLessonsDTO> lessonHashMap = (HashMap<String, LessonAdminLessonsDTO>) lessonService.compileLessonsForTeacher(teacher).get(1);
            model.addAttribute("lessons", lessonHashMap);
            model.addAttribute("weekDays", weekDays);
            model.addAttribute("teacher", teacher);
            model.addAttribute("hours", Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19,20,21));
            model.addAttribute("error", "dateInconsistency");
            return "closedAdmin/teacherLessons";
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
                    mailService.sendEmailWithThymeleafToStudentAboutLessonAdded(mail, teacher, lesson.getLessonTime().getDayOfMonth()+":"+lesson.getLessonTime().getMonth(),lesson.getLessonTime().getHour());
                    return "redirect:/admin/teacher/"+idTeach+"/lessons";
                }else{
                    Teacher teacher = teacherService.getById(idTeach);
                    List<Course> courses = new ArrayList<>();
                    teacher.getTeacherCourses().stream().forEach(tc->courses.add(tc.getCourse()));
                    List<String> weekDays = (List<String>) lessonService.compileLessonsForTeacher(teacher).get(0);
                    HashMap<String, LessonAdminLessonsDTO> lessonHashMap = (HashMap<String, LessonAdminLessonsDTO>) lessonService.compileLessonsForTeacher(teacher).get(1);
                    model.addAttribute("lessons", lessonHashMap);
                    model.addAttribute("students", studentService.getAll());
                    model.addAttribute("courses", courses);
                    model.addAttribute("weekDays", weekDays);
                    model.addAttribute("teacher", teacher);
                    model.addAttribute("hours", Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19,20,21));
                    model.addAttribute("error", "studentNotFound");
                    return "closedAdmin/teacherLessons";
                }
            }else{
                Teacher teacher = teacherService.getById(idTeach);
                List<String> weekDays = (List<String>) lessonService.compileLessonsForTeacher(teacher).get(0);
                List<Course> courses = new ArrayList<>();
                teacher.getTeacherCourses().stream().forEach(tc->courses.add(tc.getCourse()));
                HashMap<String, LessonAdminLessonsDTO> lessonHashMap = (HashMap<String, LessonAdminLessonsDTO>) lessonService.compileLessonsForTeacher(teacher).get(1);
                model.addAttribute("lessons", lessonHashMap);
                model.addAttribute("students", studentService.getAll());
                model.addAttribute("courses", courses);
                model.addAttribute("weekDays", weekDays);
                model.addAttribute("teacher", teacher);
                model.addAttribute("hours", Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19,20,21));
                model.addAttribute("error", "courseNotFound");
                return "closedAdmin/teacherLessons";
            }

        }else{
            Teacher teacher = teacherService.getById(idTeach);
            List<Course> courses = new ArrayList<>();
            teacher.getTeacherCourses().stream().forEach(tc->courses.add(tc.getCourse()));
            List<String> weekDays = (List<String>) lessonService.compileLessonsForTeacher(teacher).get(0);
            HashMap<String, LessonAdminLessonsDTO> lessonHashMap = (HashMap<String, LessonAdminLessonsDTO>) lessonService.compileLessonsForTeacher(teacher).get(1);
            model.addAttribute("lessons", lessonHashMap);
            model.addAttribute("students", studentService.getAll());
            model.addAttribute("courses", courses);
            model.addAttribute("weekDays", weekDays);
            model.addAttribute("teacher", teacher);
            model.addAttribute("hours", Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19,20,21));
            model.addAttribute("error", "lessonBeforeNow");
            return "closedAdmin/teacherLessons";
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
                    for(int i = 1; i<=4; i++){
                        for(Lesson les : teacherService.getById(idTeach).getLessons()){
                            if(les.getLessonTime().getMonth().getValue()==checkingTime.getMonth().getValue()&&les.getLessonTime().getDayOfMonth()==checkingTime.getDayOfMonth()&&les.getLessonTime().getHour()==checkingTime.getHour()){
                                exists.set(true);
                            }
                        }
                        checkingTime = checkingTime.plusWeeks(1);
                    }
                    if(exists.get()){
                        Teacher teacher = teacherService.getById(idTeach);
                        List<Course> courses = new ArrayList<>();
                        teacher.getTeacherCourses().stream().forEach(tc->courses.add(tc.getCourse()));
                        List<String> weekDays = (List<String>) lessonService.compileLessonsForTeacher(teacher).get(0);
                        HashMap<String, LessonAdminLessonsDTO> lessonHashMap = (HashMap<String, LessonAdminLessonsDTO>) lessonService.compileLessonsForTeacher(teacher).get(1);
                        model.addAttribute("lessons", lessonHashMap);
                        model.addAttribute("students", studentService.getAll());
                        model.addAttribute("courses", courses);
                        model.addAttribute("weekDays", weekDays);
                        model.addAttribute("teacher", teacher);
                        model.addAttribute("hours", Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19,20,21));
                        model.addAttribute("error", "timeNotEmpty");
                        return "closedAdmin/teacherLessons";
                    }else {
                        for(int i = 1; i<=4; i++){
                            lesson = new Lesson(studentService.getById(stId), teacherService.getById(idTeach), courseService.getById(cId), time, 1, theWeekService.findByDayOfTheWeekAndTimeOfTheDay(time.getDayOfWeek().getValue(),time.getHour()), "will");
                            lessonService.create(lesson);

                            time = time.plusWeeks(1);
                        }
                        TeacherStudentTimeOfTheWeek tsw = new TeacherStudentTimeOfTheWeek(teacherService.getById(idTeach), studentService.getById(stId), theWeekService.findByDayOfTheWeekAndTimeOfTheDay(time.getDayOfWeek().getValue(),time.getHour()));
                        tswService.create(tsw);

                        Mail mail = new Mail();
                        mail.setTo(Collections.singletonList(studentService.getById(stId).getEmail()));
                        mail.setSubject("Лист про додання курсу занять");
                        mail.setBody("");
                        Teacher teacher = teacherService.getById(idTeach);
                        mailService.sendEmailWithThymeleafToStudentAboutCourseAdded(mail, teacher, String.valueOf(lesson.getLessonTime().getDayOfWeek()),lesson.getLessonTime().getHour());
                        return "redirect:/admin/teacher/"+idTeach+"/lessons";
                    }
                }else{
                    Teacher teacher = teacherService.getById(idTeach);
                    List<Course> courses = new ArrayList<>();
                    teacher.getTeacherCourses().stream().forEach(tc->courses.add(tc.getCourse()));
                    List<String> weekDays = (List<String>) lessonService.compileLessonsForTeacher(teacher).get(0);
                    HashMap<String, LessonAdminLessonsDTO> lessonHashMap = (HashMap<String, LessonAdminLessonsDTO>) lessonService.compileLessonsForTeacher(teacher).get(1);
                    model.addAttribute("lessons", lessonHashMap);
                    model.addAttribute("students", studentService.getAll());
                    model.addAttribute("courses", courses);
                    model.addAttribute("weekDays", weekDays);
                    model.addAttribute("teacher", teacher);
                    model.addAttribute("hours", Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19,20,21));
                    model.addAttribute("error", "studentNotFound");
                    return "closedAdmin/teacherLessons";
                }
            }else{
                Teacher teacher = teacherService.getById(idTeach);
                List<String> weekDays = (List<String>) lessonService.compileLessonsForTeacher(teacher).get(0);
                List<Course> courses = new ArrayList<>();
                teacher.getTeacherCourses().stream().forEach(tc->courses.add(tc.getCourse()));
                HashMap<String, LessonAdminLessonsDTO> lessonHashMap = (HashMap<String, LessonAdminLessonsDTO>) lessonService.compileLessonsForTeacher(teacher).get(1);
                model.addAttribute("lessons", lessonHashMap);
                model.addAttribute("students", studentService.getAll());
                model.addAttribute("courses", courses);
                model.addAttribute("weekDays", weekDays);
                model.addAttribute("teacher", teacher);
                model.addAttribute("hours", Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19,20,21));
                model.addAttribute("error", "courseNotFound");
                return "closedAdmin/teacherLessons";
            }

        }else{
            Teacher teacher = teacherService.getById(idTeach);
            List<Course> courses = new ArrayList<>();
            teacher.getTeacherCourses().stream().forEach(tc->courses.add(tc.getCourse()));
            List<String> weekDays = (List<String>) lessonService.compileLessonsForTeacher(teacher).get(0);
            HashMap<String, LessonAdminLessonsDTO> lessonHashMap = (HashMap<String, LessonAdminLessonsDTO>) lessonService.compileLessonsForTeacher(teacher).get(1);
            model.addAttribute("lessons", lessonHashMap);
            model.addAttribute("students", studentService.getAll());
            model.addAttribute("courses", courses);
            model.addAttribute("weekDays", weekDays);
            model.addAttribute("teacher", teacher);
            model.addAttribute("hours", Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19,20,21));
            model.addAttribute("error", "lessonBeforeNow");
            return "closedAdmin/teacherLessons";
        }
    }


    @GetMapping("/teacher/{idTeach}/edit")
    public String gotoEditTeacher(@PathVariable("idTeach") int id, Model model){
        Teacher teacher = teacherService.getById(id);
        teacher.setPassword("");
        model.addAttribute("teacher", teacher);
        List<TimeOfTheWeek> allfreeTimes = theWeekService.getAll();
        List<Integer> teacherFreeTimes = new ArrayList<>();
        List<Course> allCourses = new ArrayList<>();
        List<Integer> days = List.of(7, 1, 2, 3, 4, 5, 6); // Sunday first
        List<String> dayNames = List.of("Неділя", "Понеділок", "Вівторок", "Середа", "Четвер", "П'ятниця", "Субота");

        ///////////////
        try{
            teacher.getEmptyTimesForTeachers().stream().forEach(time->teacherFreeTimes.add(time.getTime().getId()));
            allCourses = courseService.getAll();

        }catch (Exception e){
            System.out.println(e.getMessage());

        }

        model.addAttribute("freeTimes", allfreeTimes);
        model.addAttribute("teacherFreeTimes", teacherFreeTimes);
        model.addAttribute("allCourses", allCourses);
        model.addAttribute("hours", Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19,20,21));
        model.addAttribute("dayNames", dayNames);
        model.addAttribute("days", days);
        //////
        return "closedAdmin/editTeacher";
    }
    @PostMapping("/teacher/{id}/edit")
    @Transactional
    public String updateTeacher(@PathVariable("id") int id,
                                @ModelAttribute("teacher") Teacher teacher,
                                @RequestParam(value = "freeTimeIds", required = false) List<Integer> freeTimeIds,
                                @RequestParam(value = "days", required = false) List<Integer> days,
                                @RequestParam(value = "dayNames", required = false) List<String> dayNames,
                                @RequestParam(value = "newCourseIds", required = false) List<Integer> newCourseIds,
                                @RequestParam(value = "freeTimes", required = false) List<TimeOfTheWeek> allfreeTimes,
                                @RequestParam(value = "teacherFreeTimes", required = false) List<EmptyTimesForTeacher> teacherFreeTimes,
                                @RequestParam(value = "allCourses", required = false) List<Course> allCourses,
                                Model model) throws MessagingException {
        if(teacherService.checkIfExistsByEmail(teacher.getEmail()) && !teacherService.getById(teacher.getId()).getEmail().equals(teacher.getEmail())){
            model.addAttribute("freeTimes", allfreeTimes);
            model.addAttribute("teacherFreeTimes", teacherFreeTimes);
            model.addAttribute("allCourses", allCourses);
            model.addAttribute("dayNames", dayNames);
            model.addAttribute("days", days);
            model.addAttribute("hours", Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19,20,21));
            model.addAttribute("error", "exists");
            return "closedAdmin/editTeacher";
        }
        else{
            if(newCourseIds==null || newCourseIds.isEmpty()){
                ///
            }else{
                for (int cId : newCourseIds){
                    teacherCourseService.create(new TeacherCourse(courseService.getById(cId), teacher));
                }
            }






            //////
            if(teacher.getPassword().isEmpty()){
                teacher.setPassword(teacherService.getById(teacher.getId()).getPassword());
            }
            List<Integer> oldTimeIds = new ArrayList<>();

            emptyTimesForTeacherService.findAllByTeachId(id).stream().forEach(time->oldTimeIds.add(time.getTime().getId()));
            if(!oldTimeIds.equals(freeTimeIds)){
                emptyTimesForTeacherService.removeAllByTeacherId(teacher.getId());
                for(int iD : freeTimeIds){
                    oldTimeIds.remove(Integer.valueOf(iD));
                    emptyTimesForTeacherService.create(new EmptyTimesForTeacher(teacher, theWeekService.getById(iD)));
                }
                for(int oldId : oldTimeIds){
                    List<Lesson> lessons = lessonService.findAllByTimeOfTheWeekIdAndTeacherId(oldId, teacher.getId());

                    for(Lesson l : lessons){
                        if(l.getLessonTime().isAfter(LocalDateTime.now())){
                            lessonService.deleteById(l.getId());
                        }
                    }
                    if(!lessons.isEmpty()){
                        Mail mail = new Mail();
                        mail.setTo(Collections.singletonList(lessons.get(0).getStudent().getEmail()));
                        mail.setSubject("Лист про відміну заняття");
                        mail.setBody("");
                        Teacher saved = teacher;
                        mailService.sendEmailWithThymeleafToStudentAboutCourseRemoved(mail, saved, String.valueOf(lessons.get(0).getLessonTime().getDayOfWeek()), lessons.get(0).getLessonTime().getHour());
                    }


                }

            }
            teacherService.update(teacher.getId(), teacher);




        }
        return "redirect:/admin/teacher/"+teacher.getId()+"/edit";
    }
    @PostMapping("/teacher/{id}/delete")
    public String deleteTeacher(@PathVariable("id") int id, Model model){
        Teacher teacher = teacherService.getById(id);
        for(Lesson lesson : teacher.getLessons()){
            if(lesson.getLessonTime().isAfter(LocalDateTime.now())){
                lessonService.deleteById(lesson.getId());
            }
        }
        for(EmptyTimesForTeacher empt : teacher.getEmptyTimesForTeachers()){
            emptyTimesForTeacherService.deleteById(empt.getId());
        }
        for(TeacherCourse tc : teacher.getTeacherCourses()){
            teacherCourseService.deleteById(tc.getId());
        }
        for(TeacherStudentTimeOfTheWeek tsw : tswService.findAllByTeachId(teacher.getId())){
            tswService.deleteById(tsw.getId());
        }
        teacherService.deleteById(teacher.getId());
        return "redirect:/admin/teachers";
    }
    @GetMapping("/teacher/{teachId}/info")
    public String gotoInfo(@PathVariable("teachId") int id, Model model){
        Teacher teacher = teacherService.getById(id);
        teacher.setPassword("");
        model.addAttribute("teacher", teacher);
        List<TimeOfTheWeek> allfreeTimes = theWeekService.getAll();
        List<Integer> teacherFreeTimes = new ArrayList<>();
        List<Course> allCourses = new ArrayList<>();
        List<Integer> days = List.of(7, 1, 2, 3, 4, 5, 6); // Sunday first
        List<String> dayNames = List.of("Неділя", "Понеділок", "Вівторок", "Середа", "Четвер", "П'ятниця", "Субота");

        ///////////////
        try{
            teacher.getEmptyTimesForTeachers().stream().forEach(time->teacherFreeTimes.add(time.getTime().getId()));
            allCourses = courseService.getAll();

        }catch (Exception e){
            System.out.println(e.getMessage());

        }

        model.addAttribute("freeTimes", allfreeTimes);
        model.addAttribute("students", studentService.getAll());
        model.addAttribute("teacherFreeTimes", teacherFreeTimes);
        model.addAttribute("allCourses", allCourses);
        model.addAttribute("hours", Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19,20,21));
        model.addAttribute("dayNames", dayNames);
        model.addAttribute("days", days);
        //////
        return "closedAdmin/teacherInfo";
    }

    @GetMapping("/teacher/{teachId}/lessonsWithStudent/{stId}")
    public String gotoStudentTeacherLessons(@PathVariable("teachId") int idT, @PathVariable("stId") int idSt,Model model){
        Teacher teacher = teacherService.getById(idT);
        Student student = studentService.getById(idSt);
        List<String> weekDays = (List<String>) lessonService.compileLessonsForStudentAndTeacher(idT, idSt).get(0);
        HashMap<String, LessonAdminLessonsDTO> lessonHashMap = (HashMap<String, LessonAdminLessonsDTO>) lessonService.compileLessonsForStudentAndTeacher(idT, idSt).get(1);
        List<Course> courses = new ArrayList<>();
        teacher.getTeacherCourses().stream().forEach(course->courses.add(course.getCourse()));


        model.addAttribute("lessons", lessonHashMap);
        model.addAttribute("courses", courses);
        model.addAttribute("weekDays", weekDays);
        model.addAttribute("teacher", teacher);
        model.addAttribute("student", student);
        model.addAttribute("hours", Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19,20,21));
        return "closedAdmin/teacherStudentLessons";
    }








    @PostMapping("/teacher/{teachId}/lessonsWithStudent/deleteLesson/{id}")
    @Transactional
    public String deleteLessonForStudent(@PathVariable("teachId") int idTeach, @PathVariable("id") int idLes) throws MessagingException {
        Lesson lesson = lessonService.getById(idLes);
        System.out.println(lesson);
        Mail mail = new Mail();
        mail.setTo(Collections.singletonList(lessonService.getById(idLes).getStudent().getEmail()));
        mail.setSubject("Лист про відміну заняття");
        mail.setBody("");
        Teacher teacher = teacherService.getById(idTeach);
        mailService.sendEmailWithThymeleafToStudentAboutLessonRemoved(mail, teacher, lessonService.getById(idLes).getLessonTime().getDayOfMonth()+":"+lessonService.getById(idLes).getLessonTime().getMonth(), lessonService.getById(idLes).getLessonTime().getHour());
        lessonService.deleteById(idLes);
        return "redirect:/admin/teacher/"+idTeach+"/lessonsWithStudent/"+lesson.getStudent().getId();
    }

    @PostMapping("/teacher/{idTeach}/lessonsWithStudent/deleteCourse/{id}")
    @Transactional
    public String deleteCourseForStudent(@PathVariable("idTeach") int idTeach, @PathVariable("id") int idLes) throws MessagingException {
        Lesson lesson = lessonService.getById(idLes);
        Mail mail = new Mail();
        mail.setTo(Collections.singletonList(lesson.getStudent().getEmail()));
        mail.setSubject("Лист про відміну заняття");
        mail.setBody("");
        Teacher teacher = teacherService.getById(idTeach);
        mailService.sendEmailWithThymeleafToStudentAboutCourseRemoved(mail, teacher, lesson.getLessonTime().getDayOfMonth()+":"+lesson.getLessonTime().getMonth(), lesson.getLessonTime().getHour());
        lessonService.deleteAllByStIdAndTeachIdAndTswIdAndLesTimeAfterNow(lesson.getStudent().getId(), idTeach, lesson.getTimeOfTheWeek().getId(), LocalDateTime.now().minusMinutes(30));
        tswService.removeAllByStIdAndTeachIdAndTswId(lesson.getStudent().getId(), idTeach, lesson.getTimeOfTheWeek().getId());
        return "redirect:/admin/teacher/"+idTeach+"/lessonsWithStudent/"+lesson.getStudent().getId();
    }
    @PostMapping("/teacher/{idTeach}/lessonsWithStudent/editLesson/{id}/{nTId}/{date}")
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
            mailService.sendEmailWithThymeleafToStudentAboutLessonTimeEdited(mail, teacher, lesson.getLessonTime().getDayOfMonth()+":"+lesson.getLessonTime().getMonth(),lesson.getLessonTime().getHour(), newDate.getDayOfMonth()+":"+newDate.getMonth(),newDate.getHour());
            lesson.setLessonTime(newDate);
            lessonService.update(lesson.getId(), lesson);
            return "redirect:/admin/teacher/"+idTeach+"/lessonsWithStudent/"+lesson.getStudent().getId();
        }else{
            Teacher teacher = teacherService.getById(idTeach);
            List<String> weekDays = (List<String>) lessonService.compileLessonsForStudentAndTeacher(idTeach, lesson.getStudent().getId()).get(0);
            HashMap<String, LessonAdminLessonsDTO> lessonHashMap = (HashMap<String, LessonAdminLessonsDTO>) lessonService.compileLessonsForStudentAndTeacher(idTeach, lesson.getStudent().getId()).get(1);
            model.addAttribute("lessons", lessonHashMap);
            model.addAttribute("weekDays", weekDays);
            model.addAttribute("teacher", teacher);
            model.addAttribute("hours", Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19,20,21));
            model.addAttribute("error", "dateInconsistency");
            model.addAttribute("student", lesson.getStudent());
            return "closedAdmin/teacherStudentLessons";
        }

    }
    @PostMapping("/teacher/{teachId}/lessonsWithStudent/{stId}/addLesson/{cId}/{retDate}")
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
                    mailService.sendEmailWithThymeleafToStudentAboutLessonAdded(mail, teacher, lesson.getLessonTime().getDayOfMonth()+":"+lesson.getLessonTime().getMonth(),lesson.getLessonTime().getHour());
                    return "redirect:/admin/teacher/"+idTeach+"/lessonsWithStudent/"+stId;
                }else{
                    Teacher teacher = teacherService.getById(idTeach);
                    List<Course> courses = new ArrayList<>();
                    teacher.getTeacherCourses().stream().forEach(tc->courses.add(tc.getCourse()));
                    List<String> weekDays = (List<String>) lessonService.compileLessonsForStudentAndTeacher(idTeach, stId).get(0);
                    HashMap<String, LessonAdminLessonsDTO> lessonHashMap = (HashMap<String, LessonAdminLessonsDTO>) lessonService.compileLessonsForStudentAndTeacher(idTeach, stId).get(1);
                    model.addAttribute("lessons", lessonHashMap);
                    model.addAttribute("student", studentService.getById(stId));
                    model.addAttribute("courses", courses);
                    model.addAttribute("weekDays", weekDays);
                    model.addAttribute("teacher", teacher);
                    model.addAttribute("hours", Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19,20,21));
                    model.addAttribute("error", "studentNotFound");
                    return "closedAdmin/teacherStudentLessons";
                }
            }else{
                Teacher teacher = teacherService.getById(idTeach);

                List<Course> courses = new ArrayList<>();
                teacher.getTeacherCourses().stream().forEach(tc->courses.add(tc.getCourse()));
                List<String> weekDays = (List<String>) lessonService.compileLessonsForStudentAndTeacher(idTeach, stId).get(0);
                HashMap<String, LessonAdminLessonsDTO> lessonHashMap = (HashMap<String, LessonAdminLessonsDTO>) lessonService.compileLessonsForStudentAndTeacher(idTeach, stId).get(1);
                model.addAttribute("lessons", lessonHashMap);
                model.addAttribute("student", studentService.getById(stId));
                model.addAttribute("courses", courses);
                model.addAttribute("weekDays", weekDays);
                model.addAttribute("teacher", teacher);
                model.addAttribute("hours", Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19,20,21));
                model.addAttribute("error", "courseNotFound");
                return "closedAdmin/teacherStudentLessons";
            }

        }else{
            Teacher teacher = teacherService.getById(idTeach);
            List<Course> courses = new ArrayList<>();
            teacher.getTeacherCourses().stream().forEach(tc->courses.add(tc.getCourse()));
            List<String> weekDays = (List<String>) lessonService.compileLessonsForStudentAndTeacher(idTeach, stId).get(0);
            HashMap<String, LessonAdminLessonsDTO> lessonHashMap = (HashMap<String, LessonAdminLessonsDTO>) lessonService.compileLessonsForStudentAndTeacher(idTeach, stId).get(1);
            model.addAttribute("lessons", lessonHashMap);
            model.addAttribute("student", studentService.getById(stId));
            model.addAttribute("courses", courses);
            model.addAttribute("weekDays", weekDays);
            model.addAttribute("teacher", teacher);
            model.addAttribute("hours", Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19,20,21));
            model.addAttribute("error", "lessonBeforeNow");
            return "closedAdmin/teacherStudentLessons";
        }
    }
    @PostMapping("/teacher/{teachId}/lessonsWithStudent/{stId}/addCourse/{cId}/{retDate}")
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
                        model.addAttribute("student", studentService.getById(stId));
                        model.addAttribute("courses", courses);
                        model.addAttribute("weekDays", weekDays);
                        model.addAttribute("teacher", teacher);
                        model.addAttribute("hours", Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19,20,21));
                        model.addAttribute("error", "timeNotEmpty");
                        return "closedAdmin/teacherStudentLessons";
                    }else {
                        for(int i = 1; i<=4; i++){
                            lesson = new Lesson(studentService.getById(stId), teacherService.getById(idTeach), courseService.getById(cId), time, 1, theWeekService.findByDayOfTheWeekAndTimeOfTheDay(time.getDayOfWeek().getValue(),time.getHour()), "will");
                            lessonService.create(lesson);

                            time = time.plusWeeks(1);
                        }
                        TeacherStudentTimeOfTheWeek tsw = new TeacherStudentTimeOfTheWeek(teacherService.getById(idTeach), studentService.getById(stId), theWeekService.findByDayOfTheWeekAndTimeOfTheDay(time.getDayOfWeek().getValue(),time.getHour()));
                        tswService.create(tsw);

                        Mail mail = new Mail();
                        mail.setTo(Collections.singletonList(studentService.getById(stId).getEmail()));
                        mail.setSubject("Лист про додання курсу занять");
                        mail.setBody("");
                        Teacher teacher = teacherService.getById(idTeach);
                        mailService.sendEmailWithThymeleafToStudentAboutCourseAdded(mail, teacher, String.valueOf(lesson.getLessonTime().getDayOfWeek()),lesson.getLessonTime().getHour());
                        return "redirect:/admin/teacher/"+idTeach+"/lessonsWithStudent/"+stId;
                    }
                }else{
                    Teacher teacher = teacherService.getById(idTeach);
                    List<Course> courses = new ArrayList<>();
                    teacher.getTeacherCourses().stream().forEach(tc->courses.add(tc.getCourse()));
                    List<String> weekDays = (List<String>) lessonService.compileLessonsForStudentAndTeacher(idTeach, stId).get(0);
                    HashMap<String, LessonAdminLessonsDTO> lessonHashMap = (HashMap<String, LessonAdminLessonsDTO>) lessonService.compileLessonsForStudentAndTeacher(idTeach, stId).get(1);
                    model.addAttribute("lessons", lessonHashMap);
                    model.addAttribute("student", studentService.getById(stId));
                    model.addAttribute("courses", courses);
                    model.addAttribute("weekDays", weekDays);
                    model.addAttribute("teacher", teacher);
                    model.addAttribute("hours", Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19,20,21));
                    model.addAttribute("error", "studentNotFound");
                    return "closedAdmin/teacherStudentLessons";
                }
            }else{
                Teacher teacher = teacherService.getById(idTeach);
                List<Course> courses = new ArrayList<>();
                List<String> weekDays = (List<String>) lessonService.compileLessonsForStudentAndTeacher(idTeach, stId).get(0);
                HashMap<String, LessonAdminLessonsDTO> lessonHashMap = (HashMap<String, LessonAdminLessonsDTO>) lessonService.compileLessonsForStudentAndTeacher(idTeach, stId).get(1);
                model.addAttribute("lessons", lessonHashMap);
                model.addAttribute("student", studentService.getById(stId));
                model.addAttribute("courses", courses);
                model.addAttribute("weekDays", weekDays);
                model.addAttribute("teacher", teacher);
                model.addAttribute("hours", Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19,20,21));
                model.addAttribute("error", "courseNotFound");
                return "closedAdmin/teacherStudentLessons";
            }

        }else{
            Teacher teacher = teacherService.getById(idTeach);
            List<Course> courses = new ArrayList<>();
            teacher.getTeacherCourses().stream().forEach(tc->courses.add(tc.getCourse()));
            List<String> weekDays = (List<String>) lessonService.compileLessonsForStudentAndTeacher(idTeach, stId).get(0);
            HashMap<String, LessonAdminLessonsDTO> lessonHashMap = (HashMap<String, LessonAdminLessonsDTO>) lessonService.compileLessonsForStudentAndTeacher(idTeach, stId).get(1);
            model.addAttribute("lessons", lessonHashMap);
            model.addAttribute("student", studentService.getById(stId));
            model.addAttribute("courses", courses);
            model.addAttribute("weekDays", weekDays);
            model.addAttribute("teacher", teacher);
            model.addAttribute("hours", Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19,20,21));
            model.addAttribute("error", "lessonBeforeNow");
            return "closedAdmin/teacherStudentLessons";
        }
    }



}
