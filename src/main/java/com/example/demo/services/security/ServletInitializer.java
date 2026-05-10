package com.example.demo.services.security;

import com.example.demo.Demo16Application;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

// Ініціалізатор потрібен, коли застосунок запускається як WAR у зовнішньому servlet-контейнері.
// Він вказує Spring, що стартовим класом лишається Demo16Application.
public class ServletInitializer extends SpringBootServletInitializer {
    // Підключає Demo16Application як джерело Spring Boot конфігурації для WAR-запуску.
    // Це дозволяє розгорнути той самий застосунок не лише через main, а й у зовнішньому servlet-контейнері.
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Demo16Application.class);
    }

}
