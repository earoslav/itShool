package com.example.demo.services.entities;

import com.example.demo.models.entities.Student;
import com.example.demo.repositories.entities.StudentRepository;
import java.util.List;

import com.example.demo.repositories.entities.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private UserRepository userRepository;

    public StudentService(StudentRepository studentRepository, UserRepository userRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
    }

    public List<Student> getAll() {
        return studentRepository.findAll();
    }

    public Student getById(Integer id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
    }

    public Student create(Student student) {
        return studentRepository.save(student);
    }
    public boolean checkIfExistsByUserId(int userId){
        return studentRepository.findByUserId(userId)!=null;
    }
    public boolean checkIfExistsByEmail(String email){
        if(userRepository.findByEmailAndRole(email, "STUDENT")!=null){
            return true;
        }
        return false;
    }
    public  Student findByUserId(int id){
        return studentRepository.findByUserId(id);
    }
    public Student update(Integer id, Student student) {
        Student existing = getById(id);
        existing.setName(student.getName());
        existing.setAge(student.getAge());
        existing.setEmail(student.getEmail());
        existing.setPhoneNumber(student.getPhoneNumber());
        existing.setPassword(student.getPassword());
        existing.setComment(student.getComment());
        existing = studentRepository.save(existing);
        return existing;
    }

    public void deleteById(Integer id) {
        getById(id);
        studentRepository.deleteById(id);
    }
}
