package com.example.demo.services.entities;

import com.example.demo.dto.entities.StudentDTO;
import com.example.demo.mapper.entity.StudentMapper;
import com.example.demo.models.entities.Student;
import com.example.demo.models.entities.User;
import com.example.demo.repositories.entities.StudentRepository;
import com.example.demo.repositories.entities.UserRepository;
import com.example.demo.services.other.CommentService;
import com.example.demo.services.program.LessonService;
import com.example.demo.services.security.UserService;
import com.example.demo.services.thirdTable.TeacherStudentTimeOfTheWeekService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.stream.Collectors;

// StudentService contains business operations for the "students" module.
// Controllers call this service to avoid direct interaction with repositories, mappers, and scheduling rules.
@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private StudentMapper studentMapper;
    private TeacherStudentTimeOfTheWeekService tswService;
    private CommentService commentService;
    private LessonService lessonService;
    private UserService userService;
    // Receives StudentRepository and UserRepository dependencies through Spring.
    // These services and mappers are required by the class methods to work with the "students" module without manual object creation.
    public StudentService(StudentRepository studentRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, StudentMapper studentMapper, TeacherStudentTimeOfTheWeekService tswService, CommentService commentService, LessonService lessonService, UserService userService) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.studentMapper = studentMapper;
        this.tswService = tswService;
        this.commentService = commentService;
        this.lessonService = lessonService;
        this.userService = userService;
    }
    // Returns all students from the repository.
    // Lists, calendars, and forms use this method when the entire set of available records needs to be shown.
    public List<Student> getAll() {
        return studentRepository.findAll();
    }
    // Finds a single record in the "students" module by ID.
    // Controllers call this before editing, deleting, or assembling a details page.
    public Student getById(Integer id) {
        return studentRepository.findById(id).get();
    }
    // Saves a new record in the "students" module.
    // This method is called after the controller has collected the entity from a form or a service has generated it automatically.
    public Student create(Student student) {
        return studentRepository.save(student);
    }
    // Checks if a record in the "students" module already exists by a condition: user account.
    // Returns a boolean for creation and editing forms to prevent duplicates or incorrect selections.
    public boolean checkIfExistsByUserId(int userId){
        return studentRepository.findByUserId(userId) != null;
    }
    // Checks if a record in the "students" module already exists by a condition: email.
    // Returns a boolean for creation and editing forms to prevent duplicates or incorrect selections.
    public boolean checkIfExistsByEmail(String email){
        return userRepository.findByEmailAndRole(email, "STUDENT") != null;
    }
    // Searches for records in the "students" module by condition: user account.
    // The actual query is performed by `studentRepository.findByUserId`, and the controller receives the final result.
    public  Student findByUserId(int id){
        return studentRepository.findByUserId(id);
    }
    // Updates student data.
    // The method updates an existing profile with new data and resets the corresponding caches in Redis.
    public Student update(Integer id, Student student) {
        Student existing = getById(id);
        existing.setName(student.getName());
        existing.setAge(student.getAge());
        existing.setEmail(student.getEmail());
        existing.setPhoneNumber(student.getPhoneNumber());
        existing.setPassword(student.getPassword());
        existing.setComment(student.getComment());
        return studentRepository.save(existing);
    }
    // Deletes a record in the "students" module by ID.
    // Before calling deleteById, the method reads the entity to ensure the deletion passes through the service layer and fails clearly if the ID is incorrect.
    public void deleteById(Integer id) {
        getById(id);
        studentRepository.deleteById(id);
    }
    public void manageDeleteStudent(int id){
        Student student = getById(id);
        tswService.removeAllTswByStudentId(student.getId());
        lessonService.deleteAllLessonsByStudent(student);

        commentService.deleteAllByStudent(student);
        deleteById(id);
    }
    public String manageEditStudent(StudentDTO student, String password, int id){
        if (userService.checkIfExistsByEmail(student.getUser().getEmail()) && !getById(id).getEmail().equals(student.getUser().getEmail())) {
            return "redirect:/admin/student/" + id + "/edit/EXISTS";
        }

        Student student1 = studentMapper.mapStudentDTOToStudent(student);
        if (password.equals("OLD_PASS")) {
            password = getById(student.getId()).getPassword();
            student1.setPassword(password);
            update(id, student1);
            return "redirect:/admin/student/" + id + "/edit/STUDENT_EDITED";
        }
        student1.setPassword(passwordEncoder.encode(password));
        update(id, student1);
        return "redirect:/admin/student/" + id + "/edit/STUDENT_EDITED";
    }
    public void manageGoToEditStudent(Model model, String output, int id){
        if(!output.equals("")){
            model.addAttribute("output", output);
        }
        model.addAttribute("password", "");
        model.addAttribute("student", studentMapper.mapStudentToStudentDTO(getById(id)));
    }
    public void manageGetAllStudents(Model model, String output){
        manageGetAllStudents(model, output, "10");
    }
    public void manageGetAllStudents(Model model, String output, String limit){
        String normalizedLimit = normalizeListLimit(limit);
        List<StudentDTO> students = limitStudents(getAll(), normalizedLimit).stream().map(student -> studentMapper.mapStudentToStudentDTO(student)).collect(Collectors.toList());
        model.addAttribute("students", students);
        model.addAttribute("listLimit", normalizedLimit);
        if(!output.equals("")){
            model.addAttribute("output", output);
        }
    }
    public String normalizeListLimit(String limit) {
        return "100".equals(limit) || "all".equals(limit) ? limit : "10";
    }
    public List<Student> limitStudents(List<Student> students, String limit) {
        String normalizedLimit = normalizeListLimit(limit);
        if ("all".equals(normalizedLimit)) {
            return students;
        }
        return students.stream().limit(Integer.parseInt(normalizedLimit)).collect(Collectors.toList());
    }
    public String manageAddStudent(StudentDTO student, String password, Model model){
        if (checkIfExistsByEmail(student.getUser().getEmail())) {
            model.addAttribute("output", "EXISTS");
            model.addAttribute("student", student);
            return "closedAdmin/adminStudents/addNewStudent";
        }
        if (password.equals("NO_PASS")) {
            model.addAttribute("student");
            model.addAttribute("output", "NO_PASS");
            return "closedAdmin/adminStudents/addNewStudent";
        }
        User user = new User(student.getUser().getName(), student.getUser().getEmail(), passwordEncoder.encode(password), "STUDENT");
        userService.create(user);
        Student student1 = studentMapper.mapStudentDTOToStudent(student);
        student1.setUser(user);
        create(student1);
        return "redirect:/admin/homepage/STUDENT_ADDED";
    }

    public void manageGoToStudentHomepage(int id, String output, Model model) {
        Student student = id == 0 ? new Student() : getById(id);
        if (output != null && !output.equals("")) {
            model.addAttribute("output", output);
        }
        model.addAttribute("password", "");
        model.addAttribute("student", studentMapper.mapStudentToStudentDTO(student));
    }

    public String manageUpdateStudentHomepage(StudentDTO student, String password, int id) {
        if (userService.checkIfExistsByEmail(student.getUser().getEmail()) && !getById(id).getEmail().equals(student.getUser().getEmail())) {
            return "redirect:/student/" + id + "/homepage/exists";
        } else {
            if (password.equals("OLD_PASS")) {
                password = getById(id).getPassword();
                Student retStudent = studentMapper.mapStudentDTOToStudent(student);
                retStudent.setPassword(password);
                update(id, retStudent);
                return "redirect:/student/" + id + "/homepage/studentEdited";
            }
            Student retStudent = studentMapper.mapStudentDTOToStudent(student);
            retStudent.setPassword(passwordEncoder.encode(password));
            update(id, retStudent);
        }
        return "redirect:/student/" + id + "/homepage/studentEdited";
    }

    public String manageSignUpAsStudent(StudentDTO student, String password) {
        if (password.equals("NO_PASS")) {
            return "redirect:/openSource/signUpAsStudent/NO_PASS";
        }
        if (!checkIfExistsByEmail(student.getUser().getEmail())) {
            Student student1 = studentMapper.mapStudentDTOToStudent(student);

            User user = new User(student.getUser().getName(), student.getUser().getEmail(), passwordEncoder.encode(password), "STUDENT");
            userService.create(user);
            student1.setUser(user);
            create(student1);
            return "redirect:/openSource/homepage/STUDENT_ADDED";
        } else {
            return "redirect:/openSource/signUpAsStudent/EXISTS";
        }
    }
}
