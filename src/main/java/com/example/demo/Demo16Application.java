package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
// Після старту Spring піднімає контролери, сервіси, репозиторії та фоновий менеджер уроків.

// Головний клас запускає Spring Boot застосунок demo16 і вмикає планувальник.
// Після старту Spring піднімає контролери, сервіси, репозиторії та фоновий менеджер уроків.
@SpringBootApplication
@EnableScheduling
public class Demo16Application {
    // Запускає SpringApplication для класу Demo16Application.
    // З цього моменту застосунок реєструє MVC-контролери, сервіси, JPA-репозиторії та Scheduled-задачі.
    public static void main(String[] args) {
        SpringApplication.run(Demo16Application.class, args);
    }

}
