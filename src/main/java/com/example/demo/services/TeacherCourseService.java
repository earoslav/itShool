package com.example.demo.services;

import com.example.demo.models.TeacherCourse;
import com.example.demo.repositories.TeacherCourseRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TeacherCourseService {
    private final TeacherCourseRepository teacherCourseRepository;

    public TeacherCourseService(TeacherCourseRepository teacherCourseRepository) {
        this.teacherCourseRepository = teacherCourseRepository;
    }

    public List<TeacherCourse> getAll() {
        return teacherCourseRepository.findAll();
    }

    public TeacherCourse getById(Integer id) {
        return teacherCourseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TeacherCourse not found with id: " + id));
    }

    public TeacherCourse create(TeacherCourse teacherCourse) {
        return teacherCourseRepository.save(teacherCourse);
    }

    public TeacherCourse update(Integer id, TeacherCourse teacherCourse) {
        TeacherCourse existing = getById(id);
        existing.setCourse(teacherCourse.getCourse());
        existing.setTeacher(teacherCourse.getTeacher());
        return teacherCourseRepository.save(existing);
    }

    public void deleteById(Integer id) {
        getById(id);
        teacherCourseRepository.deleteById(id);
    }
}
