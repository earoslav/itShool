package com.example.demo.controlers.closedAdmin.adminTeacher;

import com.example.demo.dto.entities.TeacherDTO;
import com.example.demo.mapper.entity.StudentMapper;
import com.example.demo.mapper.entity.TeacherMapper;
import com.example.demo.mapper.other.TimeOfTheWeekMapper;
import com.example.demo.mapper.programe.CourseMapper;
import com.example.demo.mapper.programe.LessonMapper;
import com.example.demo.models.entities.Teacher;
import com.example.demo.models.entities.User;
import com.example.demo.models.other.TimeOfTheWeek;
import com.example.demo.models.programe.Course;
import com.example.demo.models.programe.Lesson;
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
import com.example.demo.services.security.UserService;
import com.example.demo.services.thirdTable.EmptyTimesForTeacherService;
import com.example.demo.services.thirdTable.TeacherCourseService;
import com.example.demo.services.thirdTable.TeacherStudentTimeOfTheWeekService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// AdminTeacherOtherActionsController handles web pages for the "teachers" module.
// The methods below accept parameters from URLs or forms, call project services, and return the required Thymeleaf templates or redirects.
@Controller
@RequestMapping("/admin")
public class AdminTeacherOtherActionsController {
    private TeacherCourseService teacherCourseService;
    private TeacherService teacherService;
    private EmptyTimesForTeacherService emptyTimesForTeacherService;

    private CourseService courseService;

    private CommentService commentService;
    private LessonService lessonService;
    private StudentService studentService;
    private LessonMapper lessonMapper;
    private TeacherStudentTimeOfTheWeekService tswService;
    private MailService mailService;
    private AdminService adminService;
    private StudentMapper studentMapper;
    private TeacherMapper teacherMapper;
    private final TimeOfTheWeekService weekService;
    private CourseMapper courseMapper;
    private TimeOfTheWeekMapper theWeekMapper;
    private PasswordEncoder passwordEncoder;

    private UserService userService;

    // Receives dependencies through Spring: TeacherCourseService, TimeOfTheWeekService, TeacherService, EmptyTimesForTeacherService, CourseService, StudentCourseService, CommentService, and others.
    // These services and mappers are required by the class methods to work with the "teachers" module without manual object creation.
    @Autowired
    public AdminTeacherOtherActionsController(TeacherCourseService teacherCourseService, TimeOfTheWeekService theWeekService, TeacherService teacherService, EmptyTimesForTeacherService emptyTimesForTeacherService, CourseService courseService, CommentService commentService, LessonService lessonService, StudentService studentService, LessonMapper lessonMapper, TeacherStudentTimeOfTheWeekService tswService, MailService mailService, AdminService adminService, StudentMapper studentMapper, TeacherMapper teacherMapper, CourseMapper courseMapper, TimeOfTheWeekMapper theWeekMapper, PasswordEncoder passwordEncoder, UserService userService) {
        this.teacherCourseService = teacherCourseService;
        this.teacherService = teacherService;
        this.emptyTimesForTeacherService = emptyTimesForTeacherService;
        this.courseService = courseService;

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
        this.weekService = theWeekService;
        this.theWeekMapper = theWeekMapper;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;

        System.err.println(this);
    }

    // Opens the GET /addTeacher route and prepares data for the "closedAdmin/adminTeachers/addNewTeacher" template.
    // Adds "teacher", "days", "dayNames", "hours", "courses", and "freeTimes" to the Model; retrieves data via `weekService.getAll`, `courseService.getAll`, `teacherMapper.mapTeacherToTeacherDTO`, `courseMapper.mapCourseToCourseDTO`, and `theWeekMapper.mapTTheWeekToTTheWeekDTO`.
    @GetMapping("/addTeacher")
    public String gotoCreateTeacher(Model model) {
        System.err.println(this);
        List<Integer> days = List.of(7, 1, 2, 3, 4, 5, 6); // Sunday first
        List<String> dayNames = List.of("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");
        List<TimeOfTheWeek> freeTimes = weekService.getAll();
        List<Course> courses = courseService.getAll();
        model.addAttribute("teacher", teacherMapper.mapTeacherToTeacherDTO(new Teacher()));
        model.addAttribute("days", days);
        model.addAttribute("dayNames", dayNames);
        model.addAttribute("hours", Arrays.asList("8:00", "8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30"));
        model.addAttribute("courses", courses.stream().map(el -> courseMapper.mapCourseToCourseDTO(el)).collect(Collectors.toList()));
        model.addAttribute("freeTimes", freeTimes.stream().map(el -> theWeekMapper.mapTTheWeekToTTheWeekDTO(el)).collect(Collectors.toList()));
        return "closedAdmin/adminTeachers/addNewTeacher";
    }

