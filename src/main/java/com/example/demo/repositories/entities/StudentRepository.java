package com.example.demo.repositories.entities;

import com.example.demo.models.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface StudentRepository extends JpaRepository<Student, Integer> {
    public Student findByEmail(String email);
    public Student findByUserId(int userId);

}
