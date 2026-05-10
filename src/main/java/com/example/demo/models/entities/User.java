package com.example.demo.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

// Модель User описує сутність модуля «акаунти користувачів» у базі даних або службовий об’єкт проекту.
// Поля класу читають сервіси, репозиторії та mapper-и під час створення сторінок і збереження змін.
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected int id;
    @Column(name="name")
    protected String name;
    @Column(name="email")
    protected String email;
    @Column(name="password")
    protected String password;
    @Column(name="role")
    protected String role;
    // Отримує через Spring залежності String.
    // Ці сервіси й mapper-и потрібні методам класу для роботи з модулем «акаунти користувачів» без ручного створення об’єктів.
    public User(String name, String email, String password, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Повертає поле authorities об’єкта User.
    // Його читають mapper-и, сервіси або Thymeleaf-шаблони під час показу сторінок і заповнення форм.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    // Повертає поле username об’єкта User.
    // Його читають mapper-и, сервіси або Thymeleaf-шаблони під час показу сторінок і заповнення форм.
    @Override
    public String getUsername() {
        return getEmail();
    }

    // Повертає поле accountNonExpired об’єкта User.
    // Його читають mapper-и, сервіси або Thymeleaf-шаблони під час показу сторінок і заповнення форм.
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // Повертає поле accountNonLocked об’єкта User.
    // Його читають mapper-и, сервіси або Thymeleaf-шаблони під час показу сторінок і заповнення форм.
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // Повертає поле credentialsNonExpired об’єкта User.
    // Його читають mapper-и, сервіси або Thymeleaf-шаблони під час показу сторінок і заповнення форм.
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // Повертає поле enabled об’єкта User.
    // Його читають mapper-и, сервіси або Thymeleaf-шаблони під час показу сторінок і заповнення форм.
    @Override
    public boolean isEnabled() {
        return true;
    }
}
