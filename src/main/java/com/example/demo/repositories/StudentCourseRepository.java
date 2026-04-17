package com.example.demo.repositories;

import com.example.demo.models.Student;
import com.example.demo.models.StudentCourse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentCourseRepository extends JpaRepository<StudentCourse, Integer> {
    public void deleteAllByStudent(Student student);
}
