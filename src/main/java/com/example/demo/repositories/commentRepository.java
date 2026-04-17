package com.example.demo.repositories;

import com.example.demo.models.Comment;
import com.example.demo.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface commentRepository extends JpaRepository<Comment, Integer> {
    public void deleteAllByStudent(Student student);
}
