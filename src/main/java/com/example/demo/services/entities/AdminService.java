package com.example.demo.services.entities;

import com.example.demo.models.entities.Admin;
import com.example.demo.repositories.entities.AdminRepository;
import org.springframework.stereotype.Service;
// AdminService contains business operations for the "administrators" module.
// Controllers call this service to avoid direct interaction with repositories, mappers, and scheduling rules.
@Service
public class AdminService {
    private AdminRepository adminRepository;

    // Receives AdminRepository dependency through Spring.
    // These services and mappers are required by the class methods to work with the "administrators" module without manual object creation.
    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }
    // Updates an existing record in the "administrators" module.
    // First finds the current entity, transfers name, email, and password fields, then saves it back to the repository.
    public Admin update(Integer id, Admin admin) {
        Admin existing = getById(id);
        existing.setName(admin.getName());
        existing.setEmail(admin.getEmail());
        existing.setPassword(admin.getPassword());
        return adminRepository.save(existing);
    }
    // Finds a single record in the "administrators" module by ID.
    // Controllers call this before editing, deleting, or assembling a details page.
    public Admin getById(int id){
        return adminRepository.getById(id);
    }
    // Returns the first administrator from the Admin table.
    // This record is used as a service administrator for pages and emails where school data is required.
    public Admin getAdmin(){
        return adminRepository.findAll().get(0);
    }
}
