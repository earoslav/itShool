package com.example.demo.controlers.closedTeacher.teacher;

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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/teacher")
public class TeacherHomepageController {
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
    private TimeOfTheWeekMapper theWeekMapper;
    private PasswordEncoder passwordEncoder;


    public TeacherHomepageController(TeacherService teacherService, LessonMapper lessonMapper, TimeOfTheWeekService theWeekService, CourseService courseService, EmptyTimesForTeacherService emptyTimesForTeacherService, LessonService lessonService, TeacherCourseService teacherCourseService, MailService mailService, TeacherStudentTimeOfTheWeekService tswService, StudentService studentService, StudentMapper studentMapper, TeacherMapper teacherMapper, CourseMapper courseMapper, TimeOfTheWeekMapper theWeekMapper, PasswordEncoder passwordEncoder) {
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
        this.theWeekMapper = theWeekMapper;
        this.passwordEncoder = passwordEncoder;
    }
    @GetMapping("/{idTeach}/homepage")
    public String gotoTeacher(@PathVariable("idTeach") int id, Model model){
        Teacher teacher = teacherService.getById(id);
        teacher.setPassword("");
        model.addAttribute("teacher", teacherMapper.mapTeacherToTeacherDTO(teacher));
        model.addAttribute("password", "");
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
        //////
        return "closedTeacher/teacherHomepage/homepage";
    }
    @GetMapping("/{idTeach}/homepage/{output}")
    public String gotoTeacher(@PathVariable("idTeach") int id,@PathVariable("output") String output, Model model){
        Teacher teacher = teacherService.getById(id);
        teacher.setPassword("");
        model.addAttribute("teacher", teacherMapper.mapTeacherToTeacherDTO(teacher));
        model.addAttribute("password", "");
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

        model.addAttribute("output", output);
        model.addAttribute("freeTimes", allfreeTimes.stream().map(el->theWeekMapper.mapTTheWeekToTTheWeekDTO(el)).collect(Collectors.toList()));
        model.addAttribute("teacherFreeTimes", teacherFreeTimes);
        model.addAttribute("allCourses", allCourses.stream().map(el->courseMapper.mapCourseToCourseDTO(el)).collect(Collectors.toList()));
        model.addAttribute("hours", Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19,20,21));
        model.addAttribute("dayNames", dayNames);
        model.addAttribute("days", days);
        //////
        return "closedTeacher/teacherHomepage/homepage";
    }
    @PostMapping("/{id}/homepage/{password}")
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
        if(teacherService.checkIfExistsByEmail(teacher.getUser().getEmail()) && !teacherService.getById(teacher.getId()).getEmail().equals(teacher.getUser().getEmail())){
            return "redirect:/teacher/"+teacher.getId()+"/homepage/exists";
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
                    emptyTimesForTeacherService.create(new EmptyTimesForTeacher(teacherMapper.mapTeacherDTOToTeacher(teacher), theWeekService.getById(iD)));
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
                        Teacher saved = teacherMapper.mapTeacherDTOToTeacher(teacher);
                        mailService.sendEmailWithThymeleafToStudentAboutCourseRemoved(mail, teacherMapper.mapTeacherToTeacherDTO(saved), String.valueOf(lessons.get(0).getLessonTime().getDayOfWeek()), lessons.get(0).getLessonTime().getHour());
                    }


                }

            }
            Teacher teacher1 = teacherMapper.mapTeacherDTOToTeacher(teacher);
            teacher1.setPassword(passwordEncoder.encode(password));
            teacherService.update(teacher.getId(), teacher1);




        }
        return "redirect:/teacher/"+teacher.getId()+"/homepage/teacherEdited";
    }
}
