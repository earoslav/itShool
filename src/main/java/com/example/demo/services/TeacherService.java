package com.example.demo.services;

import com.example.demo.models.Teacher;
import com.example.demo.repositories.TeacherRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TeacherService {
    private final TeacherRepository teacherRepository;

    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
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

    public Teacher update(Integer id, Teacher teacher) {
        Teacher existing = getById(id);
        existing.setName(teacher.getName());
        existing.setAge(teacher.getAge());
        existing.setEmail(teacher.getEmail());
        existing.setPassword(teacher.getPassword());
        existing.setComment(teacher.getComment());
        existing.setPhoneNumber(teacher.getPhoneNumber());
        existing.setTgUsername(teacher.getTgUsername());
        return teacherRepository.save(existing);
    }
    public boolean checkIfExistsByEmail(String email){
        return teacherRepository.existsByEmail(email);
    }
    public void deleteById(Integer id) {
        getById(id);
        teacherRepository.deleteById(id);
    }
}
