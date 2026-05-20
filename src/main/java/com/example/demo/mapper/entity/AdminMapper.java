package com.example.demo.mapper.entity;

import com.example.demo.dto.entities.AdminDTO;
import com.example.demo.mapper.univMapper.UniversalMapper;
import com.example.demo.models.entities.Admin;
import org.springframework.stereotype.Service;

// AdminMapper converts "administrators" module objects between Entity and DTO.
// This way, controllers and HTML templates receive simple objects without extra work with JPA relationships.
@Service
public class AdminMapper {
    // Creates an empty AdminMapper for Spring, JPA, or UniversalMapper.
    // Such a constructor is needed so the framework can create the object and then fill its fields.
    public AdminMapper() {
    }
    // Converts AdminDTO to Admin using UniversalMapper.
    // This allows controllers to pass a DTO to the template or assemble an Entity from form data without manual field copying.
    public Admin mapAdminDTOToAdmin(AdminDTO adminDTO){
        return UniversalMapper.generalMapper(adminDTO, Admin.class);
    }
    // Converts Admin to AdminDTO using UniversalMapper.
    // This allows controllers to pass a DTO to the template or assemble an Entity from form data without manual field copying.
    public AdminDTO mapAdminToAdminDTO(Admin admin){
        return UniversalMapper.generalMapper(admin, AdminDTO.class);
    }
}
