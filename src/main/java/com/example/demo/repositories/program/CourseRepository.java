package com.example.demo.repositories.program;

import com.example.demo.models.programe.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    public List<Course> findAll();
}