    // Opens the GET /addTeacher/{output} route and prepares data for the "closedAdmin/adminTeachers/addNewTeacher" template.
    // Adds "teacher", "output", "days", "dayNames", "hours", "courses", and others to the Model; retrieves data via `weekService.getAll`, `courseService.getAll`, `teacherMapper.mapTeacherToTeacherDTO`, `courseMapper.mapCourseToCourseDTO`, and `theWeekMapper.mapTTheWeekToTTheWeekDTO`.
    @GetMapping("/addTeacher/{output}")
    public String gotoCreateTeacherWithOutput(@PathVariable("output") String output, Model model) {
        System.err.println(this);
        List<Integer> days = List.of(7, 1, 2, 3, 4, 5, 6); // Sunday first
        List<String> dayNames = List.of("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");

        List<TimeOfTheWeek> freeTimes = weekService.getAll();
        List<Course> courses = courseService.getAll();
        model.addAttribute("teacher", teacherMapper.mapTeacherToTeacherDTO(new Teacher()));
        model.addAttribute("output", output);
        model.addAttribute("days", days);
        model.addAttribute("dayNames", dayNames);
        model.addAttribute("hours", Arrays.asList("8:00", "8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30"));
        model.addAttribute("courses", courses.stream().map(el -> courseMapper.mapCourseToCourseDTO(el)).collect(Collectors.toList()));
        model.addAttribute("freeTimes", freeTimes.stream().map(el -> theWeekMapper.mapTTheWeekToTTheWeekDTO(el)).collect(Collectors.toList()));
        return "closedAdmin/adminTeachers/addNewTeacher";
    }

    // Opens the GET /addTeacher/{name}/{age}/{email}/{password}/{comment}/{tgUser}/{phonenumber}/{approved}/{courseIds}/{timeIds} route and prepares data for the "closedAdmin/adminTeachers/addNewTeacher" template.
    // Adds "freeTimeIds", "courseIds", "teacher", "days", "dayNames", "hours", and others to the Model; retrieves data via `weekService.getAll`, `courseService.getAll`, `teacherMapper.mapTeacherToTeacherDTO`, `courseMapper.mapCourseToCourseDTO`, and `theWeekMapper.mapTTheWeekToTTheWeekDTO`.
    @GetMapping("/addTeacher/{name}/{age}/{email}/{password}/{comment}/{tgUser}/{phonenumber}/{approved}/{courseIds}/{timeIds}")
    public String gotoCreateTeacherFromEmail(Model model,
                                             @PathVariable(value = "name", required = false) String name,
                                             @PathVariable(value = "age", required = false) int age,
                                             @PathVariable(value = "email", required = false) String email,
                                             @PathVariable(value = "password", required = false) String password,
                                             @PathVariable(value = "comment", required = false) String comment,
                                             @PathVariable(value = "tgUser", required = false) String tgUser,
                                             @PathVariable(value = "phonenumber", required = false) String phonenumber,
                                             @PathVariable(value = "approved", required = false) boolean approved,
                                             @PathVariable(value = "courseIds", required = false) String courseIds,
                                             @PathVariable(value = "timeIds", required = false) String timeIds) {

        List<Integer> days = List.of(7, 1, 2, 3, 4, 5, 6); // Sunday first
        List<String> dayNames = List.of("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");

        List<Integer> courseIdsInt = new ArrayList<>();
        Arrays.stream(courseIds.replace("[", "").replace("]", "").split(", ")).forEach(num -> courseIdsInt.add(Integer.valueOf(num)));

        List<Integer> timeIdsInt = new ArrayList<>();
        Arrays.stream(timeIds.replace("[", "").replace("]", "").split(", ")).forEach(num -> timeIdsInt.add(Integer.valueOf(num)));
        User user = new User(name, email, passwordEncoder.encode(password), "TEACHER");
        Teacher t = new Teacher();
        t.setAge(age);
        t.setComment(comment);
        t.setTgUsername(tgUser);
        t.setPhoneNumber(phonenumber);
        t.setApproved(0);
        t.setUser(user);


        List<TimeOfTheWeek> freeTimes = weekService.getAll();
        List<Course> courses = courseService.getAll();
        model.addAttribute("freeTimeIds", timeIdsInt);
        model.addAttribute("courseIds", courseIdsInt);
        model.addAttribute("teacher", teacherMapper.mapTeacherToTeacherDTO(t));
        model.addAttribute("days", days);
        model.addAttribute("dayNames", dayNames);
        model.addAttribute("hours", Arrays.asList("8:00", "8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30"));
        model.addAttribute("courses", courses.stream().map(el -> courseMapper.mapCourseToCourseDTO(el)).collect(Collectors.toList()));
        model.addAttribute("freeTimes", freeTimes.stream().map(el -> theWeekMapper.mapTTheWeekToTTheWeekDTO(el)).collect(Collectors.toList()));


        return "closedAdmin/adminTeachers/addNewTeacher";
    }

