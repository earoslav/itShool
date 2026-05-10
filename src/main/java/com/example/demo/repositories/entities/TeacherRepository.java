package com.example.demo.repositories.entities;

import com.example.demo.models.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

// Репозиторій TeacherRepository дає доступ до таблиць модуля «викладачі» через Spring Data JPA.
// Назви методів нижче перетворюються Spring-ом у SQL-запити без ручного написання query.
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
    // Spring Data JPA створює перевірку існування запису «викладачі» за email.
    // Сервіси використовують її для валідації форм перед створенням користувача або профілю.
    public boolean existsByEmail(String email);
    // Шукає записи модуля «викладачі» за умовами: акаунтом користувача.
    // Назва методу описує критерії, які Spring Data перетворює у запит до бази.
    public Teacher findByUserId(int userId);
}
