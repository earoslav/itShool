package com.example.demo.services.entities;

import com.example.demo.dto.adminLessons.LessonAdminLessonsDTO;
import com.example.demo.dto.entities.TeacherDTO;
import com.example.demo.mapper.entity.StudentMapper;
import com.example.demo.mapper.entity.TeacherMapper;
import com.example.demo.mapper.other.TimeOfTheWeekMapper;
import com.example.demo.mapper.programe.CourseMapper;
import com.example.demo.models.entities.Student;
import com.example.demo.models.entities.Teacher;
import com.example.demo.models.entities.User;
import com.example.demo.models.other.TimeOfTheWeek;
import com.example.demo.models.programe.Course;
import com.example.demo.models.programe.Lesson;
import com.example.demo.models.thirdTables.TeacherCourse;
import com.example.demo.models.thirdTables.TeacherStudentTimeOfTheWeek;
import com.example.demo.repositories.entities.TeacherRepository;
import com.example.demo.repositories.entities.UserRepository;
import com.example.demo.services.Statuses;
import com.example.demo.services.email.MailService;
import com.example.demo.services.other.TimeOfTheWeekService;
import com.example.demo.services.program.CourseService;
import com.example.demo.services.program.LessonService;
import com.example.demo.services.security.UserService;
import com.example.demo.services.thirdTable.TeacherCourseService;
import com.example.demo.services.thirdTable.TeacherStudentTimeOfTheWeekService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

// TeacherService contains business operations for the "teachers" module.
// Controllers call this service to avoid direct interaction with repositories, mappers, and scheduling rules.
@Service
public class TeacherService {
    private final TeacherRepository teacherRepository;

    private UserRepository userRepository;
    private LessonService lessonService;
    private TimeOfTheWeekService theWeekService;
    private StudentService studentService;
    private StudentMapper studentMapper;
    private CourseMapper courseMapper;
    private TeacherMapper teacherMapper;
    private MailService mailService;
    private CourseService courseService;
    private TimeOfTheWeekService weekService;
    private TimeOfTheWeekMapper theWeekMapper;
    private PasswordEncoder passwordEncoder;
    private UserService userService;
    private TeacherCourseService teacherCourseService;
    private TeacherStudentTimeOfTheWeekService tswService;
    private AdminService adminService;

    // Receives TeacherRepository and UserRepository dependencies through Spring.
    // These services and mappers are required by the class methods to work with the "teachers" module without manual object creation.
    public TeacherService(TeacherRepository teacherRepository, UserRepository userRepository, LessonService lessonService, TimeOfTheWeekService theWeekService, StudentService studentService, StudentMapper studentMapper, CourseMapper courseMapper, TeacherMapper teacherMapper, MailService mailService, CourseService courseService, TimeOfTheWeekService weekService, TimeOfTheWeekMapper theWeekMapper, PasswordEncoder passwordEncoder, UserService userService, TeacherCourseService teacherCourseService, TeacherStudentTimeOfTheWeekService tswService, AdminService adminService) {
        this.teacherRepository = teacherRepository;

        this.userRepository = userRepository;
        this.lessonService = lessonService;
        this.theWeekService = theWeekService;
        this.studentService = studentService;
        this.studentMapper = studentMapper;

        this.courseMapper = courseMapper;
        this.teacherMapper = teacherMapper;
        this.mailService = mailService;
        this.courseService = courseService;
        this.weekService = weekService;

        this.theWeekMapper = theWeekMapper;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.teacherCourseService = teacherCourseService;
        this.tswService = tswService;
        this.adminService = adminService;
    }

    // Returns all teachers from the repository.
    // Lists, calendars, and forms use this method when the entire set of available records needs to be shown.
    public List<Teacher> getAll() {
        return teacherRepository.findAll();
    }

    // Finds a single record in the "teachers" module by ID.
    // Controllers call this before editing, deleting, or assembling a details page.
    public Teacher getById(Integer id) {
        return teacherRepository.findById(id).get();
    }