    // Creates data at the POST /addTeacher/{password} route in the "teachers" module.
    // Calls `userService.checkIfExistsByEmail`, `userService.create`, `teacherService.create`, `courseService.getCoursesThroughIds`, `weekService.getTimesOfTheWeekThroughIds`, and others; returns redirects upon completion.
    @PostMapping("/addTeacher/{password}")
    public String addTeacher(
            @ModelAttribute("teacher") TeacherDTO teacher,
            @PathVariable("password") String password,
            @RequestParam(value = "courseIds", required = false) List<Integer> coursesIds,
            @RequestParam(value = "freeTimeIds", required = false) List<Integer> freeTimesIds, Model model) {

        if (userService.checkIfExistsByEmail(teacher.getUser().getEmail())) {
            return "redirect:/admin/addTeacher/EXISTS";
        }
        if (coursesIds == null || coursesIds.isEmpty()) {
            return "redirect:/admin/addTeacher/NO_COURSES";
        }
        if (freeTimesIds == null || freeTimesIds.isEmpty()) {
            return "redirect:/admin/addTeacher/NO_FREE_TIMES";
        }
        Teacher teacher1 = teacherMapper.mapTeacherDTOToTeacher(teacher);
        if (password.equals("NO_PASS")) {
            return "redirect:/admin/addTeacher/NO_PASS";
        } else {
            teacher1.setPassword(passwordEncoder.encode(password));
        }

        String ids = "";
        for (int idT : freeTimesIds) {
            ids = ids + idT + ",";
        }
        User user = new User(teacher.getUser().getName(), teacher.getUser().getEmail(), passwordEncoder.encode(password), "TEACHER");
        userService.create(user);
        teacher1.setApproved(1);
        teacher1.setUser(user);
        teacher1.setFreeTimeIds(ids);
        teacherService.create(teacher1);
        List<Course> courses = courseService.getCoursesThroughIds(coursesIds);
//        List<TimeOfTheWeek> freeTimes = weekService.getTimesOfTheWeekThroughIds(freeTimesIds);

        for (Course course : courses) {
            TeacherCourse teacherCourse = new TeacherCourse(course, teacher1);
            teacherCourseService.create(teacherCourse);
        }

//        for (TimeOfTheWeek timeOfTheWeek : freeTimes) {
//            EmptyTimesForTeacher emptyTimesForTeacher = new EmptyTimesForTeacher(teacher1, timeOfTheWeek);
//            emptyTimesForTeacherService.create(emptyTimesForTeacher);
//        }


        return "redirect:/admin/homepage/TEACHER_ADDED";


    }

    // Opens the GET /teachers route and prepares data for the "closedAdmin/adminTeachers/teachers" template.
    // Adds "teachers" to the Model; retrieves data via `teacherService.getAll` and `teacherMapper.mapTeacherToTeacherDTO`.
    @GetMapping("/teachers")
    public String gotoTeachers(Model model) {
        model.addAttribute("teachers", teacherService.getAll().stream().map(teacher -> teacherMapper.mapTeacherToTeacherDTO(teacher)).collect(Collectors.toList()));
        return "closedAdmin/adminTeachers/teachers";
    }

