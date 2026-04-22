package com.example.demo.mapper.entity;

import com.example.demo.dto.entities.AdminDTO;
import com.example.demo.mapper.univMapper.UniversalMapper;
import com.example.demo.models.entities.Admin;
import org.springframework.stereotype.Service;

@Service
public class AdminMapper {
    public AdminMapper() {
    }

    public Admin mapAdminDTOToAdmin(AdminDTO adminDTO){
        return UniversalMapper.generalMapper(adminDTO, Admin.class);
    }
    public AdminDTO mapAdminToAdminDTO(Admin admin){
        return UniversalMapper.generalMapper(admin, AdminDTO.class);
    }
}
