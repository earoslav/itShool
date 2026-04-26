package com.example.demo.services.entities;

import com.example.demo.models.entities.Teacher;
import com.example.demo.repositories.entities.TeacherRepository;
import java.util.List;

import com.example.demo.repositories.entities.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private UserRepository userRepository;

    public TeacherService(TeacherRepository teacherRepository, UserRepository userRepository) {
        this.teacherRepository = teacherRepository;
        this.userRepository = userRepository;
    }

    public List<Teacher> getAll() {
        return teacherRepository.findAll();
    }

    public Teacher getById(Integer id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + id));
    }

    public Teacher create(Teacher teacher) {
        return teacherRepository.save(teacher);
    }
    public Teacher findByUserId(int userId){
        return teacherRepository.findByUserId(userId);
    }
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
        return teacherRepository.save(existing);
    }
    public boolean checkIfExistsByEmail(String email){
        return userRepository.findByEmailAndRole(email, "TEACHER")!=null;
    }
    public boolean checkIfExistsByUserId(int userId){
        return teacherRepository.findByUserId(userId)!=null;
    }
    public void deleteById(Integer id) {
        getById(id);
        teacherRepository.deleteById(id);
    }
}