    // Opens the GET /teacher/{idTeach}/edit route and prepares data for the "closedAdmin/adminTeachers/editTeacher" template.
    // Adds "teacher", "freeTimes", "teacherFreeTimes", "allCourses", "hours", "dayNames", and others to the Model; retrieves data via `teacherService.getById`, `weekService.getAll`, `courseService.getAll`, `teacherMapper.mapTeacherToTeacherDTO`, `theWeekMapper.mapTTheWeekToTTheWeekDTO`, and others.
    @GetMapping("/teacher/{idTeach}/edit")
    public String gotoEditTeacher(@PathVariable("idTeach") int id, Model model) {
        Teacher teacher = teacherService.getById(id);
        teacher.setPassword("");
        model.addAttribute("teacher", teacherMapper.mapTeacherToTeacherDTO(teacher));
        List<TimeOfTheWeek> allfreeTimes = weekService.getAll();
        List<Integer> teacherFreeTimes = new ArrayList<>();
        List<Course> allCourses = courseService.getAll();
        List<Integer> days = List.of(7, 1, 2, 3, 4, 5, 6); // Sunday first
        List<String> dayNames = List.of("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");

        ///////////////
        if (teacher.getFreeTimeIds() != null && !teacher.getFreeTimeIds().isEmpty()) {
            teacherFreeTimes = Arrays.stream(teacher.getFreeTimeIds().split(",")).map(Integer::parseInt).toList();
        }

        model.addAttribute("freeTimes", allfreeTimes.stream().map(el -> theWeekMapper.mapTTheWeekToTTheWeekDTO(el)).collect(Collectors.toList()));
        model.addAttribute("teacherFreeTimes", teacherFreeTimes);
        model.addAttribute("allCourses", allCourses.stream().map(el -> courseMapper.mapCourseToCourseDTO(el)).collect(Collectors.toList()));
        model.addAttribute("hours", Arrays.asList("8:00", "8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30"));
        model.addAttribute("dayNames", dayNames);
        model.addAttribute("days", days);
        model.addAttribute("password", "");
        //////
        return "closedAdmin/adminTeachers/editTeacher";
    }

    // Opens the GET /teacher/{idTeach}/edit/{output} route and prepares data for the "closedAdmin/adminTeachers/editTeacher" template.
    // Adds "teacher", "output", "freeTimes", "teacherFreeTimes", "allCourses", "hours", and others to the Model; retrieves data via `teacherService.getById`, `weekService.getAll`, `courseService.getAll`, `teacherMapper.mapTeacherToTeacherDTO`, `theWeekMapper.mapTTheWeekToTTheWeekDTO`, and others.
    @GetMapping("/teacher/{idTeach}/edit/{output}")
    public String gotoEditTeacherWithOutput(@PathVariable("idTeach") int id,
                                            @PathVariable("output") String output, Model model) {
        Teacher teacher = teacherService.getById(id);
        teacher.setPassword("");
        model.addAttribute("teacher", teacherMapper.mapTeacherToTeacherDTO(teacher));
        List<TimeOfTheWeek> allfreeTimes = weekService.getAll();
        List<Integer> teacherFreeTimes = new ArrayList<>();
        List<Course> allCourses = courseService.getAll();
        List<Integer> days = List.of(7, 1, 2, 3, 4, 5, 6); // Sunday first
        List<String> dayNames = List.of("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");

        ///////////////
        if (teacher.getFreeTimeIds() != null && !teacher.getFreeTimeIds().isEmpty()) {
            teacherFreeTimes = Arrays.stream(teacher.getFreeTimeIds().split(",")).map(Integer::parseInt).toList();
        }

        model.addAttribute("output", output);
        model.addAttribute("freeTimes", allfreeTimes.stream().map(el -> theWeekMapper.mapTTheWeekToTTheWeekDTO(el)).collect(Collectors.toList()));
        model.addAttribute("teacherFreeTimes", teacherFreeTimes);
        model.addAttribute("allCourses", allCourses.stream().map(el -> courseMapper.mapCourseToCourseDTO(el)).collect(Collectors.toList()));
        model.addAttribute("hours", Arrays.asList("8:00", "8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30"));
        model.addAttribute("dayNames", dayNames);
        model.addAttribute("days", days);
        model.addAttribute("password", "");
        //////
        return "closedAdmin/adminTeachers/editTeacher";
    }

