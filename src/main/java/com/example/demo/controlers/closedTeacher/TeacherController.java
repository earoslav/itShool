package com.example.demo.controlers.closedTeacher;

import com.example.demo.dto.adminLessons.LessonAdminLessonsDTO;
import com.example.demo.mapper.LessonMapper;
import com.example.demo.models.*;
import com.example.demo.services.*;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Controller
@RequestMapping("/teacher")
public class TeacherController {
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

    public TeacherController(TeacherService teacherService, LessonMapper lessonMapper, TimeOfTheWeekService theWeekService, CourseService courseService, EmptyTimesForTeacherService emptyTimesForTeacherService, LessonService lessonService, TeacherCourseService teacherCourseService, MailService mailService, TeacherStudentTimeOfTheWeekService tswService, StudentService studentService) {
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
    }

    @GetMapping("/{idTeach}/homepage")
    public String gotoTeacher(@PathVariable("idTeach") int id, Model model){
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
        return "closedTeacher/homepage";
    }
    @PostMapping("/{id}/homepage")
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
            return "closedTeacher/homepage";
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
        return "redirect:/teacher/"+teacher.getId()+"/homepage";
    }
    @GetMapping("/{idTeach}/students")
    public String gotoMyStudents(@PathVariable("idTeach") int id, Model model){
        HashSet<Student> students = new HashSet<>();
        HashSet<Student> finalStudents = students;
        tswService.findAllByTeachId(id).stream().forEach(obj-> finalStudents.add(obj.getStudent()));
        List<Student> retStudents = finalStudents.stream().sorted(Comparator.comparing(Student::getId)).toList();
        model.addAttribute("students", retStudents);
        model.addAttribute("teacher", teacherService.getById(id));
        return "closedTeacher/students";
    }

    @PostMapping("/{idTeach}/student/{idSt}/delete")
    @Transactional
    public String deleteStudent(@PathVariable("idTeach") int idT, @PathVariable("idSt") int idSt, Model model) throws MessagingException {
        List<Lesson> lessons = lessonService.findAllByIdTandIdSt(idT, idSt);
        lessons.stream().filter(lesson -> lesson.getLessonTime().isAfter(LocalDateTime.now())).forEach(lesson -> lessonService.deleteById(lesson.getId()));
        tswService.removeAllByTidAndSid(idT, idSt);
        Mail mail = new Mail();
        mail.setTo(Collections.singletonList(studentService.getById(idSt).getEmail()));
        mail.setSubject("Лист про відміну занять");
        mail.setBody("");
        Teacher saved = teacherService.getById(idT);
        mailService.sendEmailWithThymeleafToStudentAboutTeacherRemover(mail, saved);
        model.addAttribute("students", studentService.getById(idSt));
        model.addAttribute("teacher", teacherService.getById(idT));
        return "redirect:/teacher/"+idT+"/students";
    }

    @GetMapping("/{id}/lessons")
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
    @GetMapping("/{idTeach}/student/{idSt}/lessons")
    public String gotoStudentTeacherLessons(@PathVariable("idTeach") int idT, @PathVariable("idSt") int idSt,Model model){
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
        return "closedTeacher/studentLessons";
    }


    @PostMapping("/{idTeach}/lessons/deleteLesson/{id}")
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

    @PostMapping("/{idTeach}/lessons/deleteCourse/{id}")
    @Transactional
    public String deleteCourse(@PathVariable("idTeach") int idTeach, @PathVariable("id") int idLes) throws MessagingException {
        Lesson lesson = lessonService.getById(idLes);
        Mail mail = new Mail();
        mail.setTo(Collections.singletonList(lesson.getStudent().getEmail()));
        mail.setSubject("Лист про відміну заняття");
        mail.setBody("");
        Teacher teacher = teacherService.getById(idTeach);
        mailService.sendEmailWithThymeleafToStudentAboutCourseRemoved(mail, teacher, lesson.getLessonTime().getDayOfMonth()+":"+lesson.getLessonTime().getMonth(), lesson.getLessonTime().getHour());
        lessonService.deleteAllByStIdAndTeachIdAndTswIdAndLesTimeAfterNow(lesson.getStudent().getId(), idTeach, lesson.getTimeOfTheWeek().getId(), LocalDateTime.now().minusMinutes(30));
        tswService.removeAllByStIdAndTeachIdAndTswId(lesson.getStudent().getId(), idTeach, lesson.getTimeOfTheWeek().getId());
        return "redirect:/teacher/"+idTeach+"/lessons";
    }
    @PostMapping("/{idTeach}/lessons/editLesson/{id}/{nTId}/{date}")
    public String editLesson(@PathVariable("idTeach") int idTeach, @PathVariable("id") int idLes, @PathVariable("nTId") int newTimeId,@PathVariable("date") String date, Model model) throws MessagingException {
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
                return "redirect:/teacher/"+idTeach+"/lessons";
        }else{
                Teacher teacher = teacherService.getById(idTeach);
                List<String> weekDays = (List<String>) lessonService.compileLessonsForTeacher(teacher).get(0);
                HashMap<String, LessonAdminLessonsDTO> lessonHashMap = (HashMap<String, LessonAdminLessonsDTO>) lessonService.compileLessonsForTeacher(teacher).get(1);
                model.addAttribute("lessons", lessonHashMap);
                model.addAttribute("weekDays", weekDays);
                model.addAttribute("teacher", teacher);
                model.addAttribute("hours", Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19,20,21));
                model.addAttribute("error", "dateInconsistency");
                return "closedStudent/lessons";
        }

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
        mailService.sendEmailWithThymeleafToStudentAboutLessonRemoved(mail, teacher, lessonService.getById(idLes).getLessonTime().getDayOfMonth()+":"+lessonService.getById(idLes).getLessonTime().getMonth(), lessonService.getById(idLes).getLessonTime().getHour());
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
        mailService.sendEmailWithThymeleafToStudentAboutCourseRemoved(mail, teacher, lesson.getLessonTime().getDayOfMonth()+":"+lesson.getLessonTime().getMonth(), lesson.getLessonTime().getHour());
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
            mailService.sendEmailWithThymeleafToStudentAboutLessonTimeEdited(mail, teacher, lesson.getLessonTime().getDayOfMonth()+":"+lesson.getLessonTime().getMonth(),lesson.getLessonTime().getHour(), newDate.getDayOfMonth()+":"+newDate.getMonth(),newDate.getHour());
            lesson.setLessonTime(newDate);
            lessonService.update(lesson.getId(), lesson);
            return "redirect:/teacher/"+idTeach+"/student/"+lesson.getStudent().getId()+"/lessons";
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
            return "closedTeacher/studentLessons";
        }

    }
    @PostMapping("/{teachId}/lessons/addLesson/{cId}/{stId}/{retDate}")
    public String addLesson(@PathVariable("teachId") int idTeach, @PathVariable("cId") int cId, @PathVariable("stId") int stId,@PathVariable("retDate") String date, Model model) throws MessagingException {
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
                    return "redirect:/teacher/"+idTeach+"/lessons";
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
                    return "closedTeacher/lessons";
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
                return "closedTeacher/lessons";
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
            return "closedTeacher/lessons";
        }
    }
    @PostMapping("/{teachId}/lessons/addCourse/{cId}/{stId}/{retDate}")
    public String addCourse(@PathVariable("teachId") int idTeach, @PathVariable("cId") int cId, @PathVariable("stId") int stId,@PathVariable("retDate") String date, Model model) throws MessagingException {
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
                        return "closedTeacher/lessons";
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
                        return "redirect:/teacher/"+idTeach+"/lessons";
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
                    return "closedTeacher/lessons";
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
                return "closedTeacher/lessons";
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
            return "closedTeacher/lessons";
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
                    mailService.sendEmailWithThymeleafToStudentAboutLessonAdded(mail, teacher, lesson.getLessonTime().getDayOfMonth()+":"+lesson.getLessonTime().getMonth(),lesson.getLessonTime().getHour());
                    return "redirect:/teacher/"+idTeach+"/student/"+stId+"/lessons";
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
                    return "closedTeacher/studentLessons";
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
                return "closedTeacher/studentLessons";
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
            return "closedTeacher/studentLessons";
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
                        model.addAttribute("student", studentService.getById(stId));
                        model.addAttribute("courses", courses);
                        model.addAttribute("weekDays", weekDays);
                        model.addAttribute("teacher", teacher);
                        model.addAttribute("hours", Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19,20,21));
                        model.addAttribute("error", "timeNotEmpty");
                        return "closedTeacher/studentLessons";
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
                        return "redirect:/teacher/"+idTeach+"/student/"+stId+"/lessons";
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
                    return "closedTeacher/studentLessons";
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
                return "closedTeacher/studentLessons";
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
            return "closedTeacher/studentLessons";
        }
    }

}
