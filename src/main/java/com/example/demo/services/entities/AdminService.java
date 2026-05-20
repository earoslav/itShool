package com.example.demo.services.entities;

import com.example.demo.dto.entities.AdminDTO;
import com.example.demo.mapper.entity.AdminMapper;
import com.example.demo.models.entities.Admin;
import com.example.demo.repositories.entities.AdminRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
// AdminService contains business operations for the "administrators" module.
// Controllers call this service to avoid direct interaction with repositories, mappers, and scheduling rules.
@Service
public class AdminService {
    private AdminRepository adminRepository;
    private RedisTemplate<String, Object> redisTemplate;
    private ObjectMapper objectMapper;
    private AdminMapper adminMapper;

    // Updates an existing record in the "administrators" module.
    // First finds the current entity, transfers name, email, and password fields, then saves it back to the repository.
    public Admin update(Integer id, Admin admin) {
        Admin existing = getById(id);
        existing.setName(admin.getName());
        existing.setEmail(admin.getEmail());
        existing.setPassword(admin.getPassword());
        existing = adminRepository.save(existing);
        AdminDTO adminDTO = adminMapper.mapAdminToAdminDTO(existing);
        redisTemplate.opsForValue().set("adminById" + id, adminDTO);
        redisTemplate.opsForValue().set("admin", adminDTO);
        return existing;
    }
    // Receives AdminRepository dependency through Spring.
    // These services and mappers are required by the class methods to work with the "administrators" module without manual object creation.
    public AdminService(AdminRepository adminRepository, @Qualifier("schoolRedisTemplate") RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper, AdminMapper adminMapper) {
        this.adminRepository = adminRepository;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.adminMapper = adminMapper;
    }
    // Finds a single record in the "administrators" module by ID.
    // Controllers call this before editing, deleting, or assembling a details page.
    public Admin getById(int id){
        Admin admin = new Admin();
        if(redisTemplate.opsForValue().get("adminById" + id) == null){
            admin = adminRepository.getById(id);
            redisTemplate.opsForValue().set("adminById" + id, adminMapper.mapAdminToAdminDTO(admin));
        } else {
            admin = adminMapper.mapAdminDTOToAdmin(objectValueToDTO(redisTemplate.opsForValue().get("adminById" + id)));
        }
        return admin;
    }

    private AdminDTO objectValueToDTO(Object obj) {
        return objectMapper.convertValue(obj, new TypeReference<AdminDTO>() {});
    }
    // Returns the first administrator from the Admin table.
    // This record is used as a service administrator for pages and emails where school data is required.

    // Returns the first administrator from the Admin table.
    // This record is used as a service administrator for pages and emails where school data is required.
    public Admin getAdmin(){
        Admin admin = new Admin();
        if(redisTemplate.opsForValue().get("admin") == null){
            admin = adminRepository.findAll().get(0);
            redisTemplate.opsForValue().set("admin", adminMapper.mapAdminToAdminDTO(admin));
        } else {
            admin = adminMapper.mapAdminDTOToAdmin(objectValueToDTO(redisTemplate.opsForValue().get("admin")));
        }
        return admin;
    }
}
