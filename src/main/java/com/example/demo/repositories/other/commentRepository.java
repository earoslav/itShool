package com.example.demo.repositories.other;

import com.example.demo.models.other.Comment;
import com.example.demo.models.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface commentRepository extends JpaRepository<Comment, Integer> {
    public void deleteAllByStudent(Student student);
}
