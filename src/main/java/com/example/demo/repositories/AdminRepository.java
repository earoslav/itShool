package com.example.demo.repositories;

import com.example.demo.models.Admin;
import com.example.demo.models.EmptyTimesForTeacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    public List<Admin> findAll();
}
