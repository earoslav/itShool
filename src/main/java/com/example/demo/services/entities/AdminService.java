package com.example.demo.services.entities;

import com.example.demo.models.entities.Admin;
import com.example.demo.models.entities.Student;
import com.example.demo.repositories.entities.AdminRepository;
import org.springframework.stereotype.Service;
// Сервіс AdminService містить бізнес-операції для модуля «адміністратори».
// Контролери звертаються сюди, щоб не працювати напряму з репозиторіями, mapper-ами та правилами розкладу.
@Service
public class AdminService {
    private AdminRepository adminRepository;
    // Оновлює існуючий запис модуля «адміністратори».
    // Спочатку знаходить поточну сутність, переносить поля name, email, password і зберігає її назад у репозиторій.
    public Admin update(Integer id, Admin admin) {
        Admin existing = getById(id);
        existing.setName(admin.getName());
        existing.setEmail(admin.getEmail());
        existing.setPassword(admin.getPassword());
        existing = adminRepository.save(existing);
        return existing;
    }
    // Отримує через Spring залежності AdminRepository.
    // Ці сервіси й mapper-и потрібні методам класу для роботи з модулем «адміністратори» без ручного створення об’єктів.
    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }
    // Знаходить один запис модуля «адміністратори» за id.
    // Контролери викликають його перед редагуванням, видаленням або складанням сторінки з деталями.
    public Admin getById(int id){
        return adminRepository.getById(id);
    }
    // Повертає першого адміністратора з таблиці Admin.
    // Цей запис використовується як службовий адміністратор для сторінок і листів, де потрібні дані школи.
    public Admin getAdmin(){
        return adminRepository.findAll().get(0);
    }
}
