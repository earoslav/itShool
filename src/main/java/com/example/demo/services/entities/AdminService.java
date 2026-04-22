package com.example.demo.services.entities;

import com.example.demo.models.entities.Admin;
import com.example.demo.repositories.entities.AdminRepository;
import org.springframework.stereotype.Service;
@Service
public class AdminService {
    private AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public Admin getAdmin(){
        return adminRepository.findAll().get(0);
    }
}
