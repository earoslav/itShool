package com.example.demo.services.entities;

import com.example.demo.models.entities.Admin;
import com.example.demo.models.entities.Student;
import com.example.demo.repositories.entities.AdminRepository;
import org.springframework.stereotype.Service;
@Service
public class AdminService {
    private AdminRepository adminRepository;
    public Admin update(Integer id, Admin admin) {
        Admin existing = getById(id);
        existing.setName(admin.getName());
        existing.setEmail(admin.getEmail());
        existing.setPassword(admin.getPassword());
        existing = adminRepository.save(existing);
        return existing;
    }

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }
    public Admin getById(int id){
        return adminRepository.getById(id);
    }
    public Admin getAdmin(){
        return adminRepository.findAll().get(0);
    }
}
