package com.example.demo.repositories.entities;

import com.example.demo.models.entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Репозиторій AdminRepository дає доступ до таблиць модуля «адміністратори» через Spring Data JPA.
// Назви методів нижче перетворюються Spring-ом у SQL-запити без ручного написання query.
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    // Повертає всі записи «адміністратори» з таблиці.
    // Сервіс використовує цей метод для списків і довідників у формах.
    public List<Admin> findAll();
}
