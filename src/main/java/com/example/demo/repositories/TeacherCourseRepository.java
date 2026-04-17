package com.example.demo.repositories;

import com.example.demo.models.TeacherCourse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherCourseRepository extends JpaRepository<TeacherCourse, Integer> {
}
