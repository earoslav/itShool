package com.example.demo.mapper.entity;

import com.example.demo.dto.entities.AdminDTO;
import com.example.demo.mapper.univMapper.UniversalMapper;
import com.example.demo.models.entities.Admin;
import org.springframework.stereotype.Service;

// Mapper AdminMapper перетворює об’єкти модуля «адміністратори» між Entity та DTO.
// Так контролери й HTML-шаблони отримують прості об’єкти без зайвої роботи з JPA-зв’язками.
@Service
public class AdminMapper {
    // Створює порожній AdminMapper для Spring, JPA або UniversalMapper.
    // Такий конструктор потрібен, щоб фреймворк міг створити об’єкт і потім заповнити його поля.
    public AdminMapper() {
    }
    // Перетворює AdminDTO у Admin через UniversalMapper.
    // Це дозволяє контролерам передавати у шаблон DTO або збирати Entity з даних форми без ручного копіювання полів.
    public Admin mapAdminDTOToAdmin(AdminDTO adminDTO){
        return UniversalMapper.generalMapper(adminDTO, Admin.class);
    }
    // Перетворює Admin у AdminDTO через UniversalMapper.
    // Це дозволяє контролерам передавати у шаблон DTO або збирати Entity з даних форми без ручного копіювання полів.
    public AdminDTO mapAdminToAdminDTO(Admin admin){
        return UniversalMapper.generalMapper(admin, AdminDTO.class);
    }
}