    // Updates data at the POST /teacher/{id}/edit/{password} route in the "teachers" module.
    // Calls `teacherService.checkIfExistsByEmail`, `teacherService.getById`, `teacherCourseService.create`, `courseService.getById`, `emptyTimesForTeacherService.findAllByTeachId`, and others; returns "redirect:/admin/teacher/" upon completion.
    @PostMapping("/teacher/{id}/edit/{password}")
    @Transactional
    public String updateTeacher(@PathVariable("id") int id,
                                @ModelAttribute("teacher") TeacherDTO teacher,
                                @PathVariable("password") String password,
                                @RequestParam(value = "freeTimeIds", required = false) List<Integer> freeTimeIds,
                                @RequestParam(value = "newCourseIds", required = false) List<Integer> newCourseIds,
                                Model model) throws MessagingException {
        if (teacherService.checkIfExistsByEmail(teacher.getUser().getEmail()) && !teacherService.getById(teacher.getId()).getEmail().equals(teacher.getUser().getEmail())) {
            return "redirect:/admin/teacher/" + teacher.getId() + "/edit/EXISTS";
        }
        if (newCourseIds != null && !newCourseIds.isEmpty()) {
            for (int cId : newCourseIds) {
                teacherCourseService.create(new TeacherCourse(courseService.getById(cId), teacherMapper.mapTeacherDTOToTeacher(teacher)));
            }
        }
        if (freeTimeIds == null || freeTimeIds.isEmpty()) {
            freeTimeIds = new ArrayList<>();
        }
        List<Integer> oldTimeIds = new ArrayList<>();
        String teachIds = teacherService.getById(id).getFreeTimeIds();
        if (teachIds != null && !teachIds.equals("")) {
            oldTimeIds = Arrays.stream(teacherService.getById(id).getFreeTimeIds().split(",")).map(Integer::parseInt).toList();
        }
        if (!oldTimeIds.equals(freeTimeIds)) {
            String ids = "";
            for (int idT : freeTimeIds) {
                ids = ids + idT + ",";
            }
            teacher.setFreeTimeIds(ids);
        }
//            emptyTimesForTeacherService.findAllByTeachId(id).stream().forEach(time->oldTimeIds.add(time.getTime().getId()));
//            if(!oldTimeIds.equals(freeTimeIds)){
//                emptyTimesForTeacherService.removeAllByTeacherId(teacher.getId());
//                for(int iD : freeTimeIds){
//                    oldTimeIds.remove(Integer.valueOf(iD));
//                    emptyTimesForTeacherService.create(new EmptyTimesForTeacher(teacherService.getById(teacher.getId()), weekService.getById(iD)));
//                }
//
//
//            }
        if (password.equals("OLDPASS")) {
            password = teacherService.getById(teacher.getId()).getPassword();
            Teacher retTeach = teacherMapper.mapTeacherDTOToTeacher(teacher);
            retTeach.setPassword(password);
            teacherService.update(teacher.getId(), retTeach);
            return "redirect:/admin/teacher/" + teacher.getId() + "/edit/TEACHER_EDITED";
        }
        Teacher retTeach = teacherMapper.mapTeacherDTOToTeacher(teacher);
        retTeach.setPassword(passwordEncoder.encode(password));
        teacherService.update(teacher.getId(), retTeach);


        return "redirect:/admin/teacher/" + teacher.getId() + "/edit/TEACHER_EDITED";
    }

