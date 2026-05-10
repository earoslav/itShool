package com.example.demo.repositories.entities;

import com.example.demo.models.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

// Репозиторій StudentRepository дає доступ до таблиць модуля «студенти» через Spring Data JPA.
// Назви методів нижче перетворюються Spring-ом у SQL-запити без ручного написання query.
@Repository
@EnableJpaRepositories
public interface StudentRepository extends JpaRepository<Student, Integer> {
    // Шукає записи модуля «студенти» за умовами: email.
    // Назва методу описує критерії, які Spring Data перетворює у запит до бази.
    public Student findByEmail(String email);
    // Шукає записи модуля «студенти» за умовами: акаунтом користувача.
    // Назва методу описує критерії, які Spring Data перетворює у запит до бази.
    public Student findByUserId(int userId);

}
