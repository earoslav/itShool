package com.example.demo.repositories.thirdTables;

import com.example.demo.models.thirdTables.TeacherCourse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherCourseRepository extends JpaRepository<TeacherCourse, Integer> {
}
