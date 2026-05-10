package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


// Тестовий клас перевіряє, що Spring Boot контекст проекту demo16 піднімається без помилок.
// Це базова перевірка конфігурації bean-ів, безпеки, сервісів і репозиторіїв перед запуском інших сценаріїв.
@SpringBootTest
class Demo16ApplicationTests {

    // Перевіряє, що Spring-контекст проекту може стартувати під час тестів.
    // Тест не виконує бізнес-сценарій, але швидко ловить помилки у конфігурації bean-ів.
    @Test
    void contextLoads() {
    }

}
