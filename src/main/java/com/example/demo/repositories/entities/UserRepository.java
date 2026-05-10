package com.example.demo.repositories.entities;

import com.example.demo.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;
// Репозиторій UserRepository дає доступ до таблиць модуля «акаунти користувачів» через Spring Data JPA.
// Назви методів нижче перетворюються Spring-ом у SQL-запити без ручного написання query.
@Repository
@EnableJpaRepositories
public interface UserRepository extends JpaRepository<User, Integer> {
    // Шукає записи модуля «акаунти користувачів» за умовами: email.
    // Назва методу описує критерії, які Spring Data перетворює у запит до бази.
    Optional<User> findByEmail(String email);
    // Шукає записи модуля «акаунти користувачів» за умовами: email, role.
    // Назва методу описує критерії, які Spring Data перетворює у запит до бази.
    User findByEmailAndRole(String email, String role);


}
