package com.example.demo.services.entities;

import com.example.demo.models.entities.Admin;
import com.example.demo.models.entities.Student;
import com.example.demo.repositories.entities.AdminRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
// Сервіс AdminService містить бізнес-операції для модуля «адміністратори».
// Контролери звертаються сюди, щоб не працювати напряму з репозиторіями, mapper-ами та правилами розкладу.
@Service
public class AdminService {
    private AdminRepository adminRepository;
    private RedisTemplate redis;

    // Оновлює існуючий запис модуля «адміністратори».
    // Спочатку знаходить поточну сутність, переносить поля name, email, password і зберігає її назад у репозиторій.
    public Admin update(Integer id, Admin admin) {
        Admin existing = getById(id);
        existing.setName(admin.getName());
        existing.setEmail(admin.getEmail());
        existing.setPassword(admin.getPassword());
        existing = adminRepository.save(existing);
        redis.delete("adminById" + id);
        redis.delete("admin");
        return existing;
    }
    // Отримує через Spring залежності AdminRepository.
    // Ці сервіси й mapper-и потрібні методам класу для роботи з модулем «адміністратори» без ручного створення об’єктів.
    public AdminService(AdminRepository adminRepository, @Qualifier("redisTemplate") RedisTemplate redis) {
        this.adminRepository = adminRepository;
        this.redis = redis;
    }
    // Знаходить один запис модуля «адміністратори» за id.
    // Контролери викликають його перед редагуванням, видаленням або складанням сторінки з деталями.
    public Admin getById(int id){
        Admin admin = new Admin();
        if(redis.opsForValue().get("adminById" + id) == null){
            admin = adminRepository.getById(id);
            redis.opsForValue().set("adminById" + id, admin);
        } else {
            admin = (Admin) redis.opsForValue().get("adminById" + id);
        }
        return admin;
    }
    // Повертає першого адміністратора з таблиці Admin.
    // Цей запис використовується як службовий адміністратор для сторінок і листів, де потрібні дані школи.

    public Admin getAdmin(){
        Admin admin = new Admin();
        if(redis.opsForValue().get("admin") == null){
            admin = adminRepository.findAll().get(0);
            redis.opsForValue().set("admin", admin);
        } else {
            admin = (Admin) redis.opsForValue().get("admin");
        }
        return admin;
    }
}
