package com.example.demo.controlers.closedAdmin.adminTeacher;

import com.example.demo.dto.entities.TeacherDTO;
import com.example.demo.dto.other.TimeOfTheWeekDTO;
import com.example.demo.dto.programe.CourseDTO;
import com.example.demo.mapper.entity.StudentMapper;
import com.example.demo.mapper.entity.TeacherMapper;
import com.example.demo.mapper.other.TimeOfTheWeekMapper;
import com.example.demo.mapper.programe.CourseMapper;
import com.example.demo.mapper.programe.LessonMapper;
import com.example.demo.models.email.Mail;
import com.example.demo.models.entities.Teacher;
import com.example.demo.models.other.TimeOfTheWeek;
import com.example.demo.models.programe.Course;
import com.example.demo.models.programe.Lesson;
import com.example.demo.models.thirdTables.EmptyTimesForTeacher;
import com.example.demo.models.thirdTables.TeacherCourse;
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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminTeacherOtherActionsController {
    private TeacherCourseService teacherCourseService;
    private TeacherService teacherService;
    private EmptyTimesForTeacherService emptyTimesForTeacherService;

    private CourseService courseService;
    private StudentCourseService studentCourseService;
    private CommentService commentService;
    private LessonService lessonService;
    private StudentService studentService;
    private LessonMapper lessonMapper;
    private TeacherStudentTimeOfTheWeekService tswService;
    private MailService mailService;
    private AdminService adminService;
    private StudentMapper studentMapper;
    private TeacherMapper teacherMapper;
    private CourseMapper courseMapper;
    private TimeOfTheWeekMapper theWeekMapper;
    private TimeOfTheWeekService theWeekService;


    public AdminTeacherOtherActionsController(TeacherCourseService teacherCourseService, TeacherService teacherService, EmptyTimesForTeacherService emptyTimesForTeacherService, CourseService courseService, StudentCourseService studentCourseService, CommentService commentService, LessonService lessonService, StudentService studentService, LessonMapper lessonMapper, TeacherStudentTimeOfTheWeekService tswService, MailService mailService, AdminService adminService, StudentMapper studentMapper, TeacherMapper teacherMapper, CourseMapper courseMapper, TimeOfTheWeekMapper theWeekMapper, TimeOfTheWeekService  theWeekService) {
        this.teacherCourseService = teacherCourseService;
        this.teacherService = teacherService;
        this.emptyTimesForTeacherService = emptyTimesForTeacherService;

        this.courseService = courseService;
        this.studentCourseService = studentCourseService;
        this.commentService = commentService;
        this.lessonService = lessonService;
        this.studentService = studentService;
        this.lessonMapper = lessonMapper;
        this.tswService = tswService;
        this.mailService = mailService;
        this.adminService = adminService;
        this.studentMapper = studentMapper;
        this.teacherMapper = teacherMapper;
        this.courseMapper = courseMapper;

        this.theWeekMapper = theWeekMapper;
        this.theWeekService = theWeekService;
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
        model.addAttribute("teacher", teacherMapper.mapTeacherToTeacherDTO(new Teacher()));
        model.addAttribute("days", days);
        model.addAttribute("dayNames", dayNames);
        model.addAttribute("hours", hours);
        model.addAttribute("courses", courses.stream().map(el->courseMapper.mapCourseToCourseDTO(el)).collect(Collectors.toList()));
        model.addAttribute("freeTimes", freeTimes.stream().map(el->theWeekMapper.mapTTheWeekToTTheWeekDTO(el)).collect(Collectors.toList()));


        return "closedAdmin/adminTeachers/addNewTeacher";
    }

    @PostMapping("/addTeacher/{password}")
    private String addTeacher(
            @ModelAttribute("teacher") TeacherDTO teacher,
            @PathVariable("password") String password,
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
                model.addAttribute("freeTimes", freeTimesToReload.stream().map(el->theWeekMapper.mapTTheWeekToTTheWeekDTO(el)).collect(Collectors.toList()));
                return "closedAdmin/adminTeachers/addNewTeacher";
            }
            Teacher teacher1 = teacherMapper.mapTeacherDTOToTeacher(teacher);
            if(password.equals("NOPASS")){
                model.addAttribute("days", daysToReload);
                model.addAttribute("dayNames", dayNamesToReload);
                model.addAttribute("hours", hoursToReload);
                model.addAttribute("courses", coursesToReload.stream().map(course -> courseMapper.mapCourseToCourseDTO(course)).collect(Collectors.toList()));
                model.addAttribute("freeTimes", freeTimesToReload.stream().map(el->theWeekMapper.mapTTheWeekToTTheWeekDTO(el)).collect(Collectors.toList()));
                model.addAttribute("error", "NOPASS");
                return "closedAdmin/adminTeachers/addNewTeacher";
            }
            else{
                teacher1.setPassword(teacherService.getById(teacher.getId()).getPassword());
            }
            teacher.setApproved(true);
            teacherService.create(teacher1);
            List<Course> courses = courseService.getCoursesThroughIds(coursesIds);
            List<TimeOfTheWeek> freeTimes = theWeekService.getTimesOfTheWeekThroughIds(freeTimesIds);

            for (Course course : courses) {
                TeacherCourse teacherCourse = new TeacherCourse(course, teacher1);
                teacherCourseService.create(teacherCourse);
            }

            for (TimeOfTheWeek timeOfTheWeek : freeTimes) {
                EmptyTimesForTeacher emptyTimesForTeacher = new EmptyTimesForTeacher(teacher1, timeOfTheWeek);
                emptyTimesForTeacherService.create(emptyTimesForTeacher);
            }


            return "redirect:/admin/homepage";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            model.addAttribute("days", daysToReload);
            model.addAttribute("dayNames", dayNamesToReload);
            model.addAttribute("hours", hoursToReload);
            model.addAttribute("courses", coursesToReload.stream().map(course -> courseMapper.mapCourseToCourseDTO(course)).collect(Collectors.toList()));
            model.addAttribute("freeTimes", freeTimesToReload.stream().map(el->theWeekMapper.mapTTheWeekToTTheWeekDTO(el)).collect(Collectors.toList()));
            model.addAttribute("error", "general");
            return "closedAdmin/adminTeachers/addNewTeacher";
        }


    }



    @GetMapping("/teachers")
    public String gotoTeachers(Model model) {
        model.addAttribute("teachers", teacherService.getAll().stream().map(teacher -> teacherMapper.mapTeacherToTeacherDTO(teacher)).collect(Collectors.toList()));
        return "closedAdmin/adminTeachers/teachers";
    }






    @GetMapping("/teacher/{idTeach}/edit")
    public String gotoEditTeacher(@PathVariable("idTeach") int id, Model model){
        Teacher teacher = teacherService.getById(id);
        teacher.setPassword("");
        model.addAttribute("teacher", teacherMapper.mapTeacherToTeacherDTO(teacher));
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

        model.addAttribute("freeTimes", allfreeTimes.stream().map(el->theWeekMapper.mapTTheWeekToTTheWeekDTO(el)).collect(Collectors.toList()));
        model.addAttribute("teacherFreeTimes", teacherFreeTimes);
        model.addAttribute("allCourses", allCourses.stream().map(el->courseMapper.mapCourseToCourseDTO(el)).collect(Collectors.toList()));
        model.addAttribute("hours", Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19,20,21));
        model.addAttribute("dayNames", dayNames);
        model.addAttribute("days", days);
        model.addAttribute("password", "");
        //////
        return "closedAdmin/adminTeachers/editTeacher";
    }
    @PostMapping("/teacher/{id}/edit/{password}")
    @Transactional
    public String updateTeacher(@PathVariable("id") int id,
                                @ModelAttribute("teacher") TeacherDTO teacher,
                                @PathVariable("password") String password,
                                @RequestParam(value = "freeTimeIds", required = false) List<Integer> freeTimeIds,
                                @RequestParam(value = "days", required = false) List<Integer> days,
                                @RequestParam(value = "dayNames", required = false) List<String> dayNames,
                                @RequestParam(value = "newCourseIds", required = false) List<Integer> newCourseIds,
                                @RequestParam(value = "freeTimes", required = false) List<TimeOfTheWeekDTO> allfreeTimes,
                                @RequestParam(value = "teacherFreeTimes", required = false) List<Integer> teacherFreeTimes,
                                @RequestParam(value = "allCourses", required = false) List<CourseDTO> allCourses,
                                Model model) throws MessagingException {
        if(teacherService.checkIfExistsByEmail(teacher.getEmail()) && !teacherService.getById(teacher.getId()).getEmail().equals(teacher.getEmail())){
            model.addAttribute("freeTimes", allfreeTimes);
            model.addAttribute("teacherFreeTimes", teacherFreeTimes);
            model.addAttribute("allCourses", allCourses);
            model.addAttribute("dayNames", dayNames);
            model.addAttribute("days", days);
            model.addAttribute("hours", Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19,20,21));
            model.addAttribute("error", "exists");
            return "closedAdmin/adminTeachers/editTeacher";
        }
        else{
            if(newCourseIds==null || newCourseIds.isEmpty()){
                ///
            }else{
                for (int cId : newCourseIds){
                    teacherCourseService.create(new TeacherCourse(courseService.getById(cId), teacherMapper.mapTeacherDTOToTeacher(teacher)));
                }
            }






            //////
            if(password.equals("OLDPASS")){
                password = teacherService.getById(teacher.getId()).getPassword();
            }
            List<Integer> oldTimeIds = new ArrayList<>();

            emptyTimesForTeacherService.findAllByTeachId(id).stream().forEach(time->oldTimeIds.add(time.getTime().getId()));
            if(!oldTimeIds.equals(freeTimeIds)){
                emptyTimesForTeacherService.removeAllByTeacherId(teacher.getId());
                for(int iD : freeTimeIds){
                    oldTimeIds.remove(Integer.valueOf(iD));
                    emptyTimesForTeacherService.create(new EmptyTimesForTeacher(teacherService.getById(teacher.getId()), theWeekService.getById(iD)));
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
                        Teacher saved = teacherService.getById(teacher.getId());
                        mailService.sendEmailWithThymeleafToStudentAboutCourseRemoved(mail, teacherMapper.mapTeacherToTeacherDTO(saved), String.valueOf(lessons.get(0).getLessonTime().getDayOfWeek()), lessons.get(0).getLessonTime().getHour());
                    }


                }

            }
            Teacher retTeach = teacherMapper.mapTeacherDTOToTeacher(teacher);
            retTeach.setPassword(password);
            teacherService.update(teacher.getId(), retTeach);




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
        model.addAttribute("teacher", teacherMapper.mapTeacherToTeacherDTO(teacher));
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
        int[] allTeachersEarnings = {0};
        int[] ourShare = {0};
        lessonService.findAllByTeachId(id).stream().filter(lesson -> lesson.getStatus().equals("was")).forEach(lesson -> ourShare[0] = ourShare[0] +(lesson.getDuration()*lesson.getCourse().getCostPerLesson()));
        lessonService.findAllByTeachId(id).stream().filter(lesson -> lesson.getStatus().equals("was")).forEach(lesson -> allTeachersEarnings[0] = allTeachersEarnings[0] +(lesson.getDuration()*lesson.getCourse().getTeacherShare()));
        ourShare[0] = ourShare[0]-allTeachersEarnings[0];
        model.addAttribute("allEarnings", allTeachersEarnings[0]);
        model.addAttribute("ourShare", ourShare[0]);
        model.addAttribute("freeTimes", allfreeTimes.stream().map(el->theWeekMapper.mapTTheWeekToTTheWeekDTO(el)).collect(Collectors.toList()));
        model.addAttribute("students", studentService.getAll().stream().map(el->studentMapper.mapStudentToStudentDTO(el)).collect(Collectors.toList()));
        model.addAttribute("teacherFreeTimes", teacherFreeTimes);
        model.addAttribute("allCourses", allCourses.stream().map(el->courseMapper.mapCourseToCourseDTO(el)).collect(Collectors.toList()));
        model.addAttribute("hours", Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19,20,21));
        model.addAttribute("dayNames", dayNames);
        model.addAttribute("days", days);
        //////
        return "closedAdmin/adminTeachers/teacherInfo";
    }
    @PostMapping("/teacher/{teachId}/setEarnedMoneyToZero")

    public String setEarnedMoneyToZero(Model model, @PathVariable("teachId") int id){
        Teacher teacher = teacherService.getById(id);
        teacher.setUnpaidMoney(0);
        teacherService.update(id, teacher);
        return "redirect:/admin/teacher/"+id+"/info";
    }
    @PostMapping("/teacher/{teachId}/setEarnedMoney")

    public String setEarnedMoney(Model model, @PathVariable("teachId") int id, @ModelAttribute("teacher") TeacherDTO teacherDTO){
        Teacher teacher = teacherService.getById(id);
        teacher.setUnpaidMoney(teacherDTO.getUnpaidMoney());
        teacherService.update(id, teacher);
        return "redirect:/admin/teacher/"+id+"/info";
    }

}
