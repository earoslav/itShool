package com.example.demo.services.entities;

import com.example.demo.models.entities.Student;
import com.example.demo.models.entities.User;
import com.example.demo.repositories.entities.StudentRepository;
import com.example.demo.repositories.entities.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

// StudentService contains business operations for the "students" module.
// Controllers call this service to avoid direct interaction with repositories, mappers, and scheduling rules.
@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private UserRepository userRepository;
    // Receives StudentRepository and UserRepository dependencies through Spring.
    // These services and mappers are required by the class methods to work with the "students" module without manual object creation.
    public StudentService(StudentRepository studentRepository, UserRepository userRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
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
}