    // Deletes or disconnects data at the POST /teacher/{id}/delete route in the "teachers" module.
    // Calls `teacherService.getById`, `lessonService.deleteById`, `emptyTimesForTeacherService.deleteById`, `teacherCourseService.deleteById`, `tswService.findAllByTeachId`, and others; returns "redirect:/admin/teachers" upon completion.
    @PostMapping("/teacher/{id}/delete")
    public String deleteTeacher(@PathVariable("id") int id, Model model) {
        Teacher teacher = teacherService.getById(id);
        for (Lesson lesson : teacher.getLessons()) {
            if (lesson.getLessonTime().isAfter(LocalDateTime.now())) {
                lessonService.deleteById(lesson.getId());
            }
        }
        for (TeacherCourse tc : teacher.getTeacherCourses()) {
            teacherCourseService.deleteById(tc.getId());
        }
        for (TeacherStudentTimeOfTheWeek tsw : tswService.findAllByTeachId(teacher.getId())) {
            tswService.deleteById(tsw.getId());
        }
        teacherService.deleteById(teacher.getId());
        return "redirect:/admin/teachers";
    }

    // Opens the GET /teacher/{teachId}/info route and prepares data for the "closedAdmin/adminTeachers/teacherInfo" template.
    // Adds "teacher", "allEarnings", "ourShare", "freeTimes", "students", "teacherFreeTimes", and others to the Model; retrieves data via `teacherService.getById`, `weekService.getAll`, `courseService.getAll`, `lessonService.findAllByTeachId`, and `studentService.getAll`.
    @GetMapping("/teacher/{teachId}/info")
    public String gotoInfo(@PathVariable("teachId") int id, Model model) {
        Teacher teacher = teacherService.getById(id);
        model.addAttribute("teacher", teacherMapper.mapTeacherToTeacherDTO(teacher));
        List<TimeOfTheWeek> allfreeTimes = weekService.getAll();
        List<Integer> teacherFreeTimes = new ArrayList<>();
        List<Course> allCourses = courseService.getAll();
        List<Integer> days = List.of(7, 1, 2, 3, 4, 5, 6); // Sunday first
        List<String> dayNames = List.of("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");

        ///////////////
        if (teacher.getFreeTimeIds() != null && !teacher.getFreeTimeIds().isEmpty()) {
            teacherFreeTimes = Arrays.stream(teacher.getFreeTimeIds().split(",")).map(Integer::parseInt).toList();
        }
        int[] allTeachersEarnings = {0};
        int[] ourShare = {0};
        lessonService.findAllByTeachId(id).stream().filter(lesson -> lesson.getStatus().equals("WAS")).forEach(lesson -> ourShare[0] = (int) (ourShare[0] + (lesson.getDuration() * lesson.getCourse().getCostPerLesson())));
        lessonService.findAllByTeachId(id).stream().filter(lesson -> lesson.getStatus().equals("WAS")).forEach(lesson -> allTeachersEarnings[0] = (int) (allTeachersEarnings[0] + (lesson.getDuration() * lesson.getCourse().getTeacherShare())));


        ourShare[0] = ourShare[0] - allTeachersEarnings[0];
        model.addAttribute("allEarnings", allTeachersEarnings[0]);
        model.addAttribute("ourShare", ourShare[0]);
        model.addAttribute("freeTimes", allfreeTimes.stream().map(el -> theWeekMapper.mapTTheWeekToTTheWeekDTO(el)).collect(Collectors.toList()));
        model.addAttribute("students", studentService.getAll().stream().map(el -> studentMapper.mapStudentToStudentDTO(el)).collect(Collectors.toList()));
        model.addAttribute("teacherFreeTimes", teacherFreeTimes);
        model.addAttribute("allCourses", allCourses.stream().map(el -> courseMapper.mapCourseToCourseDTO(el)).collect(Collectors.toList()));
        model.addAttribute("hours", Arrays.asList("8:00", "8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30"));
        model.addAttribute("dayNames", dayNames);
        model.addAttribute("days", days);
        //////
        return "closedAdmin/adminTeachers/teacherInfo";
    }

