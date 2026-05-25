package com.example.demo.services.entities;

import com.example.demo.models.entities.Teacher;
import com.example.demo.models.entities.User;
import com.example.demo.models.programe.Lesson;
import com.example.demo.repositories.entities.TeacherRepository;
import com.example.demo.repositories.entities.UserRepository;
import com.example.demo.services.program.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TeacherService contains business operations for the "teachers" module.
// Controllers call this service to avoid direct interaction with repositories, mappers, and scheduling rules.
@Service
public class TeacherService {
    private final TeacherRepository teacherRepository;

    private UserRepository userRepository;


    // Receives TeacherRepository and UserRepository dependencies through Spring.
    // These services and mappers are required by the class methods to work with the "teachers" module without manual object creation.
    public TeacherService(TeacherRepository teacherRepository,  UserRepository userRepository) {
        this.teacherRepository = teacherRepository;

        this.userRepository = userRepository;
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
    public Teacher findByUserId(int userId){
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
    public boolean checkIfExistsByEmail(String email){
        return userRepository.findByEmailAndRole(email, "TEACHER") != null;
    }
    // Checks if a record in the "teachers" module already exists by a condition: user account.
    // Returns a boolean for creation and editing forms to prevent duplicates or incorrect selections.
    public boolean checkIfExistsByUserId(int userId){
        return teacherRepository.findByUserId(userId) != null;
    }
    // Deletes a record in the "teachers" module by ID.
    // Before calling deleteById, the method reads the entity to ensure the deletion passes through the service layer and fails clearly if the ID is incorrect.
    public void deleteById(Integer id) {
        getById(id);
        teacherRepository.deleteById(id);
    }
}
