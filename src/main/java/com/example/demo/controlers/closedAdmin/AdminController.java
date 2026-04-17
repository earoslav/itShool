package com.example.demo.controlers.closedAdmin;

import com.example.demo.dto.LessonDTO;
import com.example.demo.dto.adminLessons.LessonAdminLessonsDTO;
import com.example.demo.mapper.LessonMapper;
import com.example.demo.models.*;
import com.example.demo.services.*;
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
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private TeacherCourseService teacherCourseService;
    private TeacherService teacherService;
    private EmptyTimesForTeacherService emptyTimesForTeacherService;
    private AdminService adminService;
    private TimeOfTheWeekService theWeekService;
    private CourseService courseService;
    private StudentCourseService studentCourseService;
    private CommentService commentService;
    private LessonService lessonService;
    private StudentService studentService;
    private LessonMapper lessonMapper;
    public AdminController(TeacherCourseService teacherCourseService, TeacherService teacherService, EmptyTimesForTeacherService emptyTimesForTeacherService, AdminService adminService, TimeOfTheWeekService theWeekService, CourseService courseService, StudentCourseService studentCourseService, CommentService commentService, LessonService lessonService, StudentService studentService, LessonMapper lessonMapper) {
        this.teacherCourseService = teacherCourseService;
        this.teacherService = teacherService;
        this.emptyTimesForTeacherService = emptyTimesForTeacherService;
        this.adminService = adminService;
        this.theWeekService = theWeekService;
        this.courseService = courseService;
        this.studentCourseService = studentCourseService;
        this.commentService = commentService;
        this.lessonService = lessonService;

        this.studentService = studentService;
        this.lessonMapper = lessonMapper;
    }
    @GetMapping("/homepage")
    private String gotoHomepage(Model model){
        Admin admin = adminService.getAdmin();

        model.addAttribute("admin", admin);
        return "closedAdmin/homepage";
    }

    @GetMapping("/addTeacher")
    private String gotoCreateTeacher(

                                     Model model){

        List<Integer> days = List.of(7, 1, 2, 3, 4, 5, 6); // Sunday first
        List<String> dayNames = List.of("Неділя", "Понеділок", "Вівторок", "Середа", "Четвер", "П'ятниця", "Субота");
        List<Integer> hours = new ArrayList<>();
        for (int h = 8; h <= 21; h++) {
            hours.add(h);
        }
        List<TimeOfTheWeek> freeTimes = theWeekService.getAll();
        List<Course> courses = courseService.getAll();
//        if(name!=null){
//            freeTimes = theWeekService.getTimesOfTheWeekThroughIds(timeIds);
//            courses = courseService.getCoursesThroughIds(courseIds);
//            model.addAttribute("teacher", new Teacher(name, age, email, password, comment,phonenumber, tgUser, approved));
//        }else{
//            model.addAttribute("teacher", new Teacher());
//        }
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
                                @RequestParam("freeTimeIds")List<Integer> freeTimesIds, Model model){
        List<Course> coursesToReload = courseService.getAll();
        List<TimeOfTheWeek> freeTimesToReload = theWeekService.getAll();
        List<Integer> daysToReload = List.of(7, 1, 2, 3, 4, 5, 6); // Sunday first
        List<String> dayNamesToReload = List.of("Неділя", "Понеділок", "Вівторок", "Середа", "Четвер", "П'ятниця", "Субота");
        List<Integer> hoursToReload = new ArrayList<>();
        for (int h = 8; h <= 21; h++) {
            hoursToReload.add(h);
        }
        try{


            if(teacherService.checkIfExistsByEmail(teacher.getEmail())){
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
            for(Course course : courses){
                TeacherCourse teacherCourse = new TeacherCourse(course, teacher);
                teacherCourseService.create(teacherCourse);
                teacherCourses.add(teacherCourse);
            }
            teacher.setTeacherCourses(teacherCourses);
            List<EmptyTimesForTeacher> freeTimesForTeacher = new ArrayList<>();
            for(TimeOfTheWeek timeOfTheWeek : freeTimes){
                EmptyTimesForTeacher emptyTimesForTeacher = new EmptyTimesForTeacher(teacher, timeOfTheWeek);
                emptyTimesForTeacherService.create(emptyTimesForTeacher);
                freeTimesForTeacher.add(emptyTimesForTeacher);
            }
            teacher.setEmptyTimesForTeachers(freeTimesForTeacher);

            return "redirect:/admin/homepage";
        }catch (Exception e){
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
    @GetMapping("/students")
    public String gotoStudents(Model model){
        model.addAttribute("students", studentService.getAll());
        return "closedAdmin/students";
    }
    @GetMapping("/student/edit/{id}")
    public String gotoEditStudent(@PathVariable("id") int id,Model model){


        model.addAttribute("student", studentService.getById(id));
        return "closedAdmin/editStudent";
    }

    @PostMapping("/student/edit/{id}")
    public String editStudent(
            @PathVariable("id") int id,
            @ModelAttribute("student") Student student,
            Model model){

        try{
            if(studentService.checkIfExistsByEmail(student.getEmail())&&!studentService.getById(id).getEmail().equals(student.getEmail())){
                model.addAttribute(student);
                model.addAttribute("error", "exists");
                return "closedAdmin/editStudent";
            }



            studentService.update(id, student);
            model.addAttribute("student", studentService.getById(id));
            return "redirect:/admin/students";
        }catch (Exception e){
            System.out.println(e.getMessage());
            model.addAttribute(student);
            model.addAttribute("error", "general");
            return "closedAdmin/editStudent";
        }

    }

    @PostMapping("/student/delete")

    public String deleteStudent ( @RequestParam("studentId") int id){
        Student student = studentService.getById(id);
        lessonService.deleteAllLessonsByStudent(student);
        studentCourseService.deleteAllCoursesByStudent(student);
        commentService.deleteAllByStudent(student);
        studentService.deleteById(id);
        return "redirect:/admin/students";
    }
    @GetMapping("/student/lessons/{id}")
    public String gotoStudentLessons(@PathVariable("id") int id, Model model) {

        Student student = studentService.getById(id);
        List<String> weekDays = (List<String>) lessonService.compileLessonsForStudent(student).get(0);
        HashMap<String, LessonAdminLessonsDTO> lessonHashMap = (HashMap<String, LessonAdminLessonsDTO>) lessonService.compileLessonsForStudent(student).get(1);

        model.addAttribute("lessons", lessonHashMap);
        model.addAttribute("weekDays", weekDays);
        model.addAttribute("hours", Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19,20,21));
        System.out.println(lessonHashMap);
        return "closedAdmin/studentLessons"; // Ваша HTML сторінка
    }






    //    @GetMapping("/student/lessons/delete/{id}")
//    public String gotoStudentLessons(@PathVariable("id") int id,
//                                     @RequestParam("lessons")  HashMap<String, Lesson> lessonHashMap,
//                                     @RequestParam("weekDays")  List<String> weekDays,
//                                     Model model) {
//
//        model.addAttribute("student", studentService.getById(4));
//        model.addAttribute("lessons", lessonHashMap);
//        model.addAttribute("weekDays", weekDays);
//        model.addAttribute("hours", Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19,20,21));
//        return "closedAdmin/studentLessonsDelete";
//    }


}
