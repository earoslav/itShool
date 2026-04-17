package com.example.demo.services;

import com.example.demo.models.Student;
import com.example.demo.models.StudentCourse;
import com.example.demo.repositories.StudentCourseRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class StudentCourseService {
    private final StudentCourseRepository studentCourseRepository;

    public StudentCourseService(StudentCourseRepository studentCourseRepository) {
        this.studentCourseRepository = studentCourseRepository;
    }

    public List<StudentCourse> getAll() {
        return studentCourseRepository.findAll();
    }

    public StudentCourse getById(Integer id) {
        return studentCourseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("StudentCourse not found with id: " + id));
    }
    public void deleteAllCoursesByStudent(Student student){
        studentCourseRepository.deleteAllByStudent(student);
    }
    public StudentCourse create(StudentCourse studentCourse) {
        return studentCourseRepository.save(studentCourse);
    }

    public StudentCourse update(Integer id, StudentCourse studentCourse) {
        StudentCourse existing = getById(id);
        existing.setCourse(studentCourse.getCourse());
        existing.setStudent(studentCourse.getStudent());
        return studentCourseRepository.save(existing);
    }

    public void deleteById(Integer id) {
        getById(id);
        studentCourseRepository.deleteById(id);
    }
}
