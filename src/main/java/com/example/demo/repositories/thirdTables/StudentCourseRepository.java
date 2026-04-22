package com.example.demo.repositories.thirdTables;

import com.example.demo.models.entities.Student;
import com.example.demo.models.thirdTables.StudentCourse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentCourseRepository extends JpaRepository<StudentCourse, Integer> {
    public void deleteAllByStudent(Student student);
}
