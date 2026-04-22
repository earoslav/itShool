package com.example.demo.repositories.entities;

import com.example.demo.models.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
    public boolean existsByEmail(String email);
}