    // Opens the GET /teacher/{teachId}/info/{output} route and prepares data for the "closedAdmin/adminTeachers/teacherInfo" template.
    // Adds "teacher", "output", "allEarnings", "ourShare", "freeTimes", "students", and others to the Model; retrieves data via `teacherService.getById`, `weekService.getAll`, `courseService.getAll`, `lessonService.findAllByTeachId`, and `studentService.getAll`.
    @GetMapping("/teacher/{teachId}/info/{output}")
    public String gotoInfoWithOutput(@PathVariable("teachId") int id, @PathVariable("output") String output, Model
            model) {
        Teacher teacher = teacherService.getById(id);
        model.addAttribute("teacher", teacherMapper.mapTeacherToTeacherDTO(teacher));
        List<TimeOfTheWeek> allfreeTimes = weekService.getAll();
        List<Integer> teacherFreeTimes = new ArrayList<>();
        List<Integer> days = List.of(7, 1, 2, 3, 4, 5, 6); // Sunday first
        List<String> dayNames = List.of("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");
        List<Course> allCourses = courseService.getAll();
        ///////////////
        if (teacher.getFreeTimeIds() != null && !teacher.getFreeTimeIds().isEmpty()) {
            teacherFreeTimes = Arrays.stream(teacher.getFreeTimeIds().split(",")).map(Integer::parseInt).toList();
        }
        int[] allTeachersEarnings = {0};
        int[] ourShare = {0};
        lessonService.findAllByTeachId(id).stream().filter(lesson -> lesson.getStatus().equals("WAS")).forEach(lesson -> ourShare[0] = (int) (ourShare[0] + (lesson.getDuration() * lesson.getCourse().getCostPerLesson())));
        lessonService.findAllByTeachId(id).stream().filter(lesson -> lesson.getStatus().equals("WAS")).forEach(lesson -> allTeachersEarnings[0] = (int) (allTeachersEarnings[0] + (lesson.getDuration() * lesson.getCourse().getTeacherShare())));
        ourShare[0] = ourShare[0] - allTeachersEarnings[0];
        model.addAttribute("output", output);
        model.addAttribute("allEarnings", allTeachersEarnings[0]);
        model.addAttribute("ourShare", ourShare[0]);
        model.addAttribute("freeTimes", allfreeTimes.stream().map(el -> theWeekMapper.mapTTheWeekToTTheWeekDTO(el)).collect(Collectors.toList()));
        model.addAttribute("students", studentService.getAll().stream().map(el -> studentMapper.mapStudentToStudentDTO(el)).collect(Collectors.toList()));
        model.addAttribute("teacherFreeTimes", teacherFreeTimes);
        model.addAttribute("allCourses", allCourses.stream().map(el -> courseMapper.mapCourseToCourseDTO(el)).collect(Collectors.toList()));
        model.addAttribute("hours", Arrays.asList("8:00", "8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30"));
        model.addAttribute("dayNames", dayNames);
        model.addAttribute("days", days);
        //////
        return "closedAdmin/adminTeachers/teacherInfo";
    }

    // Updates data at the POST /teacher/{teachId}/setEarnedMoneyToZero route in the "teachers" module.
    // Calls `teacherService.getById` and `teacherService.update`; returns "redirect:/admin/teacher/" upon completion.
    @PostMapping("/teacher/{teachId}/setEarnedMoneyToZero")
    public String setEarnedMoneyToZero(Model model, @PathVariable("teachId") int id) {
        Teacher teacher = teacherService.getById(id);
        teacher.setUnpaidMoney(0);
        teacherService.update(id, teacher);
        return "redirect:/admin/teacher/" + id + "/info";
    }

    // Updates data at the POST /teacher/{teachId}/setEarnedMoney route in the "teachers" module.
    // Calls `teacherService.getById` and `teacherService.update`; returns "redirect:/admin/teacher/" upon completion.
    @PostMapping("/teacher/{teachId}/setEarnedMoney")
    public String setEarnedMoney(Model model, @PathVariable("teachId") int id,
                                 @ModelAttribute("teacher") TeacherDTO teacherDTO) {
        Teacher teacher = teacherService.getById(id);
        teacher.setUnpaidMoney(teacherDTO.getUnpaidMoney());
        teacherService.update(id, teacher);
        return "redirect:/admin/teacher/" + id + "/info";
    }

    @PostMapping("/teacher/{teachId}/deleteCourse/{courseId}")
    @Transactional
    public String deleteCourse(@PathVariable("courseId") int courseId, @PathVariable("teachId") int teachId) {
        if (courseId != 0 && teachId != 0) {
            teacherCourseService.deleteByCourseIdAndTeacherId(courseId, teachId);
            return "redirect:/admin/teacher/" + teachId + "/edit/TEACHER_EDITED";
        } else {
            return "redirect:/admin/teacher/" + teachId + "/edit/GENERAL";
        }

    }

}