    // Saves a new record in the "teachers" module.
    // This method is called after the controller has collected the entity from a form or a service has generated it automatically.
    public Teacher create(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    // Searches for records in the "teachers" module by condition: user account.
    // The actual query is performed by `teacherRepository.findByUserId`, and the controller receives the final result.
    public Teacher findByUserId(int userId) {
        return teacherRepository.findByUserId(userId);
    }

    // Updates an existing record in the "teachers" module.
    // First finds the current entity, transfers name, age, email, password, comment, phoneNumber, and unpaidMoney fields, then saves it back to the repository.
    public Teacher update(Integer id, Teacher teacher) {
        Teacher existing = getById(id);
        existing.setName(teacher.getName());
        existing.setAge(teacher.getAge());
        existing.setEmail(teacher.getEmail());
        existing.setPassword(teacher.getPassword());
        existing.setComment(teacher.getComment());
        existing.setPhoneNumber(teacher.getPhoneNumber());
        existing.setUnpaidMoney(teacher.getUnpaidMoney());
        existing.setTgUsername(teacher.getTgUsername());
        existing.setFreeTimeIds(teacher.getFreeTimeIds());
        return teacherRepository.save(existing);
    }

    // Checks if a record in the "teachers" module already exists by a condition: email.
    // Returns a boolean for creation and editing forms to prevent duplicates or incorrect selections.
    public boolean checkIfExistsByEmail(String email) {
        return userRepository.findByEmailAndRole(email, "TEACHER") != null;
    }

    // Checks if a record in the "teachers" module already exists by a condition: user account.
    // Returns a boolean for creation and editing forms to prevent duplicates or incorrect selections.
    public boolean checkIfExistsByUserId(int userId) {
        return teacherRepository.findByUserId(userId) != null;
    }

    // Deletes a record in the "teachers" module by ID.
    // Before calling deleteById, the method reads the entity to ensure the deletion passes through the service layer and fails clearly if the ID is incorrect.
    public void deleteById(Integer id) {
        getById(id);
        teacherRepository.deleteById(id);
    }

    public void manageGoToCreateTeacherFromEmail(Model model, String name,
                                                 int age,
                                                 String email,
                                                 String password,
                                                 String comment,
                                                 String tgUser,
                                                 String phonenumber,
                                                 String courseIds,
                                                 String timeIds) {
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
    }

    public String manageAddTeacher(TeacherDTO teacher,
                                  String password,
                                  List<Integer> coursesIds,
                                  List<Integer> freeTimesIds, Model model) {
        model.addAttribute("teacher", teacher);
        if (userService.checkIfExistsByEmail(teacher.getUser().getEmail())) {
            return "EXISTS";
        }
        if (coursesIds == null || coursesIds.isEmpty()) {
            return "NO_COURSES";
        }
        if (freeTimesIds == null || freeTimesIds.isEmpty()) {
            return "NO_FREE_TIMES";

        }
        Teacher teacher1 = teacherMapper.mapTeacherDTOToTeacher(teacher);
        if (password.equals("NO_PASS")) {
            return "NO_PASS";
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
        create(teacher1);
        List<Course> courses = courseService.getCoursesThroughIds(coursesIds);

        for (Course course : courses) {
            TeacherCourse teacherCourse = new TeacherCourse(course, teacher1);
            teacherCourseService.create(teacherCourse);
        }


        return "TEACHER_ADDED";
    }

    public void manageGoToCreateTeacher(Model model, String output) {
        List<Integer> days = List.of(7, 1, 2, 3, 4, 5, 6); // Sunday first
        List<String> dayNames = List.of("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");

        List<TimeOfTheWeek> freeTimes = weekService.getAll();
        List<Course> courses = courseService.getAll();
        model.addAttribute("teacher", teacherMapper.mapTeacherToTeacherDTO(new Teacher()));
        if (!output.equals("")) {
            model.addAttribute("output", output);
        }
        model.addAttribute("days", days);
        model.addAttribute("dayNames", dayNames);
        model.addAttribute("hours", Arrays.asList("8:00", "8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30"));
        model.addAttribute("courses", courses.stream().map(el -> courseMapper.mapCourseToCourseDTO(el)).collect(Collectors.toList()));
        model.addAttribute("freeTimes", freeTimes.stream().map(el -> theWeekMapper.mapTTheWeekToTTheWeekDTO(el)).collect(Collectors.toList()));
    }

    public void manageGoToTeacherStudentLessons(int idTeach, int idSt, String output, Model model, Integer days) throws JsonProcessingException {
        int lessonDays = lessonService.normalizeLessonDays(days);
        Teacher teacher = getById(idTeach);
        Student student = studentService.getById(idSt);

        HashMap<String, LessonAdminLessonsDTO> lessonHashMap = lessonService.compileLessonsForStudentAndTeacher(idTeach, student.getId(), lessonDays);
        List<String> weekDays = theWeekService.compileWeekDays(lessonDays);
        List<Course> courses = new ArrayList<>();
        teacher.getTeacherCourses().stream().forEach(course -> courses.add(course.getCourse()));
        if (!output.equals("")) {
            model.addAttribute("output", output);
        }
        model.addAttribute("lessons", lessonHashMap);
        model.addAttribute("courses", courses.stream().map(el -> courseMapper.mapCourseToCourseDTO(el)).collect(Collectors.toList()));
        model.addAttribute("weekDays", weekDays);
        model.addAttribute("lessonDays", lessonDays);
        model.addAttribute("teacher", teacherMapper.mapTeacherToTeacherDTO(teacher));
        model.addAttribute("student", studentMapper.mapStudentToStudentDTO(student));
        model.addAttribute("hours", Arrays.asList("8:00", "8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30"));
    }

    public void manageGoToTeacherLessons(int id, Model model, String output, Integer days) throws JsonProcessingException {
        int lessonDays = lessonService.normalizeLessonDays(days);
        Teacher teacher = getById(id);
        HashMap<String, LessonAdminLessonsDTO> lessonHashMap = lessonService.compileLessonsForTeacher(teacher.getId(), lessonDays);
        List<String> weekDays = theWeekService.compileWeekDays(lessonDays);
        List<Course> courses = new ArrayList<>();
        teacher.getTeacherCourses().stream().forEach(course -> courses.add(course.getCourse()));

        List<Student> students = studentService.getAll();

        if (!output.equals("")) {
            model.addAttribute("output", output);
        }
        model.addAttribute("lessons", lessonHashMap);
        model.addAttribute("students", students.stream().map(el -> studentMapper.mapStudentToStudentDTO(el)).collect(Collectors.toList()));
        model.addAttribute("courses", courses.stream().map(el -> courseMapper.mapCourseToCourseDTO(el)).collect(Collectors.toList()));
        model.addAttribute("weekDays", weekDays);
        model.addAttribute("lessonDays", lessonDays);
        model.addAttribute("teacher", teacherMapper.mapTeacherToTeacherDTO(teacher));
        model.addAttribute("hours", Arrays.asList("8:00", "8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30"));
    }

    public void manageGotoTeachers(Model model) {
        manageGotoTeachers(model, "10");
    }

    public void manageGotoTeachers(Model model, String limit) {
        String normalizedLimit = normalizeListLimit(limit);
        model.addAttribute("teachers", limitTeachers(getAll(), normalizedLimit).stream()
                .map(teacher -> teacherMapper.mapTeacherToTeacherDTO(teacher))
                .collect(Collectors.toList()));
        model.addAttribute("listLimit", normalizedLimit);
    }

    public String normalizeListLimit(String limit) {
        return "100".equals(limit) || "all".equals(limit) ? limit : "10";
    }

    private List<Teacher> limitTeachers(List<Teacher> teachers, String limit) {
        String normalizedLimit = normalizeListLimit(limit);
        if ("all".equals(normalizedLimit)) {
            return teachers;
        }
        return teachers.stream().limit(Integer.parseInt(normalizedLimit)).collect(Collectors.toList());
    }

    public void manageGoToEditTeacher(int id, String output, Model model) {
        Teacher teacher = getById(id);
        teacher.setPassword("");
        model.addAttribute("teacher", teacherMapper.mapTeacherToTeacherDTO(teacher));
        List<TimeOfTheWeek> allfreeTimes = weekService.getAll();
        List<Integer> teacherFreeTimes = new ArrayList<>();
        List<Course> allCourses = courseService.getAll();
        List<Integer> days = List.of(7, 1, 2, 3, 4, 5, 6); // Sunday first
        List<String> dayNames = List.of("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");

        if (teacher.getFreeTimeIds() != null && !teacher.getFreeTimeIds().isEmpty()) {
            teacherFreeTimes = Arrays.stream(teacher.getFreeTimeIds().split(",")).map(Integer::parseInt).toList();
        }

        if (output != null && !output.equals("")) {
            model.addAttribute("output", output);
        }
        model.addAttribute("freeTimes", allfreeTimes.stream().map(el -> theWeekMapper.mapTTheWeekToTTheWeekDTO(el)).collect(Collectors.toList()));
        model.addAttribute("teacherFreeTimes", teacherFreeTimes);
        model.addAttribute("allCourses", allCourses.stream().map(el -> courseMapper.mapCourseToCourseDTO(el)).collect(Collectors.toList()));
        model.addAttribute("hours", Arrays.asList("8:00", "8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30"));
        model.addAttribute("dayNames", dayNames);
        model.addAttribute("days", days);
        model.addAttribute("password", "");
    }

    @org.springframework.transaction.annotation.Transactional
    public String manageUpdateTeacher(int id, TeacherDTO teacher, String password, List<Integer> freeTimeIds, List<Integer> newCourseIds) {
        if (checkIfExistsByEmail(teacher.getUser().getEmail()) && !getById(teacher.getId()).getEmail().equals(teacher.getUser().getEmail())) {
            return "EXISTS";
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
        String teachIds = getById(id).getFreeTimeIds();
        if (teachIds != null && !teachIds.equals("")) {
            oldTimeIds = Arrays.stream(getById(id).getFreeTimeIds().split(",")).map(Integer::parseInt).toList();
        }
        if (!oldTimeIds.equals(freeTimeIds)) {
            String ids = "";
            for (int idT : freeTimeIds) {
                ids = ids + idT + ",";
            }
            teacher.setFreeTimeIds(ids);
        }
        if (password.equals("OLD_PASS")) {
            password = getById(teacher.getId()).getPassword();
            Teacher retTeach = teacherMapper.mapTeacherDTOToTeacher(teacher);
            retTeach.setPassword(password);
            update(teacher.getId(), retTeach);
            return "TEACHER_EDITED";
        }
        Teacher retTeach = teacherMapper.mapTeacherDTOToTeacher(teacher);
        retTeach.setPassword(passwordEncoder.encode(password));
        update(teacher.getId(), retTeach);

        return "TEACHER_EDITED";
    }

    @org.springframework.transaction.annotation.Transactional
    public void manageDeleteTeacher(int id) {
        Teacher teacher = getById(id);
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
        deleteById(teacher.getId());
    }

    public void manageGoToInfo(int id, String output, Model model) {
        Teacher teacher = getById(id);
        model.addAttribute("teacher", teacherMapper.mapTeacherToTeacherDTO(teacher));
        List<TimeOfTheWeek> allfreeTimes = weekService.getAll();
        List<Integer> teacherFreeTimes = new ArrayList<>();
        List<Course> allCourses = courseService.getAll();
        List<Integer> days = List.of(7, 1, 2, 3, 4, 5, 6); // Sunday first
        List<String> dayNames = List.of("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");

        if (teacher.getFreeTimeIds() != null && !teacher.getFreeTimeIds().isEmpty()) {
            teacherFreeTimes = Arrays.stream(teacher.getFreeTimeIds().split(",")).map(Integer::parseInt).toList();
        }
        int[] allTeachersEarnings = {0};
        int[] ourShare = {0};
        lessonService.findAllByTeachId(id).stream().filter(lesson -> lesson.getStatus().equals("WAS")).forEach(lesson -> ourShare[0] = (int) (ourShare[0] + (lesson.getDuration() * lesson.getCourse().getCostPerLesson())));
        lessonService.findAllByTeachId(id).stream().filter(lesson -> lesson.getStatus().equals("WAS")).forEach(lesson -> allTeachersEarnings[0] = (int) (allTeachersEarnings[0] + (lesson.getDuration() * lesson.getCourse().getTeacherShare())));

        ourShare[0] = ourShare[0] - allTeachersEarnings[0];
        if (output != null && !output.equals("")) {
            model.addAttribute("output", output);
        }
        model.addAttribute("allEarnings", allTeachersEarnings[0]);
        model.addAttribute("ourShare", ourShare[0]);
        model.addAttribute("freeTimes", allfreeTimes.stream().map(el -> theWeekMapper.mapTTheWeekToTTheWeekDTO(el)).collect(Collectors.toList()));
        model.addAttribute("students", studentService.getAll().stream().map(el -> studentMapper.mapStudentToStudentDTO(el)).collect(Collectors.toList()));
        model.addAttribute("teacherFreeTimes", teacherFreeTimes);
        model.addAttribute("allCourses", allCourses.stream().map(el -> courseMapper.mapCourseToCourseDTO(el)).collect(Collectors.toList()));
        model.addAttribute("hours", Arrays.asList("8:00", "8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30"));
        model.addAttribute("dayNames", dayNames);
        model.addAttribute("days", days);
    }

    public void manageSetEarnedMoneyToZero(int id) {
        Teacher teacher = getById(id);
        teacher.setUnpaidMoney(0);
        update(id, teacher);
    }

    public void manageSetEarnedMoney(int id, TeacherDTO teacherDTO) {
        Teacher teacher = getById(id);
        teacher.setUnpaidMoney(teacherDTO.getUnpaidMoney());
        update(id, teacher);
    }

    @org.springframework.transaction.annotation.Transactional
    public String manageDeleteCourse(int courseId, int teachId) {
        if (courseId != 0 && teachId != 0) {
            teacherCourseService.deleteByCourseIdAndTeacherId(courseId, teachId);
            return "TEACHER_EDITED";
        } else {
            return "GENERAL";
        }
    }

    public void manageGotoTeacherHomepage(int id, String output, Model model) {
        Teacher teacher = getById(id);
        teacher.setPassword("");
        model.addAttribute("teacher", teacherMapper.mapTeacherToTeacherDTO(teacher));
        model.addAttribute("password", "");
        List<TimeOfTheWeek> allfreeTimes = weekService.getAll();
        List<Integer> teacherFreeTimes = new ArrayList<>();
        List<Course> allCourses = courseService.getAll();
        List<Integer> days = List.of(7, 1, 2, 3, 4, 5, 6); // Sunday first
        List<String> dayNames = List.of("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");
        if (teacher.getFreeTimeIds() != null && !teacher.getFreeTimeIds().isEmpty()) {
            teacherFreeTimes = Arrays.stream(teacher.getFreeTimeIds().split(",")).map(Integer::parseInt).toList();
        }
        if (output != null && !output.equals("")) {
            model.addAttribute("output", output);
        }
        model.addAttribute("freeTimes", allfreeTimes.stream().map(el -> theWeekMapper.mapTTheWeekToTTheWeekDTO(el)).collect(Collectors.toList()));
        model.addAttribute("teacherFreeTimes", teacherFreeTimes);
        model.addAttribute("allCourses", allCourses.stream().map(el -> courseMapper.mapCourseToCourseDTO(el)).collect(Collectors.toList()));
        model.addAttribute("hours", Arrays.asList("8:00", "8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30"));
        model.addAttribute("dayNames", dayNames);
        model.addAttribute("days", days);
    }

    public void manageGotoTeacherCourses(int id, Model model) {
        TeacherDTO teacher = teacherMapper.mapTeacherToTeacherDTO(getById(id));
        model.addAttribute("teacher", teacher);
        model.addAttribute("courses", courseService.getAll().stream().map(curs-> courseMapper.mapCourseToCourseDTO(curs)).toList());
    }

    public void manageGotoMyStudents(int id, String output, Model model) {
        manageGotoMyStudents(id, output, model, "10");
    }

    public void manageGotoMyStudents(int id, String output, Model model, String limit) {
        String normalizedLimit = studentService.normalizeListLimit(limit);
        HashSet<Student> students = new HashSet<>();
        tswService.findAllByTeachId(id).stream().forEach(obj -> students.add(obj.getStudent()));
        List<Student> retStudents = studentService.limitStudents(students.stream().sorted(Comparator.comparing(Student::getId)).toList(), normalizedLimit);
        if (output != null && !output.equals("")) {
            model.addAttribute("output", output);
        }
        model.addAttribute("students", retStudents.stream().map(el -> studentMapper.mapStudentToStudentDTO(el)).collect(Collectors.toList()));
        model.addAttribute("listLimit", normalizedLimit);
        model.addAttribute("teacher", teacherMapper.mapTeacherToTeacherDTO(getById(id)));
    }

    @org.springframework.transaction.annotation.Transactional
    public void manageDeleteStudentByTeacher(int idT, int idSt, Model model) throws MessagingException {
        List<Lesson> lessons = lessonService.findAllByIdTandIdSt(idT, idSt);
        lessons.stream().filter(lesson -> lesson.getLessonTime().isAfter(LocalDateTime.now())).forEach(lesson -> lessonService.deleteById(lesson.getId()));
        tswService.removeAllByTidAndSid(idT, idSt);
        Teacher saved = getById(idT);
        mailService.sendEmailWithThymeleafToStudentAboutTeacherRemover(teacherMapper.mapTeacherToTeacherDTO(saved), "Lessons cancellation notification", Collections.singletonList(studentService.getById(idSt).getEmail()));
        model.addAttribute("students", studentMapper.mapStudentToStudentDTO(studentService.getById(idSt)));
        model.addAttribute("teacher", teacherMapper.mapTeacherToTeacherDTO(getById(idT)));
    }

    public String manageSignUpAsTeacher(TeacherDTO teacher, String password, List<Integer> courses, List<Integer> freeTimes) throws MessagingException {
        if (checkIfExistsByEmail(teacher.getUser().getEmail())) {
            return "EXISTS";
        }
        if (password.equals("NO_PASS")) {
            return "NO_PASS";
        }
        teacher.setApproved(0);

        List<Course> selectedCourses = new ArrayList<>();
        List<TimeOfTheWeek> selectedFreeTimes = new ArrayList<>();

        if (freeTimes != null) {
            freeTimes.forEach(freeTime -> selectedFreeTimes.add(weekService.getById(freeTime)));
        }
        if (courses != null) {
            courses.forEach(course -> selectedCourses.add(courseService.getById(course)));
        }

        HashMap<String, List<String>> compiledTimes = weekService.compileTimes(selectedFreeTimes);

        List<Integer> timesIds = new ArrayList<>();
        selectedFreeTimes.forEach(time -> timesIds.add(time.getId()));

        List<Integer> coursesIds = new ArrayList<>();
        selectedCourses.forEach(course -> coursesIds.add(course.getId()));

        mailService.sendEmailWithThymeleaf(teacher, password, selectedFreeTimes.stream().map(time -> theWeekMapper.mapTTheWeekToTTheWeekDTO(time)).collect(Collectors.toList()), selectedCourses.stream().map(course -> courseMapper.mapCourseToCourseDTO(course)).collect(Collectors.toList()), compiledTimes, timesIds.toString(), coursesIds.toString(), "New teacher application — IT Kids School", Collections.singletonList(adminService.getAdmin().getEmail()));
        return "TEACHER_ADDED";
    }

    public void manageGoToSignUpAsTeacher(String output, Model model) {
        manageGoToCreateTeacher(model, output);
        model.addAttribute("password", "");
    }
}
