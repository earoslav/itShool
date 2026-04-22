package com.example.demo.repositories.entities;

import com.example.demo.models.entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    public List<Admin> findAll();
}
